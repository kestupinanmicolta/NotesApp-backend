package com.notes.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;
}
