package wura.project.mini_banking_app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wura.project.mini_banking_app.dto.LoginDto;
import wura.project.mini_banking_app.dto.LoginResponseDto;
import wura.project.mini_banking_app.dto.RegisterDto;
import wura.project.mini_banking_app.dto.RegisterResponseDto;
import wura.project.mini_banking_app.entity.Account;
import wura.project.mini_banking_app.entity.User;
import wura.project.mini_banking_app.exception.InvalidCredentialsException;
import wura.project.mini_banking_app.exception.UserAlreadyExistException;
import wura.project.mini_banking_app.mapper.UserMapper;
import wura.project.mini_banking_app.repository.AccountRepository;
import wura.project.mini_banking_app.repository.UserRepository;
import wura.project.mini_banking_app.util.AccountNumberGenerator;
import wura.project.mini_banking_app.util.JwtUtil;
import java.math.BigDecimal;



@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;



    @Override
    @Transactional
    public RegisterResponseDto register(RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new UserAlreadyExistException("An account already exists with this email");
        }

        User user = UserMapper.mapToUser(registerDto);

        user.setPassword(
                passwordEncoder.encode(registerDto.getPassword())
        );

        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        user.setAccount(account);

        User savedUser = userRepository.save(user);
        return UserMapper.mapToRegisterResponse(savedUser);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {


        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {

            throw new IllegalArgumentException("Email and password are required");
        }


        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> {
                    log.warn("Failed login attempt for email {} : invalid email", loginDto.getEmail());

                    throw new InvalidCredentialsException("Invalid email");
                });


        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for email {} : incorrect password", loginDto.getEmail());

            throw new InvalidCredentialsException("Invalid Password");
        }


        String token = jwtUtil.generateToken(user.getEmail());

        long expiresIn = jwtUtil.getExpirationTime();


        Account account = user.getAccount();


        log.info("Successful login for user: {}", user.getEmail());


        return LoginResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .token(token)
                .expiresIn(expiresIn)
                .message("Login successful")
                .build();
    }

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do {
            accountNumber = accountNumberGenerator.generate();
        } while (
                accountRepository.existsByAccountNumber(accountNumber)
        );

        return accountNumber;
    }

}
