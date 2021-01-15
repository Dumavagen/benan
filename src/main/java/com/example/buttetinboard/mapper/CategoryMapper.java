package com.example.buttetinboard.mapper;

import com.example.buttetinboard.dto.CategoryDTO;
import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Note;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "numberOfNotes", expression = "java(mapNotes(category.getNotes()))")
    CategoryDTO mapToDto(Category category);

    default Integer mapNotes(List<Note> numberOfNotes) {
        return numberOfNotes.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "notes", ignore = true)
    Category map(CategoryDTO categoryDTO);
}
