package br.edu.ulbra.election.voter.api.v1;

import br.edu.ulbra.election.voter.input.v1.VoterInput;
import br.edu.ulbra.election.voter.output.v1.GenericOutput;
import br.edu.ulbra.election.voter.output.v1.VoterOutput;
import br.edu.ulbra.election.voter.service.VoterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/voter")
public class VoterApi {

    private final VoterService voterService;

    @Autowired
    public VoterApi(VoterService voterService){
        this.voterService = voterService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Get voters List")
    public List<VoterOutput> getAll(){
        return voterService.getAll();
    }

    @GetMapping("/{voterId}")
    @ApiOperation(value = "Get voter by Id")
    public VoterOutput getById(@PathVariable(name="voterId") Long voterId){
        return voterService.getById(voterId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new voter")
    public VoterOutput create(@RequestBody VoterInput voterInput){
        return voterService.create(voterInput);
    }

    @PutMapping("/{voterId}")
    @ApiOperation(value = "Update voter")
    public VoterOutput update(@PathVariable Long voterId, @RequestBody VoterInput voterInput){
        return voterService.update(voterId, voterInput);
    }

    @DeleteMapping("/{voterId}")
    @ApiOperation(value = "Delete voter")
    public GenericOutput delete(@PathVariable Long voterId){
        return voterService.delete(voterId);
    }

}
