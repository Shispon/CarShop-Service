package entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarDTO {
    private Integer id;
    private String brand;
    private String model;
    private Integer year;
    private Double price;
    private String condition;
}
