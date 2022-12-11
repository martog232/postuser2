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

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private SessionManager sessionManager;


    @PostMapping(value="/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
        userService.register(userDTO);
    }

    @GetMapping("/confirm")
    public String confirmRegistrationToken(@RequestParam String token) throws NoSuchAlgorithmException {
        return userService.confirmToken(token);
    }

    @PostMapping(value = "/sign-in", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO, HttpSession ses) throws NoSuchAlgorithmException {
        UserWithNameDTO responseDTO = userService.login(loginDTO);
        sessionManager.loginUser(ses, responseDTO.getId());
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
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

//    @GetMapping(value = ControllerConfig.USERS_URL + "/{id}")
//    public Optional<UserWithoutPassDTO> findById(@PathVariable Integer id) {
//        return userService.getUserWithoutPassDTOById(id);
//    }

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
