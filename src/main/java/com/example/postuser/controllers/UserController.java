package com.example.postuser.controllers;

import com.example.postuser.controllers.config.ControllerConfig;
import com.example.postuser.model.dto.user.*;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private SessionManager sessionManager;


    @PostMapping(value="/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpStatus> register(@RequestBody RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
       String serviceResult = userService.register(userDTO);
       HttpStatus status = HttpStatus.valueOf(200);
        if ("1".equals(serviceResult)) {
            status = HttpStatus.valueOf(409);
        }
        return new ResponseEntity<>(status);
    }

    @GetMapping("/confirm")
    public String confirmRegistrationToken(@RequestParam String token) throws NoSuchAlgorithmException {
        return userService.confirmToken(token);
    }

    @PostMapping(value = "/sign-in", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO, HttpSession ses) throws NoSuchAlgorithmException {
        ResponseEntity<UserWithNameDTO> response = userService.login(loginDTO);
        if(response.getBody()!=null) {
            sessionManager.loginUser(ses, Objects.requireNonNull(userService.login(loginDTO).getBody()).getId());
        }
        return response;
    }

    @PostMapping(value = "/sign-out")
    public void logout(HttpSession ses) {
        sessionManager.logoutUser(ses);
    }

    @GetMapping(value = "/forgot-pass")
    public void forgotPass(@RequestParam String email) throws NoSuchAlgorithmException {
        userService.sendEmailWhenForgotPass(email);
    }

    @PostMapping(value = "reset-pass", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void changePass(@RequestParam String token,@RequestBody DoublePassDTO passDTO) throws NoSuchAlgorithmException {
        userService.changePass(token,passDTO);
    }

    @GetMapping(value = ControllerConfig.USERS_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserWithoutPassDTO> getAll() {

        return userService.getAllUsers();
    }

    @GetMapping(value = ControllerConfig.USERS_URL + "/{username}")
    public UserWithoutPassDTO findByUsername(@PathVariable String username) {
        return userService.getUserDTOByUserName(username);
    }

    @DeleteMapping(value = ControllerConfig.USERS_URL + "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = ControllerConfig.USERS_URL + "/follow/{id}")
    public ResponseEntity<?> followAndUnfollow(@PathVariable Integer id, HttpSession ses) throws AuthenticationException {
        return userService.followAndUnfollow(id, sessionManager.getLoggedUser(ses));
    }
}
