package dev.orisha.service;

import dev.orisha.service.dto.CarDTO;
import dev.orisha.service.dto.DealerDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service Interface for managing {@link dev.orisha.domain.Dealer}.
 */
public interface DealerService {
    /**
     * Save a dealer.
     *
     * @param dealerDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DealerDTO> save(DealerDTO dealerDTO);

    /**
     * Updates a dealer.
     *
     * @param dealerDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DealerDTO> update(DealerDTO dealerDTO);

    /**
     * Partially updates a dealer.
     *
     * @param dealerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DealerDTO> partialUpdate(DealerDTO dealerDTO);

    /**
     * Get all the dealers.
     *
     * @return the list of entities.
     */
    Flux<DealerDTO> findAll();

    /**
     * Returns the number of dealers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" dealer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DealerDTO> findOne(Long id);

    /**
     * Delete the "id" dealer.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    Mono<List<CarDTO>> getCarsFromCarsService();

    Mono<ResponseEntity<CarDTO>> getCarFromCarsService(Long id);

    Mono<String> sayHello(String name);
}
