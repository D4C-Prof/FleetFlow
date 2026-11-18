package lockin.fleetflow.drivers.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.drivers.dto.AssignmentDTO;
import lockin.fleetflow.drivers.dto.IMapper;
import lockin.fleetflow.drivers.service.IAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Drivers Assignment", description = "Record of cases where Drivers were or will be out of reach")
@RestController
@RequiredArgsConstructor
@RequestMapping("/Assignments")
public class AssignmentController
{
    private final IAssignmentService assSer;
    private final IMapper map;

    @Operation(summary = "List all Assignment records")
    @GetMapping
    public List<AssignmentDTO> findAll(){return assSer.findAll();}

    @Operation(summary = "Add a Assignment")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentDTO addAssignment(@Valid @RequestBody AssignmentDTO adto) {return assSer.create(adto);}

    @Operation(summary = "Call for a certain Assignment")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "201", description = "Assignment found")
    @ApiResponse(responseCode = "404", description= "Assignment not in database")
    public AssignmentDTO getAssignment(@PathVariable long id){return assSer.findById(id);}

    @Operation(summary = "Update a Assignment")
    @PutMapping("/update/{id}")
    public AssignmentDTO updateAssignment(@PathVariable long id, @Valid @RequestBody AssignmentDTO dh){return assSer.update(id, map.toEntity(dh));}

    @Operation(summary = "Delete a Assignment")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(@PathVariable long id){assSer.deleteById(id);}
}
