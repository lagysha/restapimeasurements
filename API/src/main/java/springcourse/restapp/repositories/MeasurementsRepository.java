package springcourse.restapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springcourse.restapp.models.Measurement;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement,Integer> {
}
