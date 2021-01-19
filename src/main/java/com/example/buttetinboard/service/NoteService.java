package com.example.buttetinboard.service;

import com.example.buttetinboard.dto.NoteRequest;
import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.exceptions.CategoryNotFoundException;
import com.example.buttetinboard.exceptions.ForbiddenException;
import com.example.buttetinboard.exceptions.NoteNotFoundException;
import com.example.buttetinboard.mapper.NoteMapper;
import com.example.buttetinboard.model.*;
import com.example.buttetinboard.repository.CategoryRepository;
import com.example.buttetinboard.repository.NoteCriteriaRepository;
import com.example.buttetinboard.repository.NoteRepository;
import com.example.buttetinboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final NoteCriteriaRepository noteCriteriaRepository;

    public Note save(NoteRequest noteRequest) {
        Category category = categoryRepository.findByName(noteRequest.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(noteRequest.getCategoryName()));
        Note note = noteMapper.map(noteRequest, category, authService.getCurrentUser());
        note.setStatus(Status.MODERATION);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public NoteResponse getNote(Long id) {
        User user = authService.getCurrentUser();
        Note note = null;
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

//    @Transactional(readOnly = true)
//    public List<NoteResponse> getAllPosts() {
//        User user = authService.getCurrentUser();
//        List<NoteResponse> notes = null;
//        if (user == null) {
//            notes = noteRepository.findAll()
//                    .stream()
//                    .filter(n -> !n.getStatus().equals(Status.MODERATION))
//                    .map(noteMapper::mapToDto)
//                    .collect(Collectors.toList());
//        } else if (authService.isAdmin(user)) {
//            notes = noteRepository.findAll()
//                    .stream()
//                    .map(noteMapper::mapToDto)
//                    .collect(Collectors.toList());
//        } else {
//            notes = noteRepository.findAll()
//                    .stream()
//                    .filter(n -> n.getUser().getId().equals(user.getId()) || !n.getStatus().equals(Status.MODERATION))
//                    .map(noteMapper::mapToDto)
//                    .collect(Collectors.toList());
//        }
//        return notes;
//    }

    @Transactional(readOnly = true)
    public Page<NoteResponse> getNotes(NotePage notePage,
                                       NoteSearchCriteria noteSearchCriteria) {
        Page<Note> notes = noteCriteriaRepository
                .findByWithFilters(notePage, noteSearchCriteria);
        return notes.map(noteMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        List<NoteResponse> notes = null;
        User user = authService.getCurrentUser();
        if (user == null) {
            notes = noteRepository.findAllByCategory(category)
                    .stream()
                    .filter(n -> !n.getStatus().equals(Status.MODERATION))
                    .map(noteMapper::mapToDto)
                    .collect(Collectors.toList());
        } else if (authService.isAdmin(user)) {
            notes = noteRepository.findAllByCategory(category)
                    .stream()
                    .map(noteMapper::mapToDto)
                    .collect(Collectors.toList());
        } else {
            notes = noteRepository.findAllByCategory(category)
                    .stream()
                    .filter(n -> n.getUser().getId().equals(user.getId()) || !n.getStatus().equals(Status.MODERATION))
                    .map(noteMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        return notes;
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByUsername(String username) {
        User findByUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        User user = authService.getCurrentUser();
        List<NoteResponse> notes = null;
        if (user == null) {
            notes = noteRepository.findByUser(findByUser)
                    .stream()
                    .filter(n -> !n.getStatus().equals(Status.MODERATION))
                    .map(noteMapper::mapToDto)
                    .collect(Collectors.toList());
        } else if (authService.isAdmin(user)) {
            notes = noteRepository.findByUser(findByUser)
                    .stream()
                    .map(noteMapper::mapToDto)
                    .collect(Collectors.toList());
        } else {
            notes = noteRepository.findByUser(findByUser)
                    .stream()
                    .filter(n -> n.getUser().getId().equals(user.getId()) || !n.getStatus().equals(Status.MODERATION))
                    .map(noteMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        return notes;
    }

    public Note changeNote(Long id, NoteRequest noteRequest) throws ForbiddenException {
        User user = authService.getCurrentUser();

        Category category = categoryRepository.findByName(noteRequest.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(noteRequest.getCategoryName()));
        Note noteFromBD = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Note not found"));
        if (user.getId().equals(noteFromBD.getUser().getId())) {
            Note note = noteMapper.map(noteRequest, category, authService.getCurrentUser());
            note.setId(noteFromBD.getId());
            note.setStatus(Status.MODERATION);
            return noteRepository.save(note);
        } else {
            throw new ForbiddenException(id.toString());
        }
    }
}
