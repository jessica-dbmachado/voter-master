package br.edu.ulbra.election.voter.repository;

import br.edu.ulbra.election.voter.model.Token;
import br.edu.ulbra.election.voter.model.Voter;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
    Token findFirstByVoter(Voter voter);
    Token findFirstByToken(String token);
}
