package br.edu.ulbra.election.voter.repository;

import br.edu.ulbra.election.voter.model.Voter;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface VoterRepository extends CrudRepository<Voter, Long> {
	
	Optional<String> findByEmail(String email);
	
}
