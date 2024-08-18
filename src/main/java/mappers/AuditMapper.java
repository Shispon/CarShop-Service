package mappers;

import entityDTO.AuditDTO;
import models.AuditModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditDTO toDTO(AuditModel auditModel);

    AuditModel toModel(AuditDTO auditDTO);
}
