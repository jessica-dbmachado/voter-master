package br.edu.ulbra.election.voter.input.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Login Information")
public class LoginInput {

    @ApiModelProperty(example = "john@doe.com", notes = "Voter email")
    private String email;
    @ApiModelProperty(example = "12345678", notes = "Password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
