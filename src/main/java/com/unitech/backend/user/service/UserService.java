package com.unitech.backend.user.service;

import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.request.RegisterRequestDTO;
import com.unitech.backend.user.model.User;
import com.unitech.backend.user.model.UserDTO;
import com.unitech.backend.user.model.UserMapper;
import com.unitech.backend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    public boolean usernameExists(String pin) {
        return userRepository.findUserByUsername(pin).isPresent();
    }

    public UserDTO getByPin(String pin) {
        Optional<User> user = userRepository.findUserByUsername(pin);
        if (user.isPresent()) {
            return UserMapper.toDTO(user.get());
        }
        return null;
    }

    public ApiResponseDTO register(RegisterRequestDTO requestDTO) {
        Optional<User> exist = userRepository.findUserByUsername(requestDTO.getPin());
        if(exist.isPresent()) return new ApiResponseDTO(ResponseEnum.SAME_PIN_ERROR_MESSAGE.getMessage(), "");

        User user = new User();
        user.setFullName(requestDTO.getName());
        user.setUsername(requestDTO.getPin());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        userRepository.save(user);
        return ApiResponseDTO.success();
    }

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(pin)
                .orElseThrow(
                        () -> new UsernameNotFoundException(format("User with pin - %s, not found", pin))
                );
    }
}
