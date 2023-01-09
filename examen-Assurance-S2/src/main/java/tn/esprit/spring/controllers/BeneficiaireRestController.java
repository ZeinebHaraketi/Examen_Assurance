package tn.esprit.spring.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.spring.entities.Assurance;
import tn.esprit.spring.entities.Beneficiaire;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.TypeContrat;
import tn.esprit.spring.services.IBeneficiaireServices;



@RestController
@RequestMapping("/examen")
public class BeneficiaireRestController {
	
	@Autowired
	IBeneficiaireServices benefServices;
	
	@PostMapping("/addBenef")
	@ResponseBody
	public int addBeneficiaire(@RequestBody Beneficiaire beneficiaire){
		
		return benefServices.ajouterBeneficaire(beneficiaire);
	}
	@PostMapping("/addAssurance/{cin-benef}")
	@ResponseBody
	public int addAssurance(@RequestBody Assurance assurance, @PathVariable("cin-benef") int cinB){
		return benefServices.ajouterAssurance(assurance, cinB);
	}
	@GetMapping("/getContratBf/{id-benef}")
	@ResponseBody
	public Contrat getContratByBf(@PathVariable("id-benef")int idBf){
		return benefServices.getContratBf(idBf);
	}
	
	@GetMapping("/getMontantBf/{cin-benef}")
	public float getMontantBf(@PathVariable("cin-benef") int cinBf){
		return benefServices.getMontantBf(cinBf);
	}
	@GetMapping("/getBeneficairesAsC/{typeC}")
	public Set<Beneficiaire> getBeneficairesAsC(@PathVariable("typeC")TypeContrat tc) {
		return benefServices.getBeneficairesAsC(tc);
	}

}
