package de.kit.sensorlibrary.sensor.ambientnoisesensor;

import java.util.List;

/**
 * Created by Robert on 06.02.2015.
 */
public class AmbientNoiseMeasurement {
    private double averageNoise;
    private double maxNoise;
    private double minNoise;
    private List<Double> measurementList;

    public AmbientNoiseMeasurement(List<Double> measurementList) {
        this.measurementList = measurementList;
        setValuesFromMeasurementList(measurementList);

    }

    public double getAverageNoise() {
        return averageNoise;
    }

    public double getMaxNoise() {
        return maxNoise;
    }

    public double getMinNoise() {
        return minNoise;
    }

    public List<Double> getMeasurementList() {
        return measurementList;
    }

    private void setValuesFromMeasurementList(List<Double> measures) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        for (Double measure : measures) {
            sum += measure;
            min = (min < measure) ? min : measure;
            max = (max > measure) ? max : measure;
        }
        this.averageNoise = sum / measures.size();
        this.maxNoise = max;
        this.minNoise = min;
    }
}
