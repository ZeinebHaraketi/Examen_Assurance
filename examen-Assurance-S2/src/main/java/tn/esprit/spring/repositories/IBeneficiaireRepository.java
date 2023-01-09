package tn.esprit.spring.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Beneficiaire;
import tn.esprit.spring.entities.TypeContrat;

@Repository
public interface IBeneficiaireRepository extends CrudRepository<Beneficiaire, Integer>{

	Beneficiaire getByCin(int cinBenef);
	
	@Query("Select b From Beneficiaire b join b.assurances ass join ass.contrat c where c.type = :tc")
	Set<Beneficiaire> getByAssuranceType(@Param("tc") TypeContrat tc);
	
	
	
}
