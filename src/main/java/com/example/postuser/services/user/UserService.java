package com.example.postuser.services.user;

import com.example.postuser.model.dto.user.RegisterRequestUserDTO;
import com.example.postuser.model.dto.user.UserLoginDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    String register(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException;

    List<UserWithoutPassDTO> getAllUsers();

    Optional<UserWithoutPassDTO> findById(Integer id);

    UserWithoutPassDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException;

    String confirmToken(String token);

    void deleteUser(Integer id);

    User mapToEntity(UserWithoutPassDTO dto);

    UserWithoutPassDTO mapToDTO(User entity);

    void save(UserWithoutPassDTO u);
}
