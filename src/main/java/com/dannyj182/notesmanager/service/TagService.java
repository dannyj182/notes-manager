package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.TagMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import com.dannyj182.notesmanager.repository.ITagRepository;
import com.dannyj182.notesmanager.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class TagService implements ITagService {

    private final ITagRepository repository;
    private final TagMapper mapper;
    private final IUserService userService;
    private final INoteRepository noteRepository;

    @Override
    @Transactional
    public ResponseDTO findTagsByUser(Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection) {

        ResponseDTO res = Validator.ValidateParams(pageNumber, pageSize, sortBy, sortDirection, Tag.class);
        if (res != null) return res;

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return new ResponseDTO(repository.findAllByUser(getUser(), pageRequest), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseDTO saveTags(List<TagDTO> tagDTOList) {

        if (tagDTOList.isEmpty()) {
            return new ResponseDTO("The list is empty", HttpStatus.BAD_REQUEST);
        }

        List<String> duplicates = findDuplicateNames(tagDTOList);

        if (!duplicates.isEmpty()) {
            return new ResponseDTO("List has duplicate names " + duplicates, HttpStatus.CONFLICT);
        }

        for (TagDTO tagDTO : tagDTOList) {
            ResponseDTO res = ValidateTagDTO(tagDTO);
            if (res != null) return res;
        }

        List<Tag> tags = mapper.toTags(tagDTOList);
        User user = getUser();
        tags.forEach(tag -> tag.setUser(user));

        return new ResponseDTO(mapper.toTagsDTO(repository.saveAll(tags)), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseDTO editTag(Long tagId, TagDTO tagDTO) {

        ResponseDTO res = ValidateTagDTO(tagDTO);
        if (res != null) return res;

        Tag tag = getTag(tagId);

        if (tag == null) {
            return new ResponseDTO("Tag not found", HttpStatus.NOT_FOUND);
        }

        tag.setName(tagDTO.getName());

        return new ResponseDTO(mapper.toTagDTO(repository.save(tag)), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseDTO deleteTag(Long tagId, Boolean forceDelete) {

        Tag tag = getTag(tagId);

        if (tag == null) {
            return new ResponseDTO("Tag not found", HttpStatus.NOT_FOUND);
        }

        List<Note> noteList = noteRepository.findAllByTagsContains(tag);

        if (!noteList.isEmpty()) {
            if (forceDelete) {
                noteList.forEach(note -> note.getTags().remove(tag));
                noteRepository.saveAll(noteList);
            } else {
                return new ResponseDTO("The tag cannot be deleted, it is associated with one or more notes", HttpStatus.CONFLICT);
            }
        }

        repository.deleteById(tagId);
        return new ResponseDTO("Tag successfully deleted", HttpStatus.OK);
    }

    private User getUser() {
        return userService.findByUsername();
    }

    private ResponseDTO ValidateTagDTO(TagDTO tagDTO) {
        if (tagDTO == null || tagDTO.getName() == null || tagDTO.getName().isEmpty() || tagDTO.getName().isBlank()) {
            return new ResponseDTO("Check Request Body: " + tagDTO, HttpStatus.BAD_REQUEST);
        }
        if (repository.existsByName(tagDTO.getName())) {
            return new ResponseDTO("Name already exists: " + tagDTO.getName(), HttpStatus.CONFLICT);
        }
        return null;
    }

    private List<String> findDuplicateNames(List<TagDTO> tagDTOList) {
        Map<String, Integer> nameCount = new HashMap<>();
        Set<String> duplicates = new HashSet<>();
        for (TagDTO tagDTO : tagDTOList) {
            String name = tagDTO.getName();
            nameCount.put(name, nameCount.getOrDefault(name, 0) + 1);
            if (nameCount.get(name) > 1) {
                duplicates.add(name);
            }
        }
        return new ArrayList<>(duplicates);
    }

    private Tag getTag(Long tagId) {
        return repository.findById(tagId).orElse(null);
    }

    @Override
    public List<Tag> findAllById(List<TagDTO> tagDTOList) {
        List<Long> list = List.of(tagDTOList.stream().map(TagDTO::getTagId).toArray(Long[]::new));
        return repository.findAllById(list);
    }

    @Override
    public boolean checkTagsForNullTagId(List<TagDTO> tags) {
        return tags.stream().anyMatch(tag -> tag.getTagId() == null);
    }
}
