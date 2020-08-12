package nz.ac.vuw.engr300.gui.controllers;
import nz.ac.vuw.engr300.model.LaunchParameters;

public abstract class RocketController {
    protected LaunchParameters launchParameters;

    public void setLaunchParameters(LaunchParameters launchParameters) {
        this.launchParameters = launchParameters;
    }

}
