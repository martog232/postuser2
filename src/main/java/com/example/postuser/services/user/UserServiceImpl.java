package com.example.postuser.services.user;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.*;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.*;
import com.example.postuser.model.entities.Group;
import com.example.postuser.model.entities.Token;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.security.EmailSender;
import com.example.postuser.security.EmailValidator;
import com.example.postuser.security.PasswordAndTokenEncrypting;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableScheduling
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordAndTokenEncrypting passwordTokenEncrypting;
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

        userDTO.setPassword(passwordTokenEncrypting.encrypting(userDTO.getPassword()));
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
                emailService.RegBuildSignUpEmail(userDTO.getUsername(), link), "Confirm your email");
        return stringToken;
    }

    public void sendEmailWhenForgotPass(String email) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String stringToken = UUID.randomUUID().toString();
            String encryptedToken = passwordTokenEncrypting.encrypting(stringToken);
            Token token = new Token(
                    encryptedToken,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user);

            tokenService.saveToken(token);

            String link = "http://localhost:4200/reset-pass/" + stringToken;
            emailSender.send(
                    user.getEmail(),
                    emailService.ResetPassBuildSignUpEmail(user.getUsername(), link), "Change your password");
            user.setPassword("");
            userRepository.save(user);
        }
    }

    public List<UserWithoutPassDTO> getAllUsers() {
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

    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public UserWithNameDTO login(UserLoginDTO loginDTO) throws NoSuchAlgorithmException {
        User u = userRepository.findByUsername(loginDTO.getUsername());
        if (u != null) {
            if (u.isConfirmed()) {
                if (passwordTokenEncrypting.encrypting(loginDTO.getPassword())
                        .equals(u.getPassword())) {
                    System.out.println(u.getUsername() + " logged");
                    return mapToUserWithNameDTO(u);
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

    @Transactional
    public void confirmResetPassToken(String token) throws NoSuchAlgorithmException {
        String encryptedToken = passwordTokenEncrypting.encrypting(token);
        if (tokenService.getToken(encryptedToken).isPresent()) {
            tokenService.setConfirmedAt(encryptedToken);
        }
    }

    @Transactional
    public void changePass(String rawStringToken, DoublePassDTO passDTO) throws NoSuchAlgorithmException {
        if (Objects.equals(passDTO.getNewPass(), passDTO.getConfirmNewPass())) {
            String encryptedToken = passwordTokenEncrypting.encrypting(rawStringToken);
            Optional<Token> token = tokenService.getToken(encryptedToken);
            if (token.isPresent()) {
                if (token.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("token expired");
                }
                User user = token.get().getOwner();
                user.setPassword(passwordTokenEncrypting.encrypting(passDTO.getNewPass()));
                userRepository.save(user);
                tokenService.deleteByOwnerId(user.getId());
                new ResponseEntity<>("Password is changed", HttpStatus.OK);
                return;
            }
            throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
        }
        throw new PasswordsNotSameException(APIErrorCode.PASSWORDS_NOT_SAME.getDescription());
    }

    @Override
    public List<Group> getAllGroupsYouAreAdminOf(Integer loggedUserId) {
        return null;
    }

    @Override
    public Optional<User> getUserByUserName(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public UserWithoutPassDTO getUserDTOByUserName(String username) {
        Optional<UserWithoutPassDTO> userDto = Optional.ofNullable(mapToUserWithoutPassDTO(userRepository.findByUsername(username)));
  if(userDto.isPresent()) {
      List<PostDTO> resultList = new ArrayList<>();
      UserWithoutPassDTO user = userDto.get();
      List<PostDTO> posts = user.getPosts();
      for (PostDTO post : posts) {
          if (post.getGroup() == null) resultList.add(post);
      }
      user.setPosts(resultList);

      return user;
  }
        throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());

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
