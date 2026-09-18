package wura.project.mini_banking_app.service;

import wura.project.mini_banking_app.dto.LoginDto;
import wura.project.mini_banking_app.dto.LoginResponseDto;
import wura.project.mini_banking_app.dto.RegisterDto;
import wura.project.mini_banking_app.dto.RegisterResponseDto;

public interface AuthService {

    RegisterResponseDto register(RegisterDto registerDto);

    LoginResponseDto login(LoginDto loginDto);
}
