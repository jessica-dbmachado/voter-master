package br.edu.ulbra.election.voter.builder;

import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.model.Voter;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;

import java.util.Collections;
import java.util.List;

public class VoterBuilder {

    public static VoterOutput getVoterOutput(){
        VoterOutput voterOutput = new VoterOutput();
        voterOutput.setId(1L);
        voterOutput.setEmail("test@test.com");
        voterOutput.setName("Voter Name");
        return voterOutput;
    }

    public static List<VoterOutput> getVoterOutputList(){
        return Collections.singletonList(getVoterOutput());
    }

    public static VoterInput getVoterInput() {
        VoterOutput voterOutput = getVoterOutput();
        VoterInput voterInput = new VoterInput();
        voterInput.setName(voterOutput.getName());
        voterInput.setEmail(voterOutput.getEmail());
        voterInput.setPassword("123456");
        voterInput.setPasswordConfirm("123456");
        return voterInput;
    }

    public static Voter getVoter(){
        Voter voter = new Voter();
        voter.setId(1L);
        voter.setEmail("test@test.com");
        voter.setName("Voter Name");
        voter.setPassword("123456");
        return voter;
    }

    public static List<Voter> getVoterList() {
        return Collections.singletonList(getVoter());
    }
}
