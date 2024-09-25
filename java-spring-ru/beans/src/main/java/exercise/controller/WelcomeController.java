package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
public class WelcomeController {

    private static final String MESSAGE="It is %s now! Welcome to Spring!";
    @Autowired
    private Daytime daytime;

    @GetMapping("/welcome")
    private String getDaytime(){
      return String.format(MESSAGE, daytime.getName());
    }
}
// END
