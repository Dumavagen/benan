package com.example.buttetinboard.controller;

import com.example.buttetinboard.dto.NoteRequest;
import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.exceptions.ForbiddenException;
import com.example.buttetinboard.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/notes")
@AllArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody NoteRequest noteRequest) {
        noteService.save(noteRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNote(id));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotes() {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getAllPosts());
    }

    @GetMapping("/by-category/{id}")
    public ResponseEntity<List<NoteResponse>> getNotesByCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNotesByCategory(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<NoteResponse>> getNotesByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNotesByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeNote(@PathVariable Long id, @RequestBody NoteRequest noteRequest) throws ForbiddenException {
        noteService.changeNote(id, noteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
