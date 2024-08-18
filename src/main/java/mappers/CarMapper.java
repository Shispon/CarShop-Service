package mappers;

import entityDTO.CarDTO;
import models.CarModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDTO toDTO(CarModel carModel);

    CarModel toModel(CarDTO carDTO);
}
