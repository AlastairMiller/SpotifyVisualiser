package finalyearproject.controller;

import finalyearproject.model.User;
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
    public List<User> list() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public User create(@RequestBody User user) {
        return userRepository.saveAndFlush(user);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public User get(@PathVariable String id) {
        return userRepository.getOne(id);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable String id, @RequestBody User user) {
        User existingUser = userRepository.findOne(id);
        BeanUtils.copyProperties(user, existingUser);
        return userRepository.saveAndFlush(existingUser);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
    public User delete(@PathVariable String id) {
        User existingUser = userRepository.findOne(id);
        userRepository.delete(existingUser);
        return existingUser;
    }

}
