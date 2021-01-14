package com.example.buttetinboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {
    private Long id;
    private String categoryName;
    private String noteName;
    private String description;
    private Double price;
}
