package com.example.buttetinboard.mapper;

import com.example.buttetinboard.dto.NoteRequest;
import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Note;
import com.example.buttetinboard.model.User;
import com.example.buttetinboard.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class NoteMapper {
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "id", source = "noteRequest.id")
    @Mapping(target = "description", source = "noteRequest.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "user", source = "user")
    public abstract Note map(NoteRequest noteRequest, Category category, User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "duration", expression = "java(getDuration(note))")
    public abstract NoteResponse mapToDto(Note note);

    String getDuration(Note note) {
        return TimeAgo.using(note.getCreatedDate().toEpochMilli());
    }
}
