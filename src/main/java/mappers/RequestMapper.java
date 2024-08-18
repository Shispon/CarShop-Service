package mappers;

import entityDTO.RequestDTO;
import models.RequestModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestDTO toDTO(RequestModel requestModel);

    RequestModel toModel(RequestDTO requestDTO);
}
