package com.notes.api.service;

import com.notes.api.dto.NoteRequest;
import com.notes.api.dto.NoteResponse;
import com.notes.api.model.Note;
import com.notes.api.model.User;
import com.notes.api.repository.NoteRepository;
import com.notes.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public List<NoteResponse> getNotesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.findByUserIdOrderByUpdatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NoteResponse getNoteById(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(note);
    }

    public NoteResponse createNote(NoteRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Note saved = noteRepository.save(note);
        return toResponse(saved);
    }

    public NoteResponse updateNote(Long id, NoteRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        Note updated = noteRepository.save(note);
        return toResponse(updated);
    }

    public void deleteNote(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        noteRepository.delete(note);
    }

    private NoteResponse toResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setUserId(note.getUser().getId());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        return response;
    }
}
