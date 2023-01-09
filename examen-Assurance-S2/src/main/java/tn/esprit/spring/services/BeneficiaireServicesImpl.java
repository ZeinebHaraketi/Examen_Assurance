package tn.esprit.spring.services;

import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.Assurance;
import tn.esprit.spring.entities.Beneficiaire;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.TypeContrat;
import tn.esprit.spring.repositories.IAssuranceRepository;
import tn.esprit.spring.repositories.IBeneficiaireRepository;
import tn.esprit.spring.repositories.IContratRepository;

@Slf4j
@Service
public class BeneficiaireServicesImpl implements IBeneficiaireServices {

	@Autowired
	IBeneficiaireRepository benefRepository;

	@Autowired
	IAssuranceRepository assuranceRepository;

	@Autowired
	IContratRepository contratRepository;

	@Override
	public int ajouterBeneficaire(Beneficiaire bf) {
		return benefRepository.save(bf).getCin();
	}

	@Transactional
	@Override
	public int ajouterAssurance(Assurance a, int cinBf) {
		Beneficiaire beneficiaire = benefRepository.getByCin(cinBf);
		a.setBeneficiaire(beneficiaire);
		Contrat c = contratRepository.save(a.getContrat());
		a.setContrat(c);
		return assuranceRepository.save(a).getIdAssurance();
	}

	@Override
	public Contrat getContratBf(int idBf) {
		Beneficiaire beneficiaire = benefRepository.findById(idBf).orElse(null);

		Contrat contrat = beneficiaire.getAssurances().get(0).getContrat();
		Date oldDate = contrat.getDateEffet();

		for (int i = 1; i < beneficiaire.getAssurances().size(); i++) {
			Assurance assurance = beneficiaire.getAssurances().get(i);
			if (oldDate.after(assurance.getContrat().getDateEffet())) {
				contrat = assurance.getContrat();
			}
		}
		return contrat;
	}

	@Override
	public float getMontantBf(int cinBf) {
		Beneficiaire beneficiaire = benefRepository.getByCin(cinBf);
		float montantContrat = 0;
		for (Assurance ass : beneficiaire.getAssurances()) {
			if (ass.getContrat().getType() == TypeContrat.Mensuel) {
				montantContrat += ass.getMontant() * 12;
			} else if (ass.getContrat().getType() == TypeContrat.Semestriel) {
				montantContrat += ass.getMontant() * 2;
			} else {
				montantContrat += ass.getMontant();
			}
		}
		/* Methode 1 : JPQL pour annuel seulement */
		// return
		// assuranceRepository.getMontantAnnuelByBf(beneficiaire.getIdBeneficiaire());

		return montantContrat;
	}

	@Override
	public Set<Beneficiaire> getBeneficairesAsC(TypeContrat tc) {
		return benefRepository.getByAssuranceType(tc);
	}

	@Override
	@Scheduled(cron = "*/60 * * * * *")
	public void statistiques() {

		TreeMap<Integer, Integer> myStat = new TreeMap<>(Collections.reverseOrder());

		for (Beneficiaire b : benefRepository.findAll()) {
			myStat.put(b.getAssurances().size(), b.getCin());
		}
		for (Entry<Integer, Integer> va : myStat.entrySet()) {
			log.info(va.getKey() + "|" + va.getValue());
		}
	}

}
