package service;

import models.RequestModel;
import repository.RequestRepository;

import java.util.List;

public class RequestService {
    private final RequestRepository requestRepository;
    private Integer id;
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
        id = 0;
    }
    public void addRequest(RequestModel request) {
        RequestModel newRequest = request.toBuilder()
                .id(id++)
                .build();
        requestRepository.create(newRequest);
    }
    public RequestModel getOrderById(Integer id) {
        return requestRepository.read(id);
    }
    public boolean updateRequest(RequestModel request) {
        return requestRepository.update(request) != null;
    }
    public boolean deleteRequestById(Integer id) {
        return requestRepository.delete(id);
    }
    public List<RequestModel> getAllRequest() {
        return requestRepository.findAll();
    }
}
