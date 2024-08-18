package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.CarModel;
import repository.CarRepository;
import service.CarService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/cars/*")
public class CarController extends HttpServlet {

    private CarService carService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        carService = new CarService(new CarRepository()); // Инициализация сервиса
        objectMapper = new ObjectMapper(); // Инициализация ObjectMapper
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String year = request.getParameter("year");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        String condition = request.getParameter("condition");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Проверка на наличие параметров поиска
            List<CarModel> cars = null;
            if (brand != null) {
                cars = carService.searchCarsByBrand(brand);
            } else if (model != null) {
                cars = carService.searchCarsByModel(model);
            } else if (year != null) {
                cars = carService.searchCarsByYear(Integer.parseInt(year));
            } else if (minPrice != null && maxPrice != null) {
                cars = carService.searchCarsByPrice(Double.parseDouble(minPrice), Double.parseDouble(maxPrice));
            } else if (condition != null) {
                cars = carService.searchCarsByCondition(condition);
            } else {
                cars = carService.getAllCars();
            }

            if (cars != null && !cars.isEmpty()) {
                response.getWriter().write(convertToJson(cars));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No cars found");
            }
        } else {
            // Получение автомобиля по ID
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                Integer id = Integer.parseInt(pathParts[1]);
                CarModel car = carService.getCarById(id);
                if (car != null) {
                    response.getWriter().write(convertToJson(car));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarModel car = parseRequestBody(request, CarModel.class);
        carService.addCar(car);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(convertToJson(car));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            Integer id = Integer.parseInt(pathParts[1]);
            CarModel updatedCar = parseRequestBody(request, CarModel.class);
            updatedCar = updatedCar.toBuilder().id(id).build();

            if (carService.updateCar(updatedCar)) {
                response.getWriter().write(convertToJson(updatedCar));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            Integer id = Integer.parseInt(pathParts[1]);
            if (carService.deleteCar(id)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
        }
    }

    // Метод для парсинга JSON из тела запроса
    private <T> T parseRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        return objectMapper.readValue(request.getInputStream(), clazz);
    }

    // Метод для преобразования объекта в JSON
    private String convertToJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
