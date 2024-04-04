package com.unitech.backend.user.controller;


import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.request.LoginRequestDTO;
import com.unitech.backend.request.RegisterRequestDTO;
import com.unitech.backend.security.JWTUtil;
import com.unitech.backend.user.model.User;
import com.unitech.backend.user.repository.UserRepository;
import com.unitech.backend.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping(value = {"/user"})
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping(value = "/register")
    public ApiResponseDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try{
            return userService.register(registerRequestDTO);
        }
        catch (Exception e){
            System.out.println(e);
            return new ApiResponseDTO(ResponseEnum.UNKNOWN_ERROR.getMessage(), "");
        }
    }

    @PostMapping(value = "/login")
    public ApiResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getPin(), loginRequestDTO.getPassword()));

            User user = (User) authenticate.getPrincipal();

            return ApiResponseDTO.success(jwtUtil.generateAccessToken(user));
        }
        catch (BadCredentialsException ex) {
            return new ApiResponseDTO(ResponseEnum.BAD_CREDENTIALS_ERROR_MESSAGE.getMessage(), "");
        }
    }

    @PostMapping(value = "/self")
    public ApiResponseDTO self() {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (currentUser == null) return new ApiResponseDTO(ResponseEnum.AUTHENTICATION_FAILED_ERROR_MESSAGE.AUTHENTICATION_FAILED_ERROR_MESSAGE.getMessage(), "");
            return ApiResponseDTO.success(userRepository.findById(currentUser.getId()));
        }
        catch (Exception e){
            return new ApiResponseDTO(ResponseEnum.UNKNOWN_ERROR.getMessage(), "");
        }
    }

}
