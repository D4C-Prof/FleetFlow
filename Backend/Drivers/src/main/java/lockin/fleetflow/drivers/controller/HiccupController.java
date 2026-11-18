package lockin.fleetflow.drivers.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.drivers.dto.HiccupDTO;
import lockin.fleetflow.drivers.dto.IMapper;
import lockin.fleetflow.drivers.service.IHiccupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Driver Hiccups", description = "Record of cases where Drivers were or will be out of reach")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hiccups")
public class HiccupController
{
    private final IHiccupService hicSer;
    private final IMapper map;

    @Operation(summary = "List all hiccup records")
    @GetMapping
    public List<HiccupDTO> findAll(){return hicSer.findAll();}

    @Operation(summary = "Add a hiccup")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public HiccupDTO addHiccup(@Valid @RequestBody HiccupDTO hdto) {return hicSer.create(hdto);}

    @Operation(summary = "Call for a certain hiccup")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "201", description = "Hiccup found")
    @ApiResponse(responseCode = "404", description= "Hiccup not in database")
    public HiccupDTO getHiccup(@PathVariable long id){return hicSer.findById(id);}

    @Operation(summary = "Update a hiccup")
    @PutMapping("/update/{id}")
    public HiccupDTO updateHiccup(@PathVariable long id, @Valid @RequestBody HiccupDTO dh){return hicSer.update(id, map.toEntity(dh));}

    @Operation(summary = "Delete a hiccup")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHiccup(@PathVariable long id){hicSer.deleteById(id);}
}
