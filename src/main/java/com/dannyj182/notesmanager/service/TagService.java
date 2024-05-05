package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.TagMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import com.dannyj182.notesmanager.repository.ITagRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TagService implements ITagService{

    private final ITagRepository repository;
    private final TagMapper mapper;
    private final IUserService userService;
    private final INoteRepository noteRepository;

    @Override
    @Transactional
    public TagDTO saveTag(TagDTO tagDTO) {
        User user = this.getUser(this.getUsername());
        if (user == null) return null;
        if (tagDTO.getName().isEmpty() || tagDTO.getName().isBlank()) return null;
        if (repository.existsById(tagDTO.getName())) return null;
        Tag tag = mapper.toTag(tagDTO);
        tag.setUser(user);
        return mapper.toTagDTO(repository.save(tag));
    }

    @Override
    @Transactional
    public List<TagDTO> findTagByUsername() {
        return mapper.toTagsDTO(repository.findAllByUser_Username(this.getUsername()));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteById(String name) {
        Optional<Tag> optionalTag = repository.findById(name);
        if (optionalTag.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Note> noteList = noteRepository.findAllByTagsContains(optionalTag.get());
        if (!noteList.isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        repository.deleteById(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public List<Tag> findAllById(List<TagDTO> tagDTOList) {
        List<String> list = List.of(tagDTOList.stream().map(TagDTO::getName).toArray(String[]::new));
        return repository.findAllById(list);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private User getUser(String userName){
        return userService.findById(userName);
    }
}
