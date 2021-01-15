package com.example.buttetinboard.repository;

import com.example.buttetinboard.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
