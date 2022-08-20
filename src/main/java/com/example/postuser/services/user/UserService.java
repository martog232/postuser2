package com.example.postuser.services.user;

import com.example.postuser.model.dto.user.RegisterRequestUserDTO;
import com.example.postuser.model.dto.user.UserLoginDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.User;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    String register(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException;

    List<UserWithoutPassDTO> getAllUsers();

    List<UserWithNameDTO> getAllFollowings(Integer loggedUserId);

    Optional<UserWithoutPassDTO> getUserWithoutPassDTOById(Integer id);

    Optional<UserWithNameDTO> getUserWithNameDTOById(Integer id);

    UserWithoutPassDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException;

    String confirmToken(String token);

    void deleteUser(Integer id);

    UserWithoutPassDTO mapToUserWithoutPassDTO(User entity);

    UserWithNameDTO mapToUserWithNameDTO(User entity);

    ResponseEntity<?> followAndUnfollow(Integer id, Integer loggedUserId);
}
