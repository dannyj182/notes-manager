package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Status;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.NoteMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import com.dannyj182.notesmanager.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class NoteService implements INoteService{

    private final INoteRepository repository;
    private final NoteMapper mapper;
    private final IUserService userService;
    private final IStatusService statusService;
    private final ITagService tagService;

    @Override
    @Transactional
    public ResponseDTO saveNote(NoteDTO noteDTO) {

        if (noteDTO.getStatus() == null) {
            return new ResponseDTO("Check Request Body (Status)", HttpStatus.BAD_REQUEST);
        }

        if (this.checkTags(noteDTO)) {
            return new ResponseDTO("Check Request Body (Tags)", HttpStatus.BAD_REQUEST);
        }

        User user = getUser();
        if (user == null) {
            return new ResponseDTO(null, HttpStatus.FORBIDDEN);
        }

        Status status = this.getStatus(noteDTO.getStatus());
        if (status == null) {
            return new ResponseDTO("Status not found", HttpStatus.NOT_FOUND);
        }

        Note note = mapper.toNote(noteDTO);
        note.setUser(user);
        note.setStatus(status);
        this.setTags(noteDTO, note);

        return new ResponseDTO(mapper.toNoteDTO(repository.save(note)), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseDTO findNotesByUser(Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection) {

        ResponseDTO res = Validator.ValidateParams(pageNumber, pageSize, sortBy, sortDirection, Note.class);
        if (res != null) return res;

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return new ResponseDTO(repository.findAllByUser(getUser(), pageRequest), HttpStatus.OK);
    }

    @Override
    @Transactional
    public boolean deleteById(long noteId) {
        Note note = this.getNote(noteId);
        if (note == null || this.isNotUser(note)) return false;
        repository.deleteById(noteId);
        return true;
    }

    @Override
    @Transactional
    public NoteDTO editNote(long noteId, NoteDTO noteDTO) {
        Note note = this.getNote(noteId);
        if (note == null || this.isNotUser(note)) return null;
        this.editNote(noteDTO, note);
        return mapper.toNoteDTO(repository.save(note));
    }

    private Note getNote(long noteId){
        return repository.findById(noteId).orElse(null);
    }

    private boolean isNotUser(Note note){
        return !note.getUser().getUsername().equalsIgnoreCase(getUser().getUsername());
    }

    private User getUser(){
        return userService.findByUsername();
    }

    private Status getStatus(String status){
        return statusService.findById(status).orElse(null);
    }

    private void editNote(NoteDTO noteDTO, Note note){
        if (noteDTO.getTitle() != null) note.setTitle(noteDTO.getTitle());
        if (noteDTO.getDescription() != null) note.setDescription(noteDTO.getDescription());
        this.setStatus(noteDTO, note);
        this.setTags(noteDTO, note);
        note.setModifiedDate(LocalDateTime.now());
    }

    private void setStatus(NoteDTO noteDTO, Note note) {
        if (noteDTO.getStatus() != null){
            Status status = this.getStatus(noteDTO.getStatus());
            if (status != null && !status.getStatus().equals(note.getStatus().getStatus())){
                note.setStatus(status);
            }
        }
    }

    private void setTags(NoteDTO noteDTO, Note note) {
        if (noteDTO.getTags() != null) note.setTags(tagService.findAllById(noteDTO.getTags()));
        else note.setTags(new ArrayList<>());
    }

    private boolean checkTags(NoteDTO noteDTO){
        if (noteDTO.getTags() == null) return false;
        else return tagService.checkTagsForNullTagId(noteDTO.getTags());
    }
}
