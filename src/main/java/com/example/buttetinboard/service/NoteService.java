package com.example.buttetinboard.service;

import com.example.buttetinboard.dto.NoteRequest;
import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.exceptions.CategoryNotFoundException;
import com.example.buttetinboard.exceptions.ForbiddenException;
import com.example.buttetinboard.exceptions.NoteNotFoundException;
import com.example.buttetinboard.mapper.NoteMapper;
import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Note;
import com.example.buttetinboard.model.Status;
import com.example.buttetinboard.model.User;
import com.example.buttetinboard.repository.CategoryRepository;
import com.example.buttetinboard.repository.NoteRepository;
import com.example.buttetinboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
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
        note.setStatus(Status.MODERATION);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public NoteResponse getNote(Long id) {
        Note note = null;
        User user = authService.getCurrentUser();
        if (user == null) {
            note = noteRepository.findById(id)
                    .filter(n -> !n.getStatus().equals(Status.MODERATION))
                    .orElseThrow(() -> new NoteNotFoundException(id.toString()));
        } else if (authService.isAdmin(user)) {
            note = noteRepository.findById(id)
                    .orElseThrow(() -> new NoteNotFoundException(id.toString()));
        } else {
            note = noteRepository.findById(id)
                    .filter(n -> n.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new NoteNotFoundException(id.toString()));
        }
        return noteMapper.mapToDto(note);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getAllPosts() {
        return noteRepository.findAll()
                .stream()
                .filter(n -> !n.getStatus().equals(Status.MODERATION))
                .map(noteMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        List<Note> notes = noteRepository.findAllByCategory(category);
        return notes.stream()
                .filter(n -> !n.getStatus().equals(Status.MODERATION))
                .map(noteMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return noteRepository.findByUser(user)
                .stream()
                .filter(n -> !n.getStatus().equals(Status.MODERATION))
                .map(noteMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Note changeNote(Long id, NoteRequest noteRequest) {
        Category category = categoryRepository.findByName(noteRequest.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(noteRequest.getCategoryName()));
        Note noteFromBD = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Note not found"));
        Note note = noteMapper.map(noteRequest, category, authService.getCurrentUser());
        note.setId(noteFromBD.getId());
        note.setStatus(Status.MODERATION);
        return noteRepository.save(note);
    }

    private boolean hasAccess(Long id) {
        User currentUser = authService.getCurrentUser();
        Note note = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Note not found"));
        User userByNote = note.getUser();

        if (authService.isAdmin(currentUser)) {
            return true;
        }

        return userByNote.getId().equals(currentUser);
    }
}
