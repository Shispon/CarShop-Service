import Menu.Display;
import Menu.MainMenu;
import Service.CarService;
import Service.OrderService;
import Service.RequestService;
import Service.UserService;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        CarService carService = new CarService();
        OrderService orderService = new OrderService();
        RequestService requestService = new RequestService();
        MainMenu mainMenu = new MainMenu(userService, carService, orderService,requestService);
        Display display = new Display(mainMenu,userService,orderService);

        display.displayInitialMenu();
    }
}