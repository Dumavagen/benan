package com.example.buttetinboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    private Long id;
    private String noteName;
    private String description;
    private String userName;
    private String categoryName;
    private String duration;
}
