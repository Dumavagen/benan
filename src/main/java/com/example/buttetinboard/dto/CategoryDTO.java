package com.example.buttetinboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfNotes;
}
