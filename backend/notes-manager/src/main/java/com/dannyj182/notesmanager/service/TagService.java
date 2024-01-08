package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.mapper.TagMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import com.dannyj182.notesmanager.repository.ITagRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TagService implements ITagService{

    private final ITagRepository repository;
    private final TagMapper mapper;

    private final INoteRepository noteRepository;

    @Override
    public TagDTO saveTag(TagDTO tagDTO) {
        if (tagDTO.getName().isEmpty() || tagDTO.getName().isBlank()) return null;
        if (repository.existsById(tagDTO.getName())) return null;
        return mapper.toTagDTO(repository.save(mapper.toTag(tagDTO)));
    }

    @Override
    public List<TagDTO> findAll() {
        return mapper.toTagsDTO((List<Tag>) repository.findAll());
    }

    @Override
    public ResponseEntity<?> deleteById(String name) {
        Optional<Tag> optionalTag = repository.findById(name);
        if (optionalTag.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Note> noteList = noteRepository.findAllByTagsContains(optionalTag.get());
        if (noteList.size() > 0) return new ResponseEntity<>(HttpStatus.CONFLICT);
        repository.deleteById(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public List<Tag> findAllById(List<TagDTO> tagDTOList) {
        List<String> list = List.of(tagDTOList.stream().map(TagDTO::getName).toArray(String[]::new));
        return (List<Tag>) repository.findAllById(list);
    }
}
