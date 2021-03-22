package etti.agrosense.Models;

public class MeasurementData {

    private float value;
    private int id;
    private long timestamp;
    private String measure_unit;

    public MeasurementData(float value, long timestamp, int id, String measure_unit) {
        this.value = value;
        this.timestamp = timestamp;
        this.id = id;
        this.measure_unit = measure_unit;
    }

    public float getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public String getMeasure_unit() {
        return measure_unit;
    }
}