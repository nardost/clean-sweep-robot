package com.team4.commons;
package com.team4.robot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(Parameterized.class)
public class PowerManagerTest {

    private int initBattery;

    public PowerManagerTest(int _initBattery) {
        this.initBattery = _initBattery;
    }

    @Test
    public void StartBattery() {
    	PowerManager pm = new PowerUnit();
        assertEquals(pm.GetBattery(), initBattery);
    }
    
    @Test
    public void DeductBattery() {
    	PowerManager pm = new PowerUnit();
    	pm.DeductBattery(1);
        assertEquals(pm.GetBattery(), this.initBattery - 1);
        pm.DeductBattery(2);
        assertEquals(pm.GetBattery(), this.initBattery - 1 - 2);
        pm.DeductBattery(3);
        assertEquals(pm.GetBattery(), this.initBattery - 1 - 2 - 3);
    }
    
    @Test
    public void DepleteBattery() {
    	PowerManager pm = new PowerUnit();
    	pm.DeductBattery(this.initBattery + 1);
        assertEquals(pm.GetBattery(), 0);
    }
    
    @Test
    public void RechargeBattery() {
    	PowerManager pm = new PowerUnit();
    	pm.DeductBattery(this.initBattery + 1);
        pm.Recharge();
        assertEquals(pm.GetBattery(), this.initBattery);
    }
}
