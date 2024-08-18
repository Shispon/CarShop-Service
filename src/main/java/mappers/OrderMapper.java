package mappers;

import entityDTO.OrderDTO;
import models.OrderModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(OrderModel orderModel);

    OrderModel toModel(OrderDTO carDTO);
}
