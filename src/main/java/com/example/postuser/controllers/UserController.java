package com.example.postuser.controllers;

import com.example.postuser.model.dto.RegisterRequestUserDTO;
import com.example.postuser.model.dto.RegisterResponseUserDTO;
import com.example.postuser.model.dto.UserLoginDTO;
import com.example.postuser.model.dto.UserWithoutPassDTO;
import com.example.postuser.model.entities.User;
import com.example.postuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
        return userService.addUser(userDTO);
    }

    @PostMapping
    public UserWithoutPassDTO login(@RequestBody UserLoginDTO loginDTO, HttpSession ses) throws NoSuchAlgorithmException {
        UserWithoutPassDTO responseDTO=userService.login(loginDTO);
        ses.setAttribute("LoggedUser",responseDTO.getId() );
        return responseDTO;
    }

    @GetMapping(value = "/users",produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserWithoutPassDTO> getAll(){
        return userService.getAllUsers();

    }
    @GetMapping(value = "/users/{id}")
    public Optional<User> findById(@PathVariable Integer id){
       return userService.findById(id);
    }

}
