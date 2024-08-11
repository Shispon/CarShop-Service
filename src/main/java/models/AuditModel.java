package models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuditModel {
    private Integer id;
    private String username;
    private String action;
    private LocalDateTime timestamp;
    private Integer userId;
}
