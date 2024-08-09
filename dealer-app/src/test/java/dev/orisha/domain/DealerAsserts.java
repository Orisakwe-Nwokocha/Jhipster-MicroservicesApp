package dev.orisha.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DealerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealerAllPropertiesEquals(Dealer expected, Dealer actual) {
        assertDealerAutoGeneratedPropertiesEquals(expected, actual);
        assertDealerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealerAllUpdatablePropertiesEquals(Dealer expected, Dealer actual) {
        assertDealerUpdatableFieldsEquals(expected, actual);
        assertDealerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealerAutoGeneratedPropertiesEquals(Dealer expected, Dealer actual) {
        assertThat(expected)
            .as("Verify Dealer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealerUpdatableFieldsEquals(Dealer expected, Dealer actual) {
        assertThat(expected)
            .as("Verify Dealer relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealerUpdatableRelationshipsEquals(Dealer expected, Dealer actual) {}
}
