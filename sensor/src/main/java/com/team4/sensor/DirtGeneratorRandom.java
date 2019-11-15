package com.team4.sensor;

import java.util.Random;

public class DirtGeneratorRandom implements DirtGenerator {

    DirtGeneratorRandom() {
    }

    @Override
    public int generateDirt() {
        int minInclusive = 0;
        int maxExclusive = 4;
        Random random = new Random();
        return random.ints(minInclusive, maxExclusive).findFirst().getAsInt();
    }
}
