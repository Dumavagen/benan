package com.example.buttetinboard.repository;

import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Note;
import com.example.buttetinboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByCategory(Category category);

    List<Note> findByUser(User user);
}
