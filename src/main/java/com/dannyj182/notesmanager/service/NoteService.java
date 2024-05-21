package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Status;
import com.dannyj182.notesmanager.model.entity.Tag;
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
import java.util.List;

@Service
@AllArgsConstructor
public class NoteService implements INoteService {

    private final INoteRepository repository;
    private final NoteMapper mapper;
    private final IUserService userService;
    private final IStatusService statusService;
    private final ITagService tagService;

    @Override
    @Transactional
    public ResponseDTO findNotesByUser(Long noteId, Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection) {

        if (noteId != null) {
            return repository.findByNoteIdAndUser(noteId, getUser())
                    .map(note -> new ResponseDTO(mapper.toNoteDTO(note), HttpStatus.OK))
                    .orElseGet(() -> new ResponseDTO("Note not found", HttpStatus.NOT_FOUND));
        }

        ResponseDTO res = Validator.ValidateParams(pageNumber, pageSize, sortBy, sortDirection, Note.class);
        if (res != null) return res;

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return new ResponseDTO(repository.findAllByUser(getUser(), pageRequest), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseDTO saveNotes(List<NoteDTO> noteDTOList) {

        if (noteDTOList.isEmpty()) {
            return new ResponseDTO("The list is empty", HttpStatus.BAD_REQUEST);
        } else {
            List<Note> notes = new ArrayList<>();

            User user = getUser();

            for (NoteDTO noteDTO : noteDTOList) {

                ResponseDTO res = validateNoteDTO(noteDTO);
                if (res != null) return res;

                Note note = new Note();

                res = setNote(noteDTO, note);
                if (res != null) return res;

                note.setUser(user);
                notes.add(note);
            }

            return new ResponseDTO(mapper.toNotesDTO(repository.saveAll(notes)), HttpStatus.CREATED);
        }
    }

    @Override
    @Transactional
    public ResponseDTO editNote(Long noteId, NoteDTO noteDTO) {

        ResponseDTO res = validateNoteDTO(noteDTO);
        if (res != null) return res;

        Note note = getNote(noteId);

        res = validateNote(note);
        if (res != null) return res;

        res = setNote(noteDTO, note);
        if (res != null) return res;

        note.setModifiedDate(LocalDateTime.now());

        return new ResponseDTO(mapper.toNoteDTO(repository.save(note)), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseDTO deleteNotes(List<Long> noteIds) {

        List<Note> notes = repository.findAllByNoteIdInAndUser(noteIds, getUser());

        if (notes.isEmpty()) {
            return new ResponseDTO("Notes not found", HttpStatus.NOT_FOUND);
        }

        repository.deleteAll(notes);
        return new ResponseDTO("Notes successfully deleted", HttpStatus.OK);
    }

    private User getUser() {
        return userService.findByUsername();
    }

    private ResponseDTO validateNoteDTO(NoteDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty() || dto.getTitle().isBlank()) {
            return new ResponseDTO("Check Request Body (Title): " + dto, HttpStatus.BAD_REQUEST);
        }
        if (dto.getDescription() == null || dto.getDescription().isEmpty() || dto.getDescription().isBlank()) {
            return new ResponseDTO("Check Request Body (Description): " + dto, HttpStatus.BAD_REQUEST);
        }
        if (dto.getStatus() == null || dto.getStatus().isEmpty() || dto.getStatus().isBlank()) {
            return new ResponseDTO("Check Request Body (Status): " + dto, HttpStatus.BAD_REQUEST);
        }
        if (checkTags(dto)) {
            return new ResponseDTO("Check Request Body (Tags): " + dto, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private boolean checkTags(NoteDTO noteDTO) {
        if (noteDTO.getTags() == null) return false;
        else return tagService.checkTagsForNullTagId(noteDTO.getTags());
    }

    private Note getNote(long noteId) {
        return repository.findById(noteId).orElse(null);
    }

    private ResponseDTO validateNote(Note note) {
        if (note == null) {
            return new ResponseDTO("Note not found", HttpStatus.NOT_FOUND);
        }
        if (isNotUser(note)) {
            return new ResponseDTO(null, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    private boolean isNotUser(Note note) {
        return !note.getUser().getUsername().equalsIgnoreCase(getUser().getUsername());
    }

    private ResponseDTO setNote(NoteDTO noteDTO, Note note) {

        note.setTitle(noteDTO.getTitle());
        note.setDescription(noteDTO.getDescription());

        ResponseDTO res = validateAndSetStatus(noteDTO, note);
        if (res != null) return res;

        res = validateAndSetTags(noteDTO, note);
        return res;
    }

    private ResponseDTO validateAndSetStatus(NoteDTO noteDTO, Note note) {
        Status status = getStatus(noteDTO.getStatus().toLowerCase());
        if (status == null) {
            return new ResponseDTO("Status not found: " + noteDTO.getStatus(), HttpStatus.NOT_FOUND);
        } else {
            note.setStatus(status);
            return null;
        }
    }

    private Status getStatus(String name) {
        return statusService.findByName(name).orElse(null);
    }

    private ResponseDTO validateAndSetTags(NoteDTO noteDTO, Note note) {
        if (noteDTO.getTags() != null) {
            List<Long> tagIds = noteDTO.getTags().stream().map(TagDTO::getTagId).toList();
            List<Tag> tagList = tagService.findAllByTagIdInAndUser(tagIds);
            if (noteDTO.getTags().size() == tagList.size()) {
                note.setTags(tagList);
            } else {
                return new ResponseDTO("Check Request Body (Tags): " + noteDTO.getTags(), HttpStatus.CONFLICT);
            }
        } else {
            note.setTags(new ArrayList<>());
        }
        return null;
    }
}
