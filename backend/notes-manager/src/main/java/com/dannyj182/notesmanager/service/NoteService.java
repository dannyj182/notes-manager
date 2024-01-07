package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Status;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.NoteMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NoteService implements INoteService{

    private final INoteRepository repository;
    private final NoteMapper mapper;
    private final IUserService userService;
    private final IStatusService statusService;

    @Override
    public NoteDTO saveNote(NoteDTO noteDTO) {
        User user = this.getUser(this.getUsername());
        if (user == null) return null;
        Status status = this.getStatus(noteDTO.getStatus());
        if (status == null) return null;
        Note note = mapper.toNote(noteDTO);
        note.setUser(user);
        note.setStatus(status);
        note.setModificationDate(LocalDateTime.now());
        return mapper.toNoteDTO(repository.save(note));
    }

    @Override
    public List<NoteDTO> findNotesByUserName() {
        return mapper.toNotesDTO(repository.findAllByUser_UserName(this.getUsername()));
    }

    @Override
    public boolean deleteById(long noteId) {
        Note note = this.getNote(noteId);
        if (note == null || !note.getUser().getUserName().equals(this.getUsername())) return false;
        repository.deleteById(noteId);
        return true;
    }

    @Override
    public NoteDTO editNote(long noteId, NoteDTO noteDTO) {
        Note note = this.getNote(noteId);
        if (note == null || !note.getUser().getUserName().equals(this.getUsername())) return null;
        this.editNote(noteDTO, note);
        return mapper.toNoteDTO(repository.save(note));
    }

    private Note getNote(long noteId){
        return repository.findById(noteId).orElse(null);
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private User getUser(String userName){
        return userService.findById(userName);
    }

    private Status getStatus(String status){
        return statusService.findByStatus(status).orElse(null);
    }

    private void editNote(NoteDTO noteDTO, Note note){
        if (noteDTO.getTitle() != null) note.setTitle(noteDTO.getTitle());
        if (noteDTO.getDescription() != null) note.setDescription(noteDTO.getDescription());
        this.setStatus(noteDTO, note);
        note.setModificationDate(LocalDateTime.now());
    }

    private void setStatus(NoteDTO noteDTO, Note note) {
        if (noteDTO.getStatus() != null){
            Status status = this.getStatus(noteDTO.getStatus());
            if (status != null && !status.getStatus().equals(note.getStatus().getStatus())){
                note.setStatus(status);
            }
        }
    }
}
