package com.jerry.school_project.mapper;

import com.jerry.school_project.dto.UserDTO;
import com.jerry.school_project.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    // Convert User entity to UserDTO (excluding password)
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRegistrationDate()
        );
    }

    // Convert list of User entities to list of UserDTOs
    public List<UserDTO> toUserDTOList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = toUserDTO(user);
            if (userDTO != null) {
                userDTOs.add(userDTO);
            }
        }
        return userDTOs;
    }
}