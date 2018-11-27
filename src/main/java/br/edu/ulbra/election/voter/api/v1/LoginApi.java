package br.edu.ulbra.election.voter.api.v1;

import br.edu.ulbra.election.voter.input.v1.LoginInput;
import br.edu.ulbra.election.voter.output.v1.LoginOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/login/v1")
public class LoginApi {

    private final LoginService loginService;

    @Autowired
    public LoginApi(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/")
    public LoginOutput login(@RequestBody LoginInput loginInput){
        return loginService.login(loginInput);
    }

    @GetMapping("/check/{token}")
    public VoterOutput checkToken(@PathVariable(value = "token") String token){
        return loginService.checkToken(token);
    }
}
