package com.user.User.Controller;
import com.user.User.Model.User;
import com.user.User.Repository.UserRepository;
import com.user.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

//  Controller ------> Service --------> Repository
@RestController
@RequestMapping("/users")
public class UserController
{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @RequestMapping("/check")
    public String check()
    {
        return "MY PROJECT IS NOW RUNNING ";
    }

    @PostMapping("/create")
    public Integer createNewUser(@RequestBody User newUser)
    {
        User user= userRepository.save(newUser);      // Directly Calling Repository
        return user.getId();                          // Returns newly Created User I
        // make an entry into the userauth table (username,password)
    }
    @GetMapping("/findUser/{id}")
    public Optional<User> findUserById(@PathVariable int id){
       // LOGGER.info("Request for /findUserById had id as {}", id);
        System.out.println("Request for /findUserById had id as " + id);
        return userService.findUserById(id);
    }


    @GetMapping("/getAllUsers")
    public List<User> getAllUsers()
    {
         return userService.getAllUsers();
    }

//    @GetMapping("/findUserById/{id}")                        //http://localhost:8080/users/findUserById/100
//    public Optional<User> findUserById(@PathVariable int id)
//    {
//        return userService.findUserById(id);
//    }
}
