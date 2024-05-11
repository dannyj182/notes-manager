package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.TagMapper;
import com.dannyj182.notesmanager.repository.ITagRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    @Transactional
    public TagDTO saveTag(TagDTO tagDTO) {
        User user = this.getUser(this.getUsername());
        if (tagDTO.getName().isEmpty() || tagDTO.getName().isBlank()) return null;
        Tag tag = mapper.toTag(tagDTO);
        tag.setUser(user);
        return mapper.toTagDTO(repository.save(tag));
    }

    @Override
    @Transactional
    public Page<Tag> findTagByUsername(int page, int elements, String[] sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, elements, sort);
        return repository.findAllByUser_Username(this.getUsername(), pageRequest);
    }

    @Override
    @Transactional
    public boolean deleteById(Long tagId) {
        Optional<Tag> optionalTag = repository.findById(tagId);
        if (optionalTag.isEmpty()) return false;
        repository.deleteById(tagId);
        return true;
    }

    @Override
    public List<Tag> findAllById(List<TagDTO> tagDTOList) {
        List<Long> list = List.of(tagDTOList.stream().map(TagDTO::getTagId).toArray(Long[]::new));
        return repository.findAllById(list);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private User getUser(String userName){
        return userService.findById(userName);
    }
}
