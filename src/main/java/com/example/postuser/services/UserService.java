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
import com.example.postuser.security.EmailValidator;
import com.example.postuser.security.PasswordEncrypting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
   private final PasswordEncrypting passwordEncrypting;
   private  final EmailValidator emailValidator;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncrypting passwordEncrypting, EmailValidator emailValidator){
        this.userRepository=userRepository;
        this.passwordEncrypting = passwordEncrypting;
        this.emailValidator = emailValidator;
    }

    public RegisterResponseUserDTO addUser(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
        if(userRepository.findByEmail(userDTO.getEmail())!=null){
            throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
        }
        if(userRepository.findByUsername(userDTO.getUsername())!=null){
            throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
        }
        if(!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))){
        throw new MethodArgumentNotValidException("passwords are not same");
        }
        if(!emailValidator.test(userDTO.getEmail())){
            throw new MethodArgumentNotValidException("email not valid");
        }
        userDTO.setPassword(passwordEncrypting.encryptingPass(userDTO.getPassword()));
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
            if(passwordEncrypting.encryptingPass(loginDTO.getPassword()).equals(passwordEncrypting.encryptingPass(u.getPassword())))
            {
                return new UserWithoutPassDTO(u);
            } else {
                throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());

            }
        }
    }
}
