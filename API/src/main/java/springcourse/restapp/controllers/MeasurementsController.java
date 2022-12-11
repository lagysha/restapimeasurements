package springcourse.restapp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springcourse.restapp.dto.measurement.MeasurementDTO;
import springcourse.restapp.dto.measurement.MeasurementResponse;
import springcourse.restapp.models.Measurement;
import springcourse.restapp.services.MeasurementsService;
import springcourse.restapp.util.response.MeasurementErrorResponse;
import springcourse.restapp.util.exception.MeasurementNotCreatedException;
import springcourse.restapp.util.validators.MeasurementValidator;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    public MeasurementsController(MeasurementsService measurementsService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){

        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);

        measurementValidator.validate(measurement, bindingResult);
        throwExceptionToClient(bindingResult);

        measurementsService.save(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public MeasurementResponse getAllMeasurements() {
        return new MeasurementResponse(measurementsService.findAll()
                .stream()
                .map(e -> modelMapper.map(e, MeasurementDTO.class)).toList());
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDays() {
        return measurementsService.findAll().stream().filter(Measurement::isRaining).count();
    }

    @ExceptionHandler
    public ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException e) {
        MeasurementErrorResponse measurementErrorResponse = new MeasurementErrorResponse(e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(measurementErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private static void throwExceptionToClient(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMassages = new StringBuilder();
            Stream.of(bindingResult.getFieldError()).forEach(e -> errorMassages.append(e.getField()).append(" - ")
                    .append(e.getDefaultMessage()).append(";"));
            throw new MeasurementNotCreatedException(errorMassages.toString());
        }
    }
}
