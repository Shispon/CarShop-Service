package repository;

import models.OrderModel;
import models.StatusEnum;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements CrudRepository<OrderModel,Integer>{
    private final List<OrderModel> orderList;
    private Integer id = 0;

    public OrderRepository() {
        orderList = new ArrayList<>();
    }

    @Override
    public OrderModel create(OrderModel order) {
        OrderModel newOrder = order.toBuilder()
                .id(++id)
                .build();
        orderList.add(newOrder);
        return newOrder;
    }

    public OrderModel read(Integer id) {
        return orderList.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public OrderModel update(OrderModel order) {
       OrderModel existOrder = read(order.getId());
       if (existOrder != null) {
           OrderModel updateOrder = existOrder.toBuilder()
                   .carId(existOrder.getCarId())
                   .userId(existOrder.getUserId())
                   .status(existOrder.getStatus())
                   .build();
           orderList.set(orderList.indexOf(existOrder), updateOrder);
           return updateOrder;
       }
       return null;
    }

    public boolean delete(Integer id) {
        return orderList.removeIf(order -> order.getId().equals(id));
    }

    public List<OrderModel> findAll() {
        return orderList;
    }

    public List<OrderModel> findOrderByUserIdAndCarId(Integer userId, Integer carId) {
        List<OrderModel> resultList = new ArrayList<>();
        orderList.stream().filter(order -> order.getUserId().equals(userId)
                                        &&
                                            order.getCarId().equals(carId))
                            .forEach(resultList::add);
        return resultList;
    }

}
