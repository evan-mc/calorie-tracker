package db.calorietracker.calorietrackerapp;

import db.calorietracker.calorietrackerapp.model.Calories;
import db.calorietracker.calorietrackerapp.model.User;
import db.calorietracker.calorietrackerapp.repository.CaloriesRepository;
import db.calorietracker.calorietrackerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class UserController
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    CaloriesRepository caloriesRepository;

    private final static Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable int id, Model model)
    {
        return findUserInfo(id, model);
    }

    @PostMapping("/user/{id}/AddItem")
    public String addItem(@PathVariable int id, @RequestBody MultiValueMap<String, String> params, Model model)
    {
        Calories calories = new Calories(params, userRepository.findById(id).get());
        caloriesRepository.save(calories);
        return getUserInfo(id, model);
    }

    @PostMapping("/user/{id}/RemoveItem/{itemId}")
    public String removeItem(@PathVariable int id, @PathVariable int itemId, Model model)
    {
        caloriesRepository.deleteById(itemId);

        return getUserInfo(id, model);
    }

    @GetMapping("/user/{id}/EditItem/{itemId}")
    public String editItem(@PathVariable int id, @PathVariable int itemId, Model model)
    {
        Optional<Calories> calorieItem = caloriesRepository.findById(itemId);
        if(calorieItem.isPresent())
        {
            model.addAttribute("specificUser", userRepository.findById(id).get());
            model.addAttribute("calorieItem" , calorieItem.get());
            return "editItem";
        }
        else
        {
            model.addAttribute("missingItemId", itemId);
            return "noItemFound";
        }
    }

    @PostMapping("/user/{id}/EditItem/{itemId}")
    public String editItem(@PathVariable int id, @PathVariable int itemId, @RequestBody MultiValueMap<String, String> params, Model model)
    {
        Calories calories = new Calories(itemId, params, userRepository.findById(id).get());
        caloriesRepository.save(calories);

        return getUserInfo(id, model);
    }

    private String findUserInfo(int id, Model model)
    {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            LOGGER.info("found user: " + user.getName());
            model.addAttribute("specificUser", user);

            List<Calories> caloriesList = caloriesRepository
                    .findByUser(user, Sort.by(Sort.Direction.ASC, "calorieCount"));
            model.addAttribute("userCalorieList", caloriesList);

            double dailyCalories = CalorieCalculator.calculateDailyCalorieIntake(user);
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
