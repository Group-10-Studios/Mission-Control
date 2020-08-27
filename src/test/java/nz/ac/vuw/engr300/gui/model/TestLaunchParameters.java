package nz.ac.vuw.engr300.gui.model;

import nz.ac.vuw.engr300.model.LaunchParameters;

public class TestLaunchParameters extends LaunchParameters {
    protected LaunchParameter<Double> testDouble = new LaunchParameter<>(1d, Double.class);
    protected LaunchParameter<Integer> testInteger = new LaunchParameter<>(1, Integer.class);
    protected LaunchParameter<String> testString = new LaunchParameter<>("", String.class);
    protected LaunchParameter<Boolean> testBoolean = new LaunchParameter<>(false, Boolean.class);
}
