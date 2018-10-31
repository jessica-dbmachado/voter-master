package br.edu.ulbra.election.voter.service;

import br.edu.ulbra.election.voter.TestConfig;
import br.edu.ulbra.election.voter.builder.VoterBuilder;
import br.edu.ulbra.election.voter.exception.GenericOutputException;
import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.repository.VoterRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(VoterService.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class VoterServiceTest {

    @MockBean
    private VoterRepository voterRepository;

    @Autowired
    private VoterService voterService;

    @Test
    public void shouldReturnEmptyList(){
        given(voterRepository.findAll())
                .willReturn(new ArrayList<>());
        List<VoterOutput> voterOutputList = voterService.getAll();
        Assert.assertEquals(0, voterOutputList.size());
    }

    @Test
    public void shouldFindAllElements(){
        given(voterRepository.findAll())
                .willReturn(VoterBuilder.getVoterList());
        List<VoterOutput> voterOutputList = voterService.getAll();
        Assert.assertEquals(1, voterOutputList.size());
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailGetByIdNotFound(){
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        voterService.getById(1L);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailGetByIdNull(){
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        voterService.getById(null);
    }

    @Test
    public void shouldGetById(){
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.of(VoterBuilder.getVoter()));
        VoterOutput voterOutput = voterService.getById(1L);
        Assert.assertEquals((Long)1L, voterOutput.getId());
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailCreateInvalidEmail(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setEmail(null);
        voterService.create(voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailCreateEmptyEmail(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setEmail("");
        voterService.create(voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailCreateInvalidName(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setName(null);
        voterService.create(voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailCreateEmptyName(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setName("");
        voterService.create(voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailCreateEmptyPassword(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setPassword("");
        voterService.create(voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailCreatePasswordsDoesntMatch(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setPassword("123");
        voterService.create(voterInput);
    }

    @Test
    public void shouldCreate(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        given(voterRepository.save(any()))
                .willReturn(VoterBuilder.getVoter());
        VoterOutput voterOutput = voterService.create(voterInput);
        Assert.assertEquals(voterInput.getName(), voterOutput.getName());
        Assert.assertEquals(voterInput.getEmail(), voterOutput.getEmail());
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateVoterIdNull(){
        voterService.update(null, null);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateInvalidEmail(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setEmail(null);
        voterService.update(1L, voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateEmptyEmail(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setEmail("");
        voterService.update(1L, voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateInvalidName(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setName(null);
        voterService.update(1L, voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateEmptyName(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setName("");
        voterService.update(1L, voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateEmptyPassword(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setPasswordConfirm("");
        voterService.update(1L, voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdatePasswordsDoesntMatch(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setPassword("123");
        voterService.update(1L, voterInput);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailUpdateIdNotFound(){
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        voterService.update(1L, VoterBuilder.getVoterInput());
    }

    @Test
    public void shouldUpdateWithoutPassword(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        voterInput.setPassword(null);
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.of(VoterBuilder.getVoter()));
        given(voterRepository.save(any()))
                .willReturn(VoterBuilder.getVoter());

        VoterOutput voterOutput = voterService.update(1L, voterInput);
        Assert.assertEquals(voterInput.getName(), voterOutput.getName());
        Assert.assertEquals(voterInput.getEmail(), voterOutput.getEmail());
    }

    @Test
    public void shouldUpdate(){
        VoterInput voterInput = VoterBuilder.getVoterInput();
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.of(VoterBuilder.getVoter()));
        given(voterRepository.save(any()))
                .willReturn(VoterBuilder.getVoter());
        VoterOutput voterOutput = voterService.update(1L, voterInput);
        Assert.assertEquals(voterInput.getName(), voterOutput.getName());
        Assert.assertEquals(voterInput.getEmail(), voterOutput.getEmail());
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailDeleteVoterIdNull(){
        voterService.delete(null);
    }

    @Test(expected = GenericOutputException.class)
    public void shouldFailDeleteIdNotFound(){
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        voterService.delete(1L);
    }

    @Test
    public void shouldDelete(){
        given(voterRepository.findById(anyLong()))
                .willReturn(Optional.of(VoterBuilder.getVoter()));
        doNothing().when(voterRepository).delete(any());
        GenericOutput genericOutput = voterService.delete(1L);
        Assert.assertEquals("Voter deleted", genericOutput.getMessage());
    }


}
