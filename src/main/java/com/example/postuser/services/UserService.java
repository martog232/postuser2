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
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public RegisterResponseUserDTO addUser(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
        if(userRepository.findByEmail(userDTO.getEmail())!=null){
            throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
        }
        if(userRepository.findByUsername(userDTO.getUsername())!=null){
            throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
        }
        else if(!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))){
        throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());
        }
        userDTO.setPassword(encryptingPass(userDTO.getPassword()));
    User user=new User(userDTO);
    user=userRepository.save(user);
    return new RegisterResponseUserDTO(user);
}

public List<UserWithoutPassDTO> getAllUsers(){
    return userRepository.findAll().stream().map(UserWithoutPassDTO::new).collect(Collectors.toList());
}

public Optional<User> findById(Integer id){
    return Optional.ofNullable(userRepository.findById(id).orElseThrow(()->new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
}

    public UserWithoutPassDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException {
            User u = userRepository.findByUsername(loginDTO.getUsername());

        if (u == null) {
            throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());
        } else {
//            PasswordEncoder encoder = new BCryptPasswordEncoder();
//            if (encoder.matches(loginDTO.getPassword(), u.getPassword()))
            if(encryptingPass(loginDTO.getPassword()).equals(encryptingPass(u.getPassword())))
            {
                return new UserWithoutPassDTO(u);
            } else {
                throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());

            }
        }
    }
    public String encryptingPass(String password) throws NoSuchAlgorithmException {
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(password.getBytes());
byte[] bytes=m.digest();
        StringBuilder s = new StringBuilder();
        for (byte aByte : bytes) {
            s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return s.toString();}
}