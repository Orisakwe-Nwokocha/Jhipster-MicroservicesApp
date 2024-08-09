package dev.orisha.service.impl;

import dev.orisha.repository.DealerRepository;
import dev.orisha.service.CarClient;
import dev.orisha.service.DealerService;
import dev.orisha.service.dto.CarDTO;
import dev.orisha.service.dto.DealerDTO;
import dev.orisha.service.mapper.DealerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service Implementation for managing {@link dev.orisha.domain.Dealer}.
 */
@Service
@Transactional
public class DealerServiceImpl implements DealerService {

    private static final Logger log = LoggerFactory.getLogger(DealerServiceImpl.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    private final CarClient carClient;

    public DealerServiceImpl(DealerRepository dealerRepository, DealerMapper dealerMapper, CarClient carClient) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
        this.carClient = carClient;
    }

    @Override
    public Mono<DealerDTO> save(DealerDTO dealerDTO) {
        log.debug("Request to save Dealer : {}", dealerDTO);
        return dealerRepository.save(dealerMapper.toEntity(dealerDTO)).map(dealerMapper::toDto);
    }

    @Override
    public Mono<DealerDTO> update(DealerDTO dealerDTO) {
        log.debug("Request to update Dealer : {}", dealerDTO);
        return dealerRepository.save(dealerMapper.toEntity(dealerDTO)).map(dealerMapper::toDto);
    }

    @Override
    public Mono<DealerDTO> partialUpdate(DealerDTO dealerDTO) {
        log.debug("Request to partially update Dealer : {}", dealerDTO);

        return dealerRepository
            .findById(dealerDTO.getId())
            .map(existingDealer -> {
                dealerMapper.partialUpdate(existingDealer, dealerDTO);

                return existingDealer;
            })
            .flatMap(dealerRepository::save)
            .map(dealerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DealerDTO> findAll() {
        log.debug("Request to get all Dealers");
        return dealerRepository.findAll().map(dealerMapper::toDto);
    }

    public Mono<Long> countAll() {
        return dealerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DealerDTO> findOne(Long id) {
        log.debug("Request to get Dealer : {}", id);
        return dealerRepository.findById(id).map(dealerMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Dealer : {}", id);
        return dealerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<List<CarDTO>> getCarsFromCarsService() {
        log.debug("Request to get Cars from Cars Service");
        return carClient.getAllCars();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ResponseEntity<CarDTO>> getCarFromCarsService(Long id) {
        log.debug("Request to get Car from Cars Service : {}", id);
        return carClient.getCarById(id);
    }

    @Override
    public Mono<String> sayHello(String name) {
        return Mono.just("Hello " + name);
    }
}
