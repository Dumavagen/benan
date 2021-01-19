package com.example.buttetinboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProfileResponse {
    private Long id;
    private String phoneNumber;
    private String userName;
    private String email;
}
