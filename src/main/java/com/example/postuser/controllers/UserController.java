package com.example.postuser.controllers;

import com.example.postuser.model.dto.RegisterRequestUserDTO;
import com.example.postuser.model.dto.UserLoginDTO;
import com.example.postuser.model.dto.UserWithoutPassDTO;
import com.example.postuser.model.entities.User;
import com.example.postuser.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {

        return userService.register(userDTO);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }

    @PostMapping(value = "/sign-in", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public UserWithoutPassDTO login(@RequestBody UserLoginDTO loginDTO, HttpSession ses) throws NoSuchAlgorithmException {
        UserWithoutPassDTO responseDTO = userService.login(loginDTO);
        ses.setAttribute("LoggedUser", responseDTO.getId());

        return responseDTO;
    }

    @GetMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserWithoutPassDTO> getAll() {

        return userService.getAllUsers();

    }

    @GetMapping(value = "/users/{id}")
    public Optional<User> findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        System.out.println("Valo");
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
