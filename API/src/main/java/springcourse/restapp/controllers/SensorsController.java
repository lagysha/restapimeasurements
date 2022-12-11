package springcourse.restapp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springcourse.restapp.dto.SensorDTO;
import springcourse.restapp.models.Sensor;
import springcourse.restapp.services.SensorsService;
import springcourse.restapp.util.response.SensorErrorResponse;
import springcourse.restapp.util.exception.SensorNotCreatedException;
import springcourse.restapp.util.validators.SensorValidator;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorsService sensorsService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registry(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);

        sensorValidator.validate(sensor, bindingResult);
        throwExceptionToClient(bindingResult);

        sensorsService.registry(sensor);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse sensorErrorResponse = new SensorErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(sensorErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private static void throwExceptionToClient(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMassages = new StringBuilder();
            Stream.of(bindingResult.getFieldError()).forEach(e -> errorMassages.append(e.getField()).append(" - ")
                    .append(e.getDefaultMessage()).append(";"));
            throw new SensorNotCreatedException(errorMassages.toString());
        }
    }
}
