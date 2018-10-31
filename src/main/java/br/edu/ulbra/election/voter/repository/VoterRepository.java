package br.edu.ulbra.election.voter.repository;

import br.edu.ulbra.election.voter.model.Voter;
import org.springframework.data.repository.CrudRepository;

public interface VoterRepository extends CrudRepository<Voter, Long> {
}
