package com.team4.robot;

import com.team4.commons.Direction;

public class NavigatorNull implements Navigator {
    @Override
    public Direction traverseFloor(Direction[] directions) {
        return null;
    }
}
