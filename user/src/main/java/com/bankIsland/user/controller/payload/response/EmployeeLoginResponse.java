package com.bankIsland.user.controller.payload.response;

import com.bankIsland.user.dto.UserDto;

public record EmployeeLoginResponse (String token,
                                     UserDto userDto,
                                     String role) {}