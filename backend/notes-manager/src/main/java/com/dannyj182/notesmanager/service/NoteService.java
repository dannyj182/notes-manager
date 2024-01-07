package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Status;
import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.model.mapper.NoteMapper;
import com.dannyj182.notesmanager.repository.INoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NoteService implements INoteService{

    private final INoteRepository repository;
    private final NoteMapper mapper;
    private final IUserService userService;
    private final IStatusService statusService;

    @Override
    public NoteDTO saveNote(NoteDTO noteDTO) {
        User user = this.getUser(noteDTO.getUserId());
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
    public List<NoteDTO> findNotesByUserId(long userId) {
        return mapper.toNotesDTO(repository.findByUser_UserId(userId));
    }

    @Override
    public boolean deleteById(long noteId) {
        if (!repository.existsById(noteId)) return false;
        repository.deleteById(noteId);
        return true;
    }

    @Override
    public NoteDTO editNote(long noteId, NoteDTO noteDTO) {
        Optional<Note> optionalNote = repository.findById(noteId);
        if (optionalNote.isEmpty()) return null;
        Note note = optionalNote.get();
        this.editNote(noteDTO, note);
        return mapper.toNoteDTO(repository.save(note));
    }

    private User getUser(long userId){
        return userService.findById(userId);
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
