package com.example.buttetinboard.controller;

import com.example.buttetinboard.dto.CategoryDTO;
import com.example.buttetinboard.exceptions.ForbiddenException;
import com.example.buttetinboard.model.Role;
import com.example.buttetinboard.model.User;
import com.example.buttetinboard.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) throws ForbiddenException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.save(categoryDTO));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategory(id));
    }
}
