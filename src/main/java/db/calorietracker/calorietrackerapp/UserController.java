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
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static String lastDateAccessed = "today";

    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable int id, Model model, @RequestParam("date") Optional<String> dateParam) throws ParseException
    {
        String date;
        if(dateParam.isPresent())
        {
            lastDateAccessed = dateParam.get();
        }
        else
        {
            lastDateAccessed = "today";
        }

        return findUserInfo(id, model, lastDateAccessed);
    }

    @PostMapping("/user/{id}/AddItem")
    public String addItem(@PathVariable int id, @RequestBody MultiValueMap<String, String> params, Model model) throws ParseException
    {
        java.sql.Date sqlDate = getDate(lastDateAccessed);
        Calories calories = new Calories(params, userRepository.findById(id).get(), sqlDate);
        caloriesRepository.save(calories);

        return "redirect:/user/{id}";
    }

    @PostMapping("/user/{id}/RemoveItem/{itemId}")
    public String removeItem(@PathVariable int id, @PathVariable int itemId, Model model)
    {
        caloriesRepository.deleteById(itemId);

        return "redirect:/user/{id}";
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
        java.sql.Date date = caloriesRepository.findById(itemId).get().getDate();
        Calories calories = new Calories(itemId, params, userRepository.findById(id).get(), date);
        caloriesRepository.save(calories);

        return "redirect:/user/{id}";
    }

    @GetMapping("/user/{id}/EditUser")
    public String editUser(@PathVariable int id, Model model)
    {
        User user = userRepository.findById(id).get();

        model.addAttribute("specificUser", user);

        return "editUser";
    }

    @PostMapping("/user/{id}/EditUser")
    public String editUser(@PathVariable int id, @RequestBody MultiValueMap<String, String> params, Model model)
    {
        User user = new User(params, id);

        userRepository.save(user);

        return "redirect:/user/{id}";
    }

    private String findUserInfo(int id, Model model, String date) throws ParseException
    {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            LOGGER.info("found user: " + user.getName());
            model.addAttribute("specificUser", user);

            java.sql.Date sqlDate = getDate(date);

            List<Calories> caloriesList = caloriesRepository
                    .findByUserAndDate(user, sqlDate, Sort.by(Sort.Direction.ASC, "calorieCount"));
            caloriesList.forEach(System.out::println);
            model.addAttribute("userCalorieList", caloriesList);

            //count up total amount of calories
            int totalCaloriesToday = 0;
            for(int i = 0, cSize = caloriesList.size(); i < cSize; ++i)
            {
                totalCaloriesToday += caloriesList.get(i).getCalorieCount();
            }

            model.addAttribute("calorieCount", totalCaloriesToday);

            int dailyCalories = (int)CalorieCalculator.calculateDailyCalorieIntake(user);
            model.addAttribute("dailyCalories", dailyCalories);

            int caloriesLeft;
            if(dailyCalories > totalCaloriesToday)
            {
                caloriesLeft = dailyCalories - totalCaloriesToday;
            }
            else
            {
                caloriesLeft = 0;
            }
            model.addAttribute("caloriesLeft", caloriesLeft);

            return "userInfo";
        }
        else
        {
            LOGGER.info("no user found with id " + id);
            return "noUserFound";
        }
    }

    private java.sql.Date getDate(String date) throws ParseException
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate;

        if(date.equals("today"))
        {
            myDate = dateFormat.parse(java.time.LocalDate.now().toString());
        }
        else
        {
            myDate = dateFormat.parse(date);
        }

        return new java.sql.Date(myDate.getTime());
    }
}
