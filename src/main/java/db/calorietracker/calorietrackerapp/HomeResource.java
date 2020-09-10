package db.calorietracker.calorietrackerapp;

import db.calorietracker.calorietrackerapp.model.User;
import db.calorietracker.calorietrackerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeResource
{
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String getAllUsers(Model model)
    {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        model.addAttribute("users", users);

        //System.out.println("calories: " + CalorieCalculator.calculateDailyCalorieIntake(users.get(0)));
        return "homepage";
    }

    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable int id, Model model)
    {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            model.addAttribute("specificUser", userOptional.get());
            return "userInfo";
        }
        else
        {
            return "NoUserFound";
        }
    }
}
