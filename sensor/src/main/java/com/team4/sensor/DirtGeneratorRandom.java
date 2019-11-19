package com.team4.sensor;

import java.util.Random;

public class DirtGeneratorRandom implements DirtGenerator {

    DirtGeneratorRandom() {
    }

    /**
     * Generates dirt amounts randomly
     * @return
     */
    @Override
    public int generateDirt() {
        int min = 0;
        int max = 3;
        Random random = new Random();
        return random.ints(min, max + 1).findFirst().getAsInt();
    }
}
