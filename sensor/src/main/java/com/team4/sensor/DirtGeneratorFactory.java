package com.team4.sensor;

import com.team4.commons.ConfigManager;
import com.team4.commons.RobotException;

public class DirtGeneratorFactory {

    private DirtGeneratorFactory() {
    }

    static DirtGenerator createDirtGenerator() {
        String dirtGeneratorType = ConfigManager.getConfiguration("dirtGeneratorType");
        switch (dirtGeneratorType) {
            case "random": return new DirtGeneratorRandom();
            case "simple": return new DirtGeneratorSimple();
            case "gaussian": return new DirtGeneratorGaussian();
            default: throw new RobotException("Invalid dirt generator type: " + dirtGeneratorType);
        }
    }
}
