package com.mitioglov.web.controllers;

import com.mitioglov.model.domain.User;
import com.mitioglov.dto.UserDto;
import com.mitioglov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("user") UserDto accountDto,
             BindingResult result, WebRequest request, Errors errors) {
        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto, result);
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", accountDto);
        }
        else {
            return new ModelAndView("registered", "user", accountDto);
        }
    }

    @RequestMapping(value = "/registered", method = RequestMethod.GET)
    public String registrationSuccess(WebRequest request, Model model) {
        return "registered";
    }

    private User createUserAccount(UserDto accountDto, BindingResult result) {
        User toRegisterUser = new User();
        toRegisterUser.setEmail(accountDto.getEmail());
        toRegisterUser.setFirstName(accountDto.getFirstName());
        toRegisterUser.setLastName(accountDto.getLastName());
        toRegisterUser.setNickname(accountDto.getEmail());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(accountDto.getPassword());
        toRegisterUser.setPassword(hashedPassword);
        return userService.save(toRegisterUser);
    }
}
