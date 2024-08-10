package models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuditModel {
    private final Integer id;
    private final String username;
    private final String action;
    private final LocalDateTime timestamp;
}
