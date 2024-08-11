package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestModel {
    private Integer id;
    private Integer userId;
    private Integer carId;
    private String carBrand;
    private String carModel;
    private String username;
    private ServiceEnum serviceType;
    private StatusEnum status;

    public RequestModel(String carBrand, String carModel, String username, ServiceEnum serviceType, StatusEnum status) {
        this.id = -1;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.username = username;
        this.serviceType = serviceType;
        this.status = status;
    }
}
