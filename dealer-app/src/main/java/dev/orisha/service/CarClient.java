package dev.orisha.service;

import dev.orisha.service.dto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.util.List;

@FeignClient(name = "cars")
public interface CarClient {

    @GetMapping("/api/cars")
    Mono<List<CarDTO>> getAllCars();

    @GetMapping("/api/cars/{id}")
    Mono<ResponseEntity<CarDTO>> getCarById(@PathVariable Long id);
}
