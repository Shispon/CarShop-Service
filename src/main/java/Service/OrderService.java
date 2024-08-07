package Service;

import Models.OrderModel;

import java.util.*;
import java.util.stream.Collectors;

public class OrderService {

    private Map<String, OrderModel> orders = new HashMap<>();

    public void addOrder(OrderModel order) {
        orders.put(order.getId(), order);
    }

    public OrderModel getOrderById(String id) {
        return orders.get(id);
    }
    public OrderModel getOrderByStatus (String status) {
        for(OrderModel o : orders.values()) {
            if(o.getStatus().equals(status))
                return o;
        }
        System.out.println("there is no such thing");
        return null;
    }

    public OrderModel getOrderByClientId(String userId) {
        for(OrderModel o : orders.values()) {
            if(o.getUserId().equals(userId))
                return o;
        }
        System.out.println("there is no such thing");
        return null;
    }

    public OrderModel getOrderByDate (Date date) {
        for(OrderModel o : orders.values()) {
            if(o.getDate().equals(date))
                return o;
        }
        System.out.println("there is no such thing");
        return null;
    }

    public OrderModel getOrderByCarId ( String carId) {
        for(OrderModel o : orders.values()) {
            if(o.getCarId().equals(carId))
                return o;
        }
        System.out.println("there is no such thing");
        return null;
    }

    public List<OrderModel> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public List<OrderModel> searchOrders(String userId, String carId) {
        return orders.values().stream()
                .filter(order -> (userId == null || order.getUserId().equals(userId)) &&
                        (carId == null || order.getCarId().equals(carId)))
                .collect(Collectors.toList());
    }

    public boolean isCarOrdered(String carId) {
        return orders.values().stream().anyMatch(order -> order.getCarId().equals(carId));
    }

    public void updateOrder(OrderModel order) {
        orders.put(order.getId(), order);
    }

    public void deleteOrder(String id) {
        orders.remove(id);
    }
}
