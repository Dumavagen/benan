package com.example.buttetinboard.service;

import com.example.buttetinboard.dto.NoteRequest;
import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.exceptions.CategoryNotFoundException;
import com.example.buttetinboard.exceptions.NoteNotFoundException;
import com.example.buttetinboard.mapper.NoteMapper;
import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Note;
import com.example.buttetinboard.model.User;
import com.example.buttetinboard.repository.CategoryRepository;
import com.example.buttetinboard.repository.NoteRepository;
import com.example.buttetinboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final NoteMapper noteMapper;

    public Note save(NoteRequest noteRequest) {
        Category category = categoryRepository.findByName(noteRequest.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(noteRequest.getCategoryName()));
        Note note = noteMapper.map(noteRequest, category, authService.getCurrentUser());
//        note.setCategory(category);
//        note.setUser(authService.getCurrentUser());
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public NoteResponse getNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id.toString()));
        return noteMapper.mapToDto(note);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getAllPosts() {
        return noteRepository.findAll()
                .stream()
                .map(noteMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        List<Note> notes = noteRepository.findAllByCategory(category);
        return notes.stream().map(noteMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return noteRepository.findByUser(user)
                .stream()
                .map(noteMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
