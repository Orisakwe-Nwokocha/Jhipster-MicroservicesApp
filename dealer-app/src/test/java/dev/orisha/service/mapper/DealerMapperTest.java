package dev.orisha.service.mapper;

import static dev.orisha.domain.DealerAsserts.*;
import static dev.orisha.domain.DealerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealerMapperTest {

    private DealerMapper dealerMapper;

    @BeforeEach
    void setUp() {
        dealerMapper = new DealerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDealerSample1();
        var actual = dealerMapper.toEntity(dealerMapper.toDto(expected));
        assertDealerAllPropertiesEquals(expected, actual);
    }
}
