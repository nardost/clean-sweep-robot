package com.team4.sensor;

import com.team4.commons.Utilities;

public class DirtGeneratorSimple implements DirtGenerator {

    private int callCount;

    DirtGeneratorSimple() {
        setCallCount(0);
    }

    /**
     * Generates a fixed amount of dirt for tiles at all times.
     * @return
     */
    @Override
    public int generateDirt() {
        setCallCount(1 + getCallCount());
        int generatedDirt = 1;
        if(Utilities.isPrime(getCallCount())) {
            if(getCallCount() % 10 == 9) {
                //generatedDirt = 0;
            } else if(getCallCount() % 10 == 7) {
                generatedDirt = 3;
            } else {
                generatedDirt = 2;
            }
        }
        return generatedDirt;
    }

    private int getCallCount() {
        return callCount;
    }

    private void setCallCount(int callCount) {
        this.callCount = callCount;
    }
}
