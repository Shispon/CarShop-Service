package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestModel {
    private final Integer id;
    private final String carBrand;
    private final String carModel;
    private final String username;
    private final ServiceEnum serviceType;
    private final StatusEnum status;

    public RequestModel(String carBrand, String carModel, String username, ServiceEnum serviceType, StatusEnum status) {
        this.id = -1;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.username = username;
        this.serviceType = serviceType;
        this.status = status;
    }
}
