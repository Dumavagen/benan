package com.example.buttetinboard.service;

import com.example.buttetinboard.dto.CategoryDTO;
import com.example.buttetinboard.exceptions.CategoryNotFoundException;
import com.example.buttetinboard.exceptions.ForbiddenException;
import com.example.buttetinboard.mapper.CategoryMapper;
import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Role;
import com.example.buttetinboard.model.User;
import com.example.buttetinboard.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthService authService;

    public CategoryDTO save(CategoryDTO categoryDTO) throws ForbiddenException {
        if (authService.isAdmin(authService.getCurrentUser())) {
            Category category = categoryMapper.map(categoryDTO);
            categoryRepository.save(category);
            categoryDTO.setId(category.getId());
            return categoryDTO;
        } else {
            throw new ForbiddenException("You don't have roles for this operation");
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToDto)
                .collect(Collectors.toList());
    }


    public CategoryDTO getCategory(Long id) {
        Category subreddit = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("No category found with id"));
        return categoryMapper.mapToDto(subreddit);
    }

}
