package springcourse.restapp.dto.measurement;

import springcourse.restapp.dto.SensorDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MeasurementDTO {

    @NotNull
    @Min(value = -100, message = "Value must be greater than -100")
    @Max(value = 100, message = "Value must be lower than 100")
    private Double value;
    @NotNull
    private Boolean raining;
    @NotNull
    private SensorDTO sensor;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}


