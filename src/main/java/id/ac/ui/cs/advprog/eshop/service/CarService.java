package id.ac.ui.cs.advprog.eshop.service;
import id.ac.ui.cs.advprog.eshop.model.Car;
import java.util.List;

public interface CarService extends Service<Car>{
    public Car create(Car car);
    public List<Car> findAll();
    public Car findById(String carId);
    public Car edit(Car car);
    public void delete(String carId);
}
