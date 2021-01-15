package com.example.buttetinboard.dto;

import com.example.buttetinboard.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class ProfileDTO {
    private Long id;
    private String phoneNumber;
    private String userName;
    private String email;
}
