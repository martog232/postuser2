package com.example.postuser.services;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.*;
import com.example.postuser.model.dto.user.RegisterRequestUserDTO;
import com.example.postuser.model.dto.user.UserLoginDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.Token;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.security.EmailSender;
import com.example.postuser.security.EmailValidator;
import com.example.postuser.security.PasswordEncrypting;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncrypting passwordEncrypting;
    private final EmailValidator emailValidator;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final EmailService emailService;
    ModelMapper modelMapper;

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
                emailService.buildEmail(userDTO.getUsername(), link));
        return stringToken;
    }

    public List<UserWithoutPassDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }


    public Optional<UserWithoutPassDTO> findById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id).map(this::mapToDTO).orElseThrow(()
                -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public UserWithoutPassDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException {
        User u = userRepository.findByUsername(loginDTO.getUsername());
        if (u != null) {
            if (u.isConfirmed()) {
                if (passwordEncrypting.encryptingPass(loginDTO.getPassword())
                        .equals(u.getPassword())) {
                    System.out.println(u.getUsername() + " logged");
                    return mapToDTO(u);
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

//    @Scheduled(fixedDelay = 15000)
//    public void deletingNotConfirmedTokens() {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        tokenRepository.deleteExpiredToken(localDateTime);
//    }

    public void deleteUser(Integer id) {
        UserWithoutPassDTO u = findById(id).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        tokenService.deleteByOwnerId(id);
        userRepository.deleteUserById(u.getId());
    }

    public User mapToEntity(UserWithoutPassDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserWithoutPassDTO mapToDTO(User entity) {

        return modelMapper.map(entity, UserWithoutPassDTO.class);
    }

    public void save(UserWithoutPassDTO u) {
        userRepository.save(mapToEntity(u));
    }
}
