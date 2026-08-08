package com.speedtype.dto;

public class StatsResponse {

    private double averageWpm;
    private double bestWpm;
    private double averageAccuracy;
    private long totalTests;
    private Double bestWpmEasy;
    private Double bestWpmMedium;
    private Double bestWpmHard;

    public double getAverageWpm() {
        return averageWpm;
    }

    public void setAverageWpm(double averageWpm) {
        this.averageWpm = averageWpm;
    }

    public double getBestWpm() {
        return bestWpm;
    }

    public void setBestWpm(double bestWpm) {
        this.bestWpm = bestWpm;
    }

    public double getAverageAccuracy() {
        return averageAccuracy;
    }

    public void setAverageAccuracy(double averageAccuracy) {
        this.averageAccuracy = averageAccuracy;
    }

    public long getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(long totalTests) {
        this.totalTests = totalTests;
    }

    public Double getBestWpmEasy() {
        return bestWpmEasy;
    }

    public void setBestWpmEasy(Double bestWpmEasy) {
        this.bestWpmEasy = bestWpmEasy;
    }

    public Double getBestWpmMedium() {
        return bestWpmMedium;
    }

    public void setBestWpmMedium(Double bestWpmMedium) {
        this.bestWpmMedium = bestWpmMedium;
    }

    public Double getBestWpmHard() {
        return bestWpmHard;
    }

    public void setBestWpmHard(Double bestWpmHard) {
        this.bestWpmHard = bestWpmHard;
    }
}
