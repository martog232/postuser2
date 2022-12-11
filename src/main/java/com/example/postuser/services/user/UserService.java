package com.example.postuser.services.user;

import com.example.postuser.model.dto.user.*;
import com.example.postuser.model.entities.Group;
import com.example.postuser.model.entities.User;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    String register(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException;

    void sendEmailWhenForgotPass(String email) throws NoSuchAlgorithmException;

    List<UserWithoutPassDTO> getAllUsers();

    List<UserWithNameDTO> getAllFollowings(Integer loggedUserId);

    Optional<UserWithoutPassDTO> getUserWithoutPassDTOById(Integer id);

    Optional<UserWithNameDTO> getUserWithNameDTOById(Integer id);

    Optional<User> getUserById(Integer id);

    UserWithNameDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException;

    String confirmToken(String token) throws NoSuchAlgorithmException;

    void deleteUser(Integer id);

    UserWithoutPassDTO mapToUserWithoutPassDTO(User entity);

    UserWithNameDTO mapToUserWithNameDTO(User entity);

    ResponseEntity<?> followAndUnfollow(Integer id, Integer loggedUserId);

    void confirmResetPassToken(String token) throws NoSuchAlgorithmException;

    void changePass(String rawStringToken, DoublePassDTO passDTO) throws NoSuchAlgorithmException;

    List<Group> getAllGroupsYouAreAdminOf(Integer loggedUserId);

    Optional<User> getUserByUserName(String username);

    UserWithoutPassDTO getUserDTOByUserName(String username);


}
