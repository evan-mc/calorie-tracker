package db.calorietracker.calorietrackerapp.repository;

import db.calorietracker.calorietrackerapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer>
{

}
