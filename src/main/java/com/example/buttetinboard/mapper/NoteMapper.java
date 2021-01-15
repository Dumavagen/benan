package com.example.buttetinboard.mapper;

import com.example.buttetinboard.dto.NoteRequest;
import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.model.Category;
import com.example.buttetinboard.model.Note;
import com.example.buttetinboard.model.User;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class NoteMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "id", source = "noteRequest.id")
    @Mapping(target = "description", source = "noteRequest.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "noteName", source = "noteRequest.noteName")
    @Mapping(target = "status", ignore = true)
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
