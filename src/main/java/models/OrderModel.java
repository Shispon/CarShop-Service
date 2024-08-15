package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder=true)
public class OrderModel {
    private Integer id;
    private Integer carId;
    private Integer userId;
    private StatusEnum status;
    public OrderModel(Integer carId, Integer userId, StatusEnum status) {
        this.id = -1;
        this.carId = carId;
        this.userId = userId;
        this.status = status;
    }
}
