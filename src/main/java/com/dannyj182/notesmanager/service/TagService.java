package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.TagMapper;
import com.dannyj182.notesmanager.repository.ITagRepository;
import com.dannyj182.notesmanager.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService implements ITagService{

    private final ITagRepository repository;
    private final TagMapper mapper;
    private final IUserService userService;

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
    public ResponseDTO saveTag(TagDTO tagDTO) {

        ResponseDTO res = ValidateTagDTO(tagDTO);
        if (res != null) return res;

        Tag tag = mapper.toTag(tagDTO);
        User user = getUser();
        tag.setUser(user);

        return new ResponseDTO(mapper.toTagDTO(repository.save(tag)), HttpStatus.CREATED);
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
    public ResponseDTO deleteTag(Long tagId) {

        Tag tag = getTag(tagId);

        if (tag == null) {
            return new ResponseDTO("Tag not found", HttpStatus.NOT_FOUND);
        }

        repository.deleteById(tagId);

        return new ResponseDTO("Tag successfully deleted", HttpStatus.OK);
    }

    private User getUser(){
        return userService.findByUsername();
    }

    private ResponseDTO ValidateTagDTO(TagDTO tagDTO) {
        if (tagDTO == null || tagDTO.getName() == null || tagDTO.getName().isEmpty() || tagDTO.getName().isBlank()) {
            return new ResponseDTO("Check Request Body", HttpStatus.BAD_REQUEST);
        }
        if (repository.existsByName(tagDTO.getName())) {
            return new ResponseDTO("Name already exists", HttpStatus.CONFLICT);
        }
        return null;
    }

    private Tag getTag(Long tagId){
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
