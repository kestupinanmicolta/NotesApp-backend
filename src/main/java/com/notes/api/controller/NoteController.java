package com.notes.api.controller;

import com.notes.api.dto.NoteRequest;
import com.notes.api.dto.NoteResponse;
import com.notes.api.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes(Authentication authentication) {
        List<NoteResponse> notes = noteService.getNotesByUser(authentication.getName());
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long id, Authentication authentication) {
        NoteResponse note = noteService.getNoteById(id, authentication.getName());
        return ResponseEntity.ok(note);
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody NoteRequest request,
                                                    Authentication authentication) {
        NoteResponse note = noteService.createNote(request, authentication.getName());
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id,
                                                    @Valid @RequestBody NoteRequest request,
                                                    Authentication authentication) {
        NoteResponse note = noteService.updateNote(id, request, authentication.getName());
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Authentication authentication) {
        noteService.deleteNote(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
