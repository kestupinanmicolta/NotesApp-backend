package com.notes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private String locationName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
