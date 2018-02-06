package finalyearproject.controller;

import finalyearproject.model.RefinedUser;
import finalyearproject.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<RefinedUser> list() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public RefinedUser create(@RequestBody RefinedUser refinedUser) {
        return userRepository.saveAndFlush(refinedUser);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public RefinedUser get(@PathVariable String id) {
        return userRepository.getOne(id);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.PUT)
    public RefinedUser update(@PathVariable String id, @RequestBody RefinedUser refinedUser) {
        RefinedUser existingRefinedUser = userRepository.findOne(id);
        BeanUtils.copyProperties(refinedUser, existingRefinedUser);
        return userRepository.saveAndFlush(existingRefinedUser);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
    public RefinedUser delete(@PathVariable String id) {
        RefinedUser existingRefinedUser = userRepository.findOne(id);
        userRepository.delete(existingRefinedUser);
        return existingRefinedUser;
    }

}
