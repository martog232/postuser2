package com.example.postuser.controllers;

import com.example.postuser.model.dto.user.RegisterRequestUserDTO;
import com.example.postuser.model.dto.user.UserLoginDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.services.user.UserService;
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
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private SessionManager sessionManager;


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
        sessionManager.loginUser(ses, responseDTO.getId());
        return responseDTO;
    }

    @PostMapping(value = "/sign-out")
    public void logout(HttpSession ses){
        sessionManager.logoutUser(ses);
    }

    @GetMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserWithoutPassDTO> getAll() {

        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{id}")
    public Optional<UserWithoutPassDTO> findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
