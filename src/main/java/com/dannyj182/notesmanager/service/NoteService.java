package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Status;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.NoteMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public NoteDTO saveNote(NoteDTO noteDTO) {
        User user = this.getUser(this.getUsername());
        Status status = this.getStatus(noteDTO.getStatus());
        if (status == null) return null;
        Note note = mapper.toNote(noteDTO);
        note.setUser(user);
        note.setStatus(status);
        this.setTags(noteDTO, note);
        return mapper.toNoteDTO(repository.save(note));
    }

    @Override
    @Transactional
    public Page<Note> findNotesByUsername(int page, int elements, String[] sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, elements, sort);
        return repository.findAllByUser_Username(this.getUsername(), pageRequest);
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
        return !note.getUser().getUsername().equalsIgnoreCase(this.getUsername());
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private User getUser(String userName){
        return userService.findById(userName);
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
    }
}
