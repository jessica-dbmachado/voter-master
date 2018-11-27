package br.edu.ulbra.election.voter.output.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Login Output Information")
public class LoginOutput {

    @ApiModelProperty(example = "1231qdjwabsiu2rq", notes = "Token Identification")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
