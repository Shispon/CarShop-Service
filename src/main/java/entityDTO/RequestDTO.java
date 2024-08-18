package entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import models.ServiceEnum;
import models.StatusEnum;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestDTO {
    private Integer id;
    private Integer userId;
    private Integer carId;
    private String carBrand;
    private String carModel;
    private String username;
    private ServiceEnum serviceType;
    private StatusEnum status;
}
