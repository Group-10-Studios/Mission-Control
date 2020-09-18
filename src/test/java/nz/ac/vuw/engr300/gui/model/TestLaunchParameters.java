package nz.ac.vuw.engr300.gui.model;

import nz.ac.vuw.engr300.model.LaunchParameters;

public class TestLaunchParameters extends LaunchParameters {
    public LaunchParameter<Double> testDouble = new LaunchParameter<>(1d, Double.class);
    public LaunchParameter<Integer> testInteger = new LaunchParameter<>(1, Integer.class);
    public LaunchParameter<String> testString = new LaunchParameter<>("", String.class);
    public LaunchParameter<Boolean> testBoolean = new LaunchParameter<>(false, Boolean.class);
}
