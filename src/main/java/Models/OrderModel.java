package Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderModel {
    private Integer addId;
    private String id;
    private String carId;
    private String userId;
    private String status;
    private Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public OrderModel() {
    }

    public OrderModel( String userId, String status) {
        this.addId = addId + 1;
        this.id = addId.toString();
        this.carId = carId;
        this.userId = userId;
        this.status = status;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "addId=" + addId +
                ", id='" + id + '\'' +
                ", carId='" + carId + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
