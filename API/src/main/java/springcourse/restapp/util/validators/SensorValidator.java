package springcourse.restapp.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springcourse.restapp.models.Sensor;
import springcourse.restapp.repositories.SensorsRepository;

@Component
public class SensorValidator implements Validator {

    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorValidator(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Sensor.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Sensor sensor = (Sensor) o;

        if (sensorsRepository.findByName(sensor.getName()).isPresent()){
            errors.rejectValue("name","","Sensor already exist");
        }

    }
}
