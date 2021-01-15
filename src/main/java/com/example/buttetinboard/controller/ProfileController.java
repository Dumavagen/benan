package com.example.buttetinboard.controller;

import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.dto.ProfileDTO;
import com.example.buttetinboard.model.Profile;
import com.example.buttetinboard.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/profiles")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfile(id));
    }
}
