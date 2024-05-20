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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService implements ITagService {

    private final ITagRepository repository;
    private final TagMapper mapper;
    private final IUserService userService;
    private final INoteRepository noteRepository;

    @Override
    @Transactional
    public ResponseDTO findTagsByUser(Long tagId, Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection) {

        if (tagId != null) {
            return repository.findByTagIdAndUser(tagId, getUser())
                    .map(tag -> new ResponseDTO(mapper.toTagDTO(tag), HttpStatus.OK))
                    .orElseGet(() -> new ResponseDTO("Tag not found", HttpStatus.NOT_FOUND));
        }

        ResponseDTO res = Validator.ValidateParams(pageNumber, pageSize, sortBy, sortDirection, Tag.class);
        if (res != null) return res;

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return new ResponseDTO(repository.findAllByUser(getUser(), pageRequest), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseDTO saveTags(List<TagDTO> tagDTOList) {

        ResponseDTO res = validateTagDTOList(tagDTOList);
        if (res != null) return res;

        List<Tag> tags = mapper.toTags(tagDTOList);
        User user = getUser();
        tags.forEach(tag -> tag.setUser(user));

        return new ResponseDTO(mapper.toTagsDTO(repository.saveAll(tags)), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseDTO editTags(List<TagDTO> tagDTOList) {

        ResponseDTO res = validateTagDTOList(tagDTOList);
        if (res != null) return res;

        List<Long> ids = tagDTOList.stream().map(TagDTO::getTagId).toList();
        List<Tag> tags = repository.findAllById(ids);

        if (tags.isEmpty()) {
            return new ResponseDTO("Tags not found", HttpStatus.NOT_FOUND);
        }

        Map<Long, TagDTO> tagDTOMap = tagDTOList.stream().collect(Collectors.toMap(TagDTO::getTagId, tagDTO -> tagDTO));

        for (Tag tag : tags) {
            TagDTO tagDTO = tagDTOMap.get(tag.getTagId());
            if (tagDTO != null) {
                tag.setName(tagDTO.getName());
            }
        }

        return new ResponseDTO(mapper.toTagsDTO(repository.saveAll(tags)), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseDTO deleteTags(List<Long> tagIds, Boolean forceDelete) {

        List<Tag> tags = repository.findAllByTagIdInAndUser(tagIds, getUser());

        if (tags.isEmpty()) {
            return new ResponseDTO("Tags not found", HttpStatus.NOT_FOUND);
        }

        tags.forEach(tag -> System.out.println(tag.getName()));

        List<Note> notes = noteRepository.findAllByTagsIn(tags);

        if (!notes.isEmpty()) {
            if (forceDelete) {
                notes.forEach(note -> note.getTags().removeAll(tags));
                noteRepository.saveAll(notes);
            } else {
                return new ResponseDTO("Tags cannot be deleted, they are associated with one or more notes", HttpStatus.CONFLICT);
            }
        }

        repository.deleteAll(tags);
        return new ResponseDTO("Tags successfully deleted", HttpStatus.OK);
    }

    private User getUser() {
        return userService.findByUsername();
    }

    private ResponseDTO validateTagDTOList(List<TagDTO> tagDTOList) {

        if (tagDTOList.isEmpty()) {
            return new ResponseDTO("The list is empty", HttpStatus.BAD_REQUEST);
        }

        for (TagDTO tagDTO : tagDTOList) {
            ResponseDTO res = validateTagDTO(tagDTO);
            if (res != null) return res;
        }

        List<String> nameList = tagDTOList.stream().map(TagDTO::getName).toList();
        List<Tag> tags = repository.findAllByNameIn(nameList);
        nameList = tags.stream().map(Tag::getName).toList();

        if (!nameList.isEmpty()) {
            return new ResponseDTO("The names already exist: " + nameList, HttpStatus.CONFLICT);
        }

        Map<String, List<String>> duplicates = findDuplicateNamesAndIds(tagDTOList);

        List<String> names = duplicates.get("names");
        if (!names.isEmpty()) {
            return new ResponseDTO("List has duplicate names: " + names, HttpStatus.CONFLICT);
        }

        List<String> tagIds = duplicates.get("tagIds");
        if (!tagIds.isEmpty()) {
            return new ResponseDTO("List has duplicate tagIds: " + tagIds, HttpStatus.CONFLICT);
        }

        return null;
    }

    private ResponseDTO validateTagDTO(TagDTO tagDTO) {
        if (tagDTO == null || tagDTO.getName() == null || tagDTO.getName().isEmpty() || tagDTO.getName().isBlank()) {
            return new ResponseDTO("Check Request Body: " + tagDTO, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private static Map<String, List<String>> findDuplicateNamesAndIds(List<TagDTO> tagDTOList) {

        Map<String, Long> nameCount = new HashMap<>();
        Map<Long, Long> tagIdCount = new HashMap<>();
        Set<String> duplicateNames = new HashSet<>();
        Set<Long> duplicateTagIds = new HashSet<>();

        for (TagDTO tagDTO : tagDTOList) {
            String name = tagDTO.getName();
            nameCount.put(name, nameCount.getOrDefault(name, 0L) + 1);
            if (nameCount.get(name) > 1) {
                duplicateNames.add(name);
            }

            Long tagId = tagDTO.getTagId();
            if (tagId != null) {
                tagIdCount.put(tagId, tagIdCount.getOrDefault(tagId, 0L) + 1);
                if (tagIdCount.get(tagId) > 1) {
                    duplicateTagIds.add(tagId);
                }
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("names", new ArrayList<>(duplicateNames));
        result.put("tagIds", duplicateTagIds.stream().map(String::valueOf).collect(Collectors.toList()));

        return result;
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
