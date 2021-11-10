package com.example.postuser.services;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.DuplicateEntityException;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.exceptions.MethodArgumentNotValidException;
import com.example.postuser.model.dto.RegisterRequestUserDTO;
import com.example.postuser.model.dto.UserLoginDTO;
import com.example.postuser.model.dto.UserWithoutPassDTO;
import com.example.postuser.model.entities.Token;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.security.EmailSender;
import com.example.postuser.security.EmailValidator;
import com.example.postuser.security.PasswordEncrypting;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncrypting passwordEncrypting;
    private final EmailValidator emailValidator;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final EmailService emailService;

    public String register(RegisterRequestUserDTO userDTO) throws NoSuchAlgorithmException {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
        }
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new DuplicateEntityException(APIErrorCode.DUPLICATE_ENTITY.getDescription());
        }
        if (!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))) {
            throw new MethodArgumentNotValidException("passwords are not same");
        }
        if (!emailValidator.test(userDTO.getEmail())) {
            throw new MethodArgumentNotValidException("email not valid");
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
        //return new RegisterResponseUserDTO(user);
        return stringToken;
    }

    public List<UserWithoutPassDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserWithoutPassDTO::new).collect(Collectors.toList());
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public UserWithoutPassDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException {
        User u = userRepository.findByUsername(loginDTO.getUsername());

        if (u == null) {
            throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());
        } else {
            if (passwordEncrypting.encryptingPass(loginDTO.getPassword()).equals(passwordEncrypting.encryptingPass(u.getPassword()))) {
                return new UserWithoutPassDTO(u);
            } else {
                throw new MethodArgumentNotValidException(APIErrorCode.METHOD_ARG_NOT_VALID.getDescription());

            }
        }
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
}
