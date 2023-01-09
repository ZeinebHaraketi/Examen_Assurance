package tn.esprit.spring.services;

import java.util.Set;

import tn.esprit.spring.entities.Assurance;
import tn.esprit.spring.entities.Beneficiaire;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.TypeContrat;

public interface IBeneficiaireServices {

	int ajouterBeneficaire(Beneficiaire bf);

	int ajouterAssurance(Assurance a, int cinBf);

	Contrat getContratBf(int idBf);

	float getMontantBf(int cinBf);

	Set<Beneficiaire> getBeneficairesAsC(TypeContrat tc);

	void statistiques();
}
