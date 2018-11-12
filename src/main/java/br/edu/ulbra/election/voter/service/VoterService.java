package br.edu.ulbra.election.voter.service;

import br.edu.ulbra.election.voter.config.Criptografia;
import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.model.Voter;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.repository.VoterRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class VoterService {

    private final VoterRepository voterRepository;

    private final ModelMapper modelMapper;

    private final Criptografia passwordEncoder;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTER_NOT_FOUND = "Voter not found";
   

    @Autowired
    public VoterService(VoterRepository voterRepository, ModelMapper modelMapper, Criptografia passwordEncoder){
        this.voterRepository = voterRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<VoterOutput> getAll(){
        Type voterOutputListType = new TypeToken<List<VoterOutput>>(){}.getType();
     
        return modelMapper.map(voterRepository.findAll(), voterOutputListType);
    }
    
    

    public VoterOutput create(VoterInput voterInput) {
        validateInput(voterInput, false);
        Voter voter = modelMapper.map(voterInput, Voter.class);
      
        
        voterInput.setPassword(passwordEncoder.criptografar(voterInput.getPassword()));
    	
       
        voter = voterRepository.save(voter);
         return modelMapper.map(voter, VoterOutput.class);
    }

    public VoterOutput getById(Long voterId){
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        return modelMapper.map(voter, VoterOutput.class);
    }

    public VoterOutput update(Long voterId, VoterInput voterInput) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(voterInput, true);

        Voter voter = voterRepository.findById(voterId).orElse(null);
        
        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        voter.setEmail(voterInput.getEmail());
        voter.setName(voterInput.getName());
        
        //Ao criar um eleitor ou alterar a senha, deve verificar se a senha é igual à confirmação
        if (!StringUtils.isBlank(voterInput.getPassword())) {
        	if (!voterInput.getPassword().equals(voterInput.getPasswordConfirm())){
                throw new GenericOutputException("Passwords doesn't match");
            }else
            voter.setPassword(passwordEncoder.criptografar(voterInput.getPassword()));
        }
        voter = voterRepository.save(voter);
        return modelMapper.map(voter, VoterOutput.class);
    }

    public GenericOutput delete(Long voterId) {
        if (voterId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Voter voter = voterRepository.findById(voterId).orElse(null);

        if (voter == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        voterRepository.delete(voter);

        return new GenericOutput("Voter deleted");
    }

    private void validateInput(VoterInput voterInput, boolean isUpdate){
        if (StringUtils.isBlank(voterInput.getEmail() ) ){
            throw new GenericOutputException("Invalid email");
        }else {
        		//busca email do input no repositorio e armazena na variavel para verificar dps
        	 String email = voterRepository.findByEmail(voterInput.getEmail()).orElse(null);
        	 
        	 if (email != null)
        	 {
            	  throw new GenericOutputException("Email already registered!");
        	 }
          }
        
        if (StringUtils.isBlank(voterInput.getName())){
            throw new GenericOutputException("Invalid name");
        }
        
        if (voterInput.getName().length() < 5){
            throw new GenericOutputException("Short name, must have more than 5 letters");
        }
        //Ao criar um eleitor ou alterar a senha, deve verificar se a senha é igual à confirmação
        if (!StringUtils.isBlank(voterInput.getPassword())) {
        	if (!voterInput.getPassword().equals(voterInput.getPasswordConfirm())){
                throw new GenericOutputException("Passwords doesn't match");
            }
         }	 
            else {
            	throw new GenericOutputException(" the password should not be empty");
            	
            }
       
        
        
        //validate is has lastname
        if(voterInput.getName().indexOf(" ") == -1){
        	{
        		throw new GenericOutputException("Please add your last name as well");
        	}
        }
        
        
    }

}
