package db.calorietracker.calorietrackerapp;

import db.calorietracker.calorietrackerapp.model.User;
import db.calorietracker.calorietrackerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class HomeResource
{
    @Autowired
    UserRepository userRepository;

    private final static Logger LOGGER = Logger.getLogger(HomeResource.class.getName());

    @GetMapping("/")
    public String getAllUsers(Model model) throws ParseException
    {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        model.addAttribute("users", users);

        return "homepage";
    }

    @PostMapping("/")
    public String addUser(@RequestBody MultiValueMap<String, String> params, Model model)
    {
        User user = new User(params);
        userRepository.save(user);

        return "redirect:http://localhost:8080";
    }

    //should be a delete mapping, but its all i can do because delete is not avaialable in form method
    //i could keep this and the function then decides if the id is found, delete, otherwise create
    @PostMapping("/user/{id}/delete")
    public String removeUser(@PathVariable int id, Model model)
    {
        LOGGER.info("deleting user with id " + id);
        userRepository.deleteById(id);

        return "redirect:http://localhost:8080";
    }
}
