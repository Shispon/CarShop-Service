package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarModel {
    private final Integer id;
    private final String brand;
    private final String model;
    private final Integer year;
    private final Double price;
    private final String condition;

    public CarModel( String brand, String model, Integer year, Double price, String condition) {
        this.id = -1;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }
}
