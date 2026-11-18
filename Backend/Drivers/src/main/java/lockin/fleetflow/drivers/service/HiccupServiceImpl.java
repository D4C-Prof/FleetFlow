package lockin.fleetflow.drivers.service;

import lockin.fleetflow.drivers.domain.DriverHiccup;
import lockin.fleetflow.drivers.dto.HiccupDTO;
import lockin.fleetflow.drivers.dto.IMapper;
import lockin.fleetflow.drivers.exception.ResourceNotFoundException;
import lockin.fleetflow.drivers.repository.IHiccupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class HiccupServiceImpl implements IHiccupService
{
    private final IHiccupRepository hiccupRepo;
    private final IMapper map;

    @Override
    public HiccupDTO create(HiccupDTO hiccupDTO) {
        DriverHiccup driverHiccup = map.toEntity(hiccupDTO);
        return map.toDTO(hiccupRepo.save(driverHiccup));
    }

    @Override
    public HiccupDTO findById(Long idDriHic) {

        DriverHiccup driHic = hiccupRepo.findById(idDriHic)
                .orElseThrow(() -> new ResourceNotFoundException("Driver Hiccup", idDriHic));
        return map.toDTO(driHic);
    }

    @Override
    public List<HiccupDTO> findAll() {
        return map.hicToDTOList(hiccupRepo.findAll());
    }

    @Override
    public HiccupDTO update(long idDriHic, DriverHiccup newH) {
        DriverHiccup old = hiccupRepo.findById(idDriHic)
                .orElseThrow(() -> new ResourceNotFoundException("Driver Hiccup", idDriHic));
        old.setHiccup(newH.getHiccup());
        old.setIdDriver(newH.getIdDriver());
        old.setStartDate(newH.getStartDate());
        old.setEndDate(newH.getEndDate());
        old.setReason(newH.getReason());
        return map.toDTO(hiccupRepo.save(old));
    }

    @Override
    public void deleteById(long id) {
        hiccupRepo.deleteById(id);
    }
}
