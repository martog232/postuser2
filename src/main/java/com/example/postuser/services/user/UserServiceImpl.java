package com.example.postuser.services.user;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.*;
import com.example.postuser.model.dto.user.RegisterRequestUserDTO;
import com.example.postuser.model.dto.user.UserLoginDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.Token;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.security.EmailSender;
import com.example.postuser.security.EmailValidator;
import com.example.postuser.security.PasswordEncrypting;
import com.example.postuser.services.email.EmailService;
import com.example.postuser.services.token.TokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableScheduling
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncrypting passwordEncrypting;
    private final EmailValidator emailValidator;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    public String register(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
        if (userRepository.findByEmail(userDTO.getEmail()) != null || userRepository.findByUsername(userDTO.getUsername()) != null) {

            if (userRepository.getIsConfirmedByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername())) {
                throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
            }
        }
        if (!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))) {
            throw new PasswordsNotSameException(APIErrorCode.PASSWORDS_NOT_SAME.getDescription());
        }
        if (!emailValidator.test(userDTO.getEmail())) {
            throw new EmailNotValidException(APIErrorCode.EMAIL_NOT_VALID.getDescription());
        }


        userDTO.setPassword(passwordEncrypting.encryptingPass(userDTO.getPassword()));
        User user = new User(userDTO);
        user = userRepository.save(user);

        String stringToken = UUID.randomUUID().toString();

        Token token = new Token(
                stringToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user

        );
        tokenService.saveToken(token);

        String link = "http://localhost:8080/confirm?token=" + token.getToken();
        emailSender.send(
                userDTO.getEmail(),
                emailService.buildSignUpEmail(userDTO.getUsername(), link));
        return stringToken;
    }

    public List<UserWithoutPassDTO> getAllUsers() {
        // TODO: 20.8.2022 г. fix it
        List<User> users = userRepository.findAll();
               List<UserWithoutPassDTO> dtos = users.stream().map(this::mapToUserWithoutPassDTO).collect(Collectors.toList());
        System.out.println(users);
        System.out.println(dtos);
        return dtos;
    }

    public List<UserWithNameDTO> getAllFollowings(Integer loggedUserId) {
        UserWithoutPassDTO loggedUser = mapToUserWithoutPassDTO(userRepository.findById(loggedUserId).get());
        return loggedUser.getFollowings();
    }


    public Optional<UserWithoutPassDTO> getUserWithoutPassDTOById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id).map(this::mapToUserWithoutPassDTO).orElseThrow(()
                -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public Optional<UserWithNameDTO> getUserWithNameDTOById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id).map(this::mapToUserWithNameDTO).orElseThrow(()
                -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public UserWithoutPassDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException {
        User u = userRepository.findByUsername(loginDTO.getUsername());
        if (u != null) {
            if (u.isConfirmed()) {
                if (passwordEncrypting.encryptingPass(loginDTO.getPassword())
                        .equals(u.getPassword())) {
                    System.out.println(u.getUsername() + " logged");
                    return mapToUserWithoutPassDTO(u);
                }
            }
        }
        throw new CredentialsNotCorrectException(APIErrorCode.CREDENTIALS_NOT_CORRECT.getDescription());
    }

    @Transactional
    public String confirmToken(String token) {
        Token confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        userRepository.enableUser(confirmationToken.getOwner().getEmail());
        return "confirmed";
    }

    public void deleteUser(Integer id) {
        UserWithoutPassDTO u = getUserWithoutPassDTOById(id).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        tokenService.deleteByOwnerId(id);
        userRepository.deleteUserById(u.getId());
    }

    @Transactional
    public ResponseEntity<?> followAndUnfollow(Integer id, Integer loggedUserId) {
        User userToFollow = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        User loggedUser = userRepository.findById(loggedUserId).get();
        List<User> loggedUserFollowings = loggedUser.getFollowings();
        if (loggedUserFollowings.contains(userToFollow)) loggedUserFollowings.remove(userToFollow);
        else loggedUserFollowings.add(userToFollow);
        loggedUser.setFollowings(loggedUserFollowings);
        userRepository.save(loggedUser);
        return new ResponseEntity<>(mapToUserWithoutPassDTO(userRepository.findById(loggedUserId).get()), HttpStatus.OK);
    }

    public UserWithoutPassDTO mapToUserWithoutPassDTO(User entity) {

        return modelMapper.map(entity, UserWithoutPassDTO.class);
    }

    public UserWithNameDTO mapToUserWithNameDTO(User entity) {

        return modelMapper.map(entity, UserWithNameDTO.class);
    }

}
