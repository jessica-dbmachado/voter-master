package br.edu.ulbra.election.voter.service;

import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.LoginInput;
import br.edu.ulbra.election.voter.model.Token;
import br.edu.ulbra.election.voter.model.Voter;
import br.edu.ulbra.election.voter.output.v1.LoginOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.repository.TokenRepository;
import br.edu.ulbra.election.voter.repository.VoterRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class LoginService {

    private final VoterRepository voterRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private static final String INVALID_CREDENTIALS = "Invalid Credentials";
    private static final String INVALID_TOKEN = "Invalid Token";
    private static final long EXPIRE_TIME_IN_MILLIS = 2L * 60000L;

    @Autowired
    public LoginService(VoterRepository voterRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper){
        this.voterRepository = voterRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public LoginOutput login(LoginInput loginInput){

        if (loginInput.getEmail() == null || loginInput.getPassword() == null){
            throw new GenericOutputException("Invalid input");
        }

        Voter voter = voterRepository.findFirstByEmail(loginInput.getEmail());

        if (voter == null){
            throw new GenericOutputException(INVALID_CREDENTIALS);
        }

        if (!passwordEncoder.matches(loginInput.getPassword(), voter.getPassword())){
            throw new GenericOutputException(INVALID_CREDENTIALS);
        }

        Token token = tokenRepository.findFirstByVoter(voter);
        if (token == null){
            token = new Token();
            token.setVoter(voter);
        }

        Calendar date = Calendar.getInstance();
        long timeStamp = date.getTimeInMillis();
        Date expireDate = new Date(timeStamp + EXPIRE_TIME_IN_MILLIS);

        String tokenString = generateToken(voter, expireDate);
        token.setToken(tokenString);
        token.setExpireDate(expireDate);
        tokenRepository.save(token);

        LoginOutput loginOutput = new LoginOutput();
        loginOutput.setToken(tokenString);
        return loginOutput;
    }

    public VoterOutput checkToken(String tokenInput){
        if(StringUtils.isBlank(tokenInput)){
            throw new GenericOutputException(INVALID_TOKEN);
        }

        Token token = tokenRepository.findFirstByToken(tokenInput);
        if (token == null){
            throw new GenericOutputException(INVALID_TOKEN);
        }

        Date actualDate = new Date();
        if (!token.getExpireDate().after(actualDate)){
            throw new GenericOutputException("Expired Token");
        }

        return modelMapper.map(token.getVoter(), VoterOutput.class);
    }

    private String generateToken(Voter voter, Date expireDate){
        String openToken = String.format("%s%s%s", voter.getEmail(), voter.getName(), expireDate.toString());
        return passwordEncoder.encode(openToken);
    }
}
