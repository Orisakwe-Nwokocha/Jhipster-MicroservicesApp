package dev.orisha.web.rest;

import static dev.orisha.domain.DealerAsserts.*;
import static dev.orisha.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.orisha.IntegrationTest;
import dev.orisha.domain.Dealer;
import dev.orisha.repository.DealerRepository;
import dev.orisha.repository.EntityManager;
import dev.orisha.service.dto.DealerDTO;
import dev.orisha.service.mapper.DealerMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link DealerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DealerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dealers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private DealerMapper dealerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Dealer dealer;

    private Dealer insertedDealer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createEntity(EntityManager em) {
        Dealer dealer = new Dealer().name(DEFAULT_NAME).address(DEFAULT_ADDRESS);
        return dealer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createUpdatedEntity(EntityManager em) {
        Dealer dealer = new Dealer().name(UPDATED_NAME).address(UPDATED_ADDRESS);
        return dealer;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Dealer.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        dealer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDealer != null) {
            dealerRepository.delete(insertedDealer).block();
            insertedDealer = null;
        }
        deleteEntities(em);
    }

    @Test
    void createDealer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);
        var returnedDealerDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(DealerDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Dealer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDealer = dealerMapper.toEntity(returnedDealerDTO);
        assertDealerUpdatableFieldsEquals(returnedDealer, getPersistedDealer(returnedDealer));

        insertedDealer = returnedDealer;
    }

    @Test
    void createDealerWithExistingId() throws Exception {
        // Create the Dealer with an existing ID
        dealer.setId(1L);
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dealer.setName(null);

        // Create the Dealer, which fails.
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllDealersAsStream() {
        // Initialize the database
        dealerRepository.save(dealer).block();

        List<Dealer> dealerList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DealerDTO.class)
            .getResponseBody()
            .map(dealerMapper::toEntity)
            .filter(dealer::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(dealerList).isNotNull();
        assertThat(dealerList).hasSize(1);
        Dealer testDealer = dealerList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertDealerAllPropertiesEquals(dealer, testDealer);
        assertDealerUpdatableFieldsEquals(dealer, testDealer);
    }

    @Test
    void getAllDealers() {
        // Initialize the database
        insertedDealer = dealerRepository.save(dealer).block();

        // Get all the dealerList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(dealer.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS));
    }

    @Test
    void getDealer() {
        // Initialize the database
        insertedDealer = dealerRepository.save(dealer).block();

        // Get the dealer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dealer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dealer.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS));
    }

    @Test
    void getNonExistingDealer() {
        // Get the dealer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.save(dealer).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer
        Dealer updatedDealer = dealerRepository.findById(dealer.getId()).block();
        updatedDealer.name(UPDATED_NAME).address(UPDATED_ADDRESS);
        DealerDTO dealerDTO = dealerMapper.toDto(updatedDealer);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dealerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDealerToMatchAllProperties(updatedDealer);
    }

    @Test
    void putNonExistingDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dealerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.save(dealer).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDealer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dealer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDealer, dealer), getPersistedDealer(dealer));
    }

    @Test
    void fullUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.save(dealer).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer.name(UPDATED_NAME).address(UPDATED_ADDRESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDealer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dealer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealerUpdatableFieldsEquals(partialUpdatedDealer, getPersistedDealer(partialUpdatedDealer));
    }

    @Test
    void patchNonExistingDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dealerDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(dealerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDealer() {
        // Initialize the database
        insertedDealer = dealerRepository.save(dealer).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dealer
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dealer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dealerRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Dealer getPersistedDealer(Dealer dealer) {
        return dealerRepository.findById(dealer.getId()).block();
    }

    protected void assertPersistedDealerToMatchAllProperties(Dealer expectedDealer) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDealerAllPropertiesEquals(expectedDealer, getPersistedDealer(expectedDealer));
        assertDealerUpdatableFieldsEquals(expectedDealer, getPersistedDealer(expectedDealer));
    }

    protected void assertPersistedDealerToMatchUpdatableProperties(Dealer expectedDealer) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDealerAllUpdatablePropertiesEquals(expectedDealer, getPersistedDealer(expectedDealer));
        assertDealerUpdatableFieldsEquals(expectedDealer, getPersistedDealer(expectedDealer));
    }
}
