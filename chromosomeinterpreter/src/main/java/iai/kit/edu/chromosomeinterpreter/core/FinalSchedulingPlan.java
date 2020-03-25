package iai.kit.edu.chromosomeinterpreter.core;

import java.util.List;

public class FinalSchedulingPlan {
    private int day;
    private int month;
    private int year;
    public int planID; // ChromosomeID
    public List<ResourcePlan> ResourcePlan;

    public List<ResourcePlan> getResourcePlan() {
        return ResourcePlan;
    }
    public void setResourcePlan(List<ResourcePlan> resourceplan) {
        ResourcePlan  = resourceplan;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPlanID() {
        return planID;
    }

    public void setPlanID(int planID) {
        this.planID = planID;
    }
}
