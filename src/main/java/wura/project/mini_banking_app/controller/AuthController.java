package wura.project.mini_banking_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wura.project.mini_banking_app.dto.LoginDto;
import wura.project.mini_banking_app.dto.LoginResponseDto;
import wura.project.mini_banking_app.dto.RegisterDto;
import wura.project.mini_banking_app.dto.RegisterResponseDto;
import wura.project.mini_banking_app.service.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService registerService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterDto registerDto) {
        return  new ResponseEntity<>(registerService.register(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {

        return new ResponseEntity<>(registerService.login(loginDto), HttpStatus.OK);
    }

}
