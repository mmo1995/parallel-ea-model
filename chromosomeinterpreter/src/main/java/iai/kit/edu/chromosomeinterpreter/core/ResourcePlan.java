package iai.kit.edu.chromosomeinterpreter.core;

import java.util.List;

public class ResourcePlan {
    public int resourceID; // GenID = resourceID
    public float[] powerGeneration;


    public Integer getresourceID() {

        return resourceID;
    }
    public void setresourceID(int resourceID) {
        this.resourceID = resourceID;
    }
    public float[] getPowerFraction() {

        return powerGeneration;
    }
    public void setPowerFraction(float[] powerfraction) {

        this.powerGeneration = powerfraction;
    }


}
