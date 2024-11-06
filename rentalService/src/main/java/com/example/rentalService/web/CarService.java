package com.example.rentalService.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;

import com.example.rentalService.data.Car;
import com.example.rentalService.data.Date;
import com.example.rentalService.data.Rental;

@RestController
public class CarService {
    private ArrayList<Car> cars = new ArrayList<>();
    
    public CarService() {
        cars.add(new Car("ABC-123", "Toyota", "Corolla", 2019, "Black", 100));
        cars.add(new Car("DEF-456", "Honda", "Civic", 2020, "White", 120));
        cars.add(new Car("GHI-789", "Ford", "Fiesta", 2018, "Red", 80));
    }

    @GetMapping("/")
    public String hello() {
        return """
        Welcome to the car rental app:<br>
        - You can rent a car by sending a PUT request to /cars/:plateNumber?rent=true with begin and end dates in the body.<br>
        - You can return a car by sending a PUT request to /cars/:plateNumber?rent=false.<br>
        - You can get a list of cars by sending a GET request to /cars.<br>
        - You can get a car by sending a GET request to /cars/:plateNumber.
        """;
    }

    @GetMapping("/cars")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<Car> getCars() {
        return cars;
    }

    @GetMapping("/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getCar(@PathVariable String plateNumber) {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber)) {
                return new ResponseEntity<>(car, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/cars/{plateNumber}")
    public ResponseEntity<?> rentCar(@PathVariable String plateNumber, @RequestParam(value="rent", required=true) boolean rent, @RequestBody(required=false) Date dates) {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber)) {
                if (rent) {
                    if (!car.isRented() && dates != null && dates.getBegin() != null && dates.getEnd() != null) {
                        car.addRental(new Rental(dates.getBegin(), dates.getEnd(), true));
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    if (car.isRented()) {
                        car.getActiveRental().setActive(false);
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}