package br.edu.ulbra.election.voter.api.v1;

import br.edu.ulbra.election.voter.TestConfig;
import br.edu.ulbra.election.voter.builder.VoterBuilder;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.service.VoterService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(VoterApi.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class VoterApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoterService voterService;

    private final Gson gson = new Gson();
    private String URL_BASE = "/v1/voter/";

    @Test
    public void getAll() throws Exception {
        given(voterService.getAll())
                .willReturn(VoterBuilder.getVoterOutputList());

        mockMvc.perform(get(URL_BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].email", equalTo("test@test.com")))
                .andExpect(jsonPath("$[0].name", equalTo("Voter Name")));
    }

    @Test
    public void getOne() throws Exception{
        given(voterService.getById(anyLong()))
            .willReturn(VoterBuilder.getVoterOutput());
        mockMvc.perform(get(URL_BASE + "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.email", equalTo("test@test.com")))
                .andExpect(jsonPath("$.name", equalTo("Voter Name")));
    }

    @Test
    public void create() throws Exception{
        given(voterService.create(any()))
                .willReturn(VoterBuilder.getVoterOutput());

        mockMvc.perform(put(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(VoterBuilder.getVoterInput()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.email", equalTo("test@test.com")))
                .andExpect(jsonPath("$.name", equalTo("Voter Name")));
    }

    @Test
    public void update() throws Exception{
        given(voterService.update(anyLong(), any()))
                .willReturn(VoterBuilder.getVoterOutput());

        mockMvc.perform(post(URL_BASE + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(VoterBuilder.getVoterInput()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.email", equalTo("test@test.com")))
                .andExpect(jsonPath("$.name", equalTo("Voter Name")));
    }

    @Test
    public void deleteVoter() throws Exception{
        given(voterService.delete(anyLong()))
                .willReturn(new GenericOutput("OK"));

        mockMvc.perform(delete(URL_BASE + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(VoterBuilder.getVoterInput()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("OK")));
    }

}
