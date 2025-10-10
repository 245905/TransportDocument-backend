package com.tui.transport.converters;

import com.tui.transport.dto.UserDto;
import com.tui.transport.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserToUserDtoConverter {
    public UserDto convert(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
