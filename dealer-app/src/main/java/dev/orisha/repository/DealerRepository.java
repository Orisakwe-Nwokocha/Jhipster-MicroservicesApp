package dev.orisha.repository;

import dev.orisha.domain.Dealer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Dealer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealerRepository extends ReactiveCrudRepository<Dealer, Long>, DealerRepositoryInternal {
    @Override
    <S extends Dealer> Mono<S> save(S entity);

    @Override
    Flux<Dealer> findAll();

    @Override
    Mono<Dealer> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DealerRepositoryInternal {
    <S extends Dealer> Mono<S> save(S entity);

    Flux<Dealer> findAllBy(Pageable pageable);

    Flux<Dealer> findAll();

    Mono<Dealer> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Dealer> findAllBy(Pageable pageable, Criteria criteria);
}
