package com.team4.sensor;

public class DirtGeneratorSimple implements DirtGenerator {

    DirtGeneratorSimple() {
    }

    /**
     * Generates a fixed amount of dirt for all tiles.
     * @return
     */
    @Override
    public int generateDirt() {
        return 3;
    }
}
