package com.team4.sensor;

import java.util.Random;

public class DirtGeneratorGaussian implements DirtGenerator {

    DirtGeneratorGaussian() {
    }

    /**
     * Generates dirt amounts randomly
     * @return
     */
    @Override
    public int generateDirt() {
        Random random = new Random();
        int randomGaussianInt;
        do {
            double value = random.nextGaussian() * 0.5 + 1;
            randomGaussianInt = (int) Math.round(value);
        } while (randomGaussianInt < 0);
        return randomGaussianInt;
    }
}
