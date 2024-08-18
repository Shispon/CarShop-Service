package mappers;

import entityDTO.UserDTO;
import models.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserModel userModel);

    UserModel toModel(UserDTO userDTO);
}
