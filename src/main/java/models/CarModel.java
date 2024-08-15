package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarModel {
    private Integer id;
    private String brand;
    private String model;
    private Integer year;
    private Double price;
    private String condition;

    public CarModel( String brand, String model, Integer year, Double price, String condition) {
        this.id = -1;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }
}
