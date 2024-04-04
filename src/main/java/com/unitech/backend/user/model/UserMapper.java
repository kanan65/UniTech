package com.unitech.backend.user.model;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO toDTO(User user) {
        if(user == null) return null;

        UserDTO result = new UserDTO();
        result.setFullName(user.getFullName());
        result.setPassword("**********");
        result.setUsername(user.getUsername());
        result.setEnabled(user.isEnabled());

        return result;
    }

    public static User toEntity(UserDTO userDTO) {
        if(userDTO == null) return null;

        User result = new User();
        result.setFullName(userDTO.getFullName());
        result.setPassword(userDTO.getPassword());
        result.setUsername(userDTO.getUsername());
        result.setEnabled(userDTO.isEnabled());

        return result;
    }
}
