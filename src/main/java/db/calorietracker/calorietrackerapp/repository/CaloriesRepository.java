package db.calorietracker.calorietrackerapp.repository;

import db.calorietracker.calorietrackerapp.model.Calories;
import db.calorietracker.calorietrackerapp.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CaloriesRepository extends CrudRepository<Calories, Integer>
{
    List<Calories> findByUser(User user, Sort sort);
}
