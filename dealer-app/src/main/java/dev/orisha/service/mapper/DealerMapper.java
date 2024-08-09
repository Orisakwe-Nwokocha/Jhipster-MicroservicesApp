package dev.orisha.service.mapper;

import dev.orisha.domain.Dealer;
import dev.orisha.service.dto.DealerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dealer} and its DTO {@link DealerDTO}.
 */
@Mapper(componentModel = "spring")
public interface DealerMapper extends EntityMapper<DealerDTO, Dealer> {}
