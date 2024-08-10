package repository;

import models.RequestModel;
import models.StatusEnum;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository implements CrudRepository<RequestModel,Integer> {
    private final List<RequestModel> requestList = new ArrayList<>();
    private Integer id = 0;

    @Override
    public RequestModel create(RequestModel request) {
        RequestModel newRequest = request.toBuilder()
                .id(++id)
                .build();
        requestList.add(newRequest);
        return newRequest;
    }

    @Override
    public RequestModel read(Integer id) {
        return requestList.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public RequestModel update(RequestModel requset) {
        RequestModel existRequset = read(requset.getId());
        if (existRequset != null) {
            RequestModel updateRequest = requset.toBuilder()
                    .carBrand(requset.getCarBrand())
                    .carModel(requset.getCarModel())
                    .username(requset.getUsername())
                    .serviceType(requset.getServiceType())
                    .status(requset.getStatus())
                    .build();
            requestList.set(requestList.indexOf(existRequset), updateRequest);
            return updateRequest;
        }
        return null;
    }

    @Override
    public boolean delete(Integer integer) {
        return requestList.removeIf(r -> r.getId().equals(integer));
    }

    @Override
    public List<RequestModel> findAll() {
        return requestList;
    }
}
