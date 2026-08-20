package davidepan.capstone.controllers;

import davidepan.capstone.entities.User;
import davidepan.capstone.payloads.UserLoginDTO;
import davidepan.capstone.payloads.UserLoginResponseDTO;
import davidepan.capstone.payloads.UserRegistrationDTO;
import davidepan.capstone.services.AuthService;
import davidepan.capstone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body){
        String accessToken = authService.authenticateUserAndGenerateToken(body);
        return new UserLoginResponseDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody UserRegistrationDTO body){
        return userService.save(body);
    }
}
