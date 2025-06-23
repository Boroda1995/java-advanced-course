package com.galdovich.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockedUserDto {
    private String email;
    private long remainingBlockMinutes;
}
