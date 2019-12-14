package iai.kit.edu.chromosomeinterpreter.core;

import java.util.List;

public class SchedulingPlan {
    public int planID; // ChromosomeID
    public int childID;

    public List<ResourcePlan> resourcePlan;

    public Integer getplanID() {
        return planID;
    }
    public void setplanID(int planID) {
        this.planID = planID;
    }

    public List<ResourcePlan> getResourcePlan() {
        return resourcePlan;
    }
    public void setResourcePlan(List<ResourcePlan> resourceplan) {
        resourcePlan = resourceplan;
    }
    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }

}
