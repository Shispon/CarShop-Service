package Models;

public class RequestModel {

    private Integer addId;
    private String id;
    private String carId;
    private String userId;
    private String description;
    private String status;

    public RequestModel() {
    }

    public RequestModel( String carId, String userId, String description, String status) {
        this.addId = addId + 1;
        this.id = addId.toString();
        this.carId = carId;
        this.userId = userId;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
