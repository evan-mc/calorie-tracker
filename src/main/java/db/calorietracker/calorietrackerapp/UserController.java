package db.calorietracker.calorietrackerapp;

import db.calorietracker.calorietrackerapp.model.User;
import db.calorietracker.calorietrackerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class UserController
{
    @Autowired
    UserRepository userRepository;

    private final static Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable int id, Model model)
    {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            User user = userOptional.get();

            LOGGER.info("found user: " + user.getName());

            model.addAttribute("specificUser", user);

            double dailyCalories = CalorieCalculator.calculateDailyCalorieIntake(user);
            DecimalFormat df = new DecimalFormat("#.##");
            dailyCalories = Double.parseDouble(df.format(dailyCalories));

            model.addAttribute("dailyCalories", dailyCalories);

            return "userInfo";
        }
        else
        {
            LOGGER.info("no user found with id " + id);
            return "noUserFound";
        }
    }
}
