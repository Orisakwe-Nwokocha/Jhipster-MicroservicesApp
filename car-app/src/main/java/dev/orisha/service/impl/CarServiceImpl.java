package dev.orisha.service.impl;

import dev.orisha.repository.CarRepository;
import dev.orisha.service.CarService;
import dev.orisha.service.dto.CarDTO;
import dev.orisha.service.mapper.CarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link dev.orisha.domain.Car}.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private static final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    @Override
    public Mono<CarDTO> save(CarDTO carDTO) {
        log.debug("Request to save Car : {}", carDTO);
        return carRepository.save(carMapper.toEntity(carDTO)).map(carMapper::toDto);
    }

    @Override
    public Mono<CarDTO> update(CarDTO carDTO) {
        log.debug("Request to update Car : {}", carDTO);
        return carRepository.save(carMapper.toEntity(carDTO)).map(carMapper::toDto);
    }

    @Override
    public Mono<CarDTO> partialUpdate(CarDTO carDTO) {
        log.debug("Request to partially update Car : {}", carDTO);

        return carRepository
            .findById(carDTO.getId())
            .map(existingCar -> {
                carMapper.partialUpdate(existingCar, carDTO);

                return existingCar;
            })
            .flatMap(carRepository::save)
            .map(carMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CarDTO> findAll() {
        log.debug("Request to get all Cars");
        return carRepository.findAll().map(carMapper::toDto);
    }

    public Mono<Long> countAll() {
        return carRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CarDTO> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id).map(carMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        return carRepository.deleteById(id);
    }
}
