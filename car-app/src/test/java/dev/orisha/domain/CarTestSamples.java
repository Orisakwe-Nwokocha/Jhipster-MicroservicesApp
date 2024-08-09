package dev.orisha.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Car getCarSample1() {
        return new Car().id(1L).make("make1").model("model1");
    }

    public static Car getCarSample2() {
        return new Car().id(2L).make("make2").model("model2");
    }

    public static Car getCarRandomSampleGenerator() {
        return new Car().id(longCount.incrementAndGet()).make(UUID.randomUUID().toString()).model(UUID.randomUUID().toString());
    }
}
