package dev.orisha.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CarAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCarAllPropertiesEquals(Car expected, Car actual) {
        assertCarAutoGeneratedPropertiesEquals(expected, actual);
        assertCarAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCarAllUpdatablePropertiesEquals(Car expected, Car actual) {
        assertCarUpdatableFieldsEquals(expected, actual);
        assertCarUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCarAutoGeneratedPropertiesEquals(Car expected, Car actual) {
        assertThat(expected)
            .as("Verify Car auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCarUpdatableFieldsEquals(Car expected, Car actual) {
        assertThat(expected)
            .as("Verify Car relevant properties")
            .satisfies(e -> assertThat(e.getMake()).as("check make").isEqualTo(actual.getMake()))
            .satisfies(e -> assertThat(e.getModel()).as("check model").isEqualTo(actual.getModel()))
            .satisfies(e -> assertThat(e.getPrice()).as("check price").isEqualTo(actual.getPrice()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCarUpdatableRelationshipsEquals(Car expected, Car actual) {}
}
