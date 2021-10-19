package com.example.postuser.services;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.DuplicateEntityException;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.exceptions.MethodArgumentNotValidException;
import com.example.postuser.model.dto.RegisterRequestUserDTO;
import com.example.postuser.model.dto.RegisterResponseUserDTO;
import com.example.postuser.model.dto.UserLoginDTO;
import com.example.postuser.model.dto.UserWithoutPassDTO;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public RegisterResponseUserDTO addUser(RegisterRequestUserDTO userDTO){
    if(!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))){
        throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());
    }
    if(userRepository.findByEmail(userDTO.getEmail())!=null){
        throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
    }
    if(userRepository.findByUsername(userDTO.getUsername())!=null){
        throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
    }
    PasswordEncoder encoder=new BCryptPasswordEncoder();
            userDTO.setPassword(encoder.encode(userDTO.getPassword() ));
    User user=new User(userDTO);
    user=userRepository.save(user);
    return new RegisterResponseUserDTO(user);
}

public List<UserWithoutPassDTO> getAllUsers(){
//    List<User> users=userRepository.findAll();
//    List<UserWithoutPassDTO> returnUsers=new ArrayList<>();
//    for(User u:users){
//        returnUsers.add(new UserWithoutPassDTO(u));
//    }
//    return returnUsers;
    return userRepository.findAll().stream().map(UserWithoutPassDTO::new).collect(Collectors.toList());
}

public Optional<User> findById(Integer id){
    return Optional.ofNullable(userRepository.findById(id).orElseThrow(()->new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
}

    public UserWithoutPassDTO login(UserLoginDTO loginDTO) {
            User u = userRepository.findByUsername(loginDTO.getUsername());

        if (u == null) {
            throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(loginDTO.getPassword(), u.getPassword())) {
                return new UserWithoutPassDTO(u);
            } else {
                throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());

            }
        }
    }
}
