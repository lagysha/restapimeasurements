package springcourse.restapp.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import springcourse.restapp.models.Measurement;
import springcourse.restapp.services.SensorsService;

@Component
public class MeasurementValidator implements Validator {
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Measurement.class.equals(aClass);
    }

    @Override
    public void validate(Object o, org.springframework.validation.Errors errors) {
        Measurement measurement = (Measurement) o;

        if (measurement.getSensor() == null) {
            return;
        }

        if (sensorsService.findByName(measurement.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "", "No such sensor");
        }
    }
}
