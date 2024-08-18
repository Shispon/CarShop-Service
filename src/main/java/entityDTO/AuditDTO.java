package entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDTO {
    private Integer id;
    private String action;
    private LocalDateTime timestamp;
    private Integer userId;
}
