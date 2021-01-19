package com.example.buttetinboard.service;

import com.example.buttetinboard.dto.ProfileResponse;
import com.example.buttetinboard.mapper.ProfileMapper;
import com.example.buttetinboard.model.Profile;
import com.example.buttetinboard.model.User;
import com.example.buttetinboard.repository.ProfileRepository;
import com.example.buttetinboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserRepository userRepository;

    public void save(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id.toString()));
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return profileMapper.mapToDTO(profile, user);
    }

}
