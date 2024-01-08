package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.mapper.TagMapper;
import com.dannyj182.notesmanager.repository.ITagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService implements ITagService{

    private final ITagRepository repository;
    private final TagMapper mapper;

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
    public boolean deleteById(String name) {
        if (!repository.existsById(name)) return false;
        repository.deleteById(name);
        return true;
    }

    @Override
    public List<Tag> findAllById(List<TagDTO> tagDTOList) {
        List<String> list = List.of(tagDTOList.stream().map(TagDTO::getName).toArray(String[]::new));
        return (List<Tag>) repository.findAllById(list);
    }
}
