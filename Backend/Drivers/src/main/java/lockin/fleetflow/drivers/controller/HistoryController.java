package lockin.fleetflow.drivers.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.drivers.dto.HistoryDTO;
import lockin.fleetflow.drivers.dto.IMapper;
import lockin.fleetflow.drivers.service.IHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Drivers History", description = "Record of cases where Drivers were or will be out of reach")
@RestController
@RequiredArgsConstructor
@RequestMapping("/Histories")
public class HistoryController
{
    private final IHistoryService hisSer;
    private final IMapper map;

    @Operation(summary = "List all History records")
    @GetMapping
    public List<HistoryDTO> findAll(){return hisSer.findAll();}

    @Operation(summary = "Add a History")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public HistoryDTO addHistory(@Valid @RequestBody HistoryDTO hdto) {return hisSer.create(hdto);}

    @Operation(summary = "Call for a certain History")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "201", description = "History found")
    @ApiResponse(responseCode = "404", description= "History not in database")
    public HistoryDTO getHistory(@PathVariable long id){return hisSer.findById(id);}

    @Operation(summary = "Update a History")
    @PutMapping("/update/{id}")
    public HistoryDTO updateHistory(@PathVariable long id, @Valid @RequestBody HistoryDTO dh){return hisSer.update(id, map.toEntity(dh));}

    @Operation(summary = "Delete a History")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistory(@PathVariable long id){hisSer.deleteById(id);}
}
