package com.team4.robot;

import com.team4.commons.ConfigManager;

public class NavigatorFactory {

    private NavigatorFactory() {
    }

    static Navigator createNavigator() {
        switch(ConfigManager.getConfiguration("navigator")) {
            case "null":
                return new NavigatorNull();
            case "alpha":
                return new NavigatorAlpha();
            case "beta":
            case "omega":
            default:
                return new NavigatorOmega();
        }
    }

}
