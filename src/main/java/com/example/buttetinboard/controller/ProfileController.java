package com.example.buttetinboard.controller;

import com.example.buttetinboard.dto.ProfileResponse;
import com.example.buttetinboard.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/profiles")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfile(id));
    }
}
