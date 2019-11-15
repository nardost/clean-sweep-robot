package com.team4.sensor;

import java.util.Random;

public class DirtGeneratorGaussian implements DirtGenerator {

    DirtGeneratorGaussian() {
    }

    /**
     * Generates dirt amounts randomly using the Gaussian (normal) distribution
     * Mean = 1.0
     *   SD = 0.5
     * The Javadoc says Mean ± 1 SD (1.0 ± 0.5) contain 68.2% of all values.
     * => 68% of the time the dirt unit is going to be 1.
     * @return
     */
    @Override
    public int generateDirt() {
        Random random = new Random();
        int randomGaussianInt;
        do {
            double value = random.nextGaussian() * 0.5 + 1;
            randomGaussianInt = (int) Math.round(value);
        } while (randomGaussianInt <= 0);
        return randomGaussianInt;
    }
}
