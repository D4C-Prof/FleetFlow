package lockin.fleetflow.drivers.service;



import lockin.fleetflow.drivers.domain.DriverHiccup;
import lockin.fleetflow.drivers.dto.HiccupDTO;

import java.util.List;

public interface IHiccupService
{
    HiccupDTO create(HiccupDTO hiccupDTO);
    HiccupDTO findById(Long idDriHic);
    List<HiccupDTO> findAll();
    HiccupDTO update(long idAssHis, DriverHiccup newH);
    void deleteById(long id);
}
