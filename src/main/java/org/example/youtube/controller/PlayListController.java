package org.example.youtube.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.youtube.Service.PlaylistService;
import org.example.youtube.dto.playlist.PlaylistCreateDTO;
import org.example.youtube.dto.playlist.PlaylistDTO;
import org.example.youtube.dto.playlist.PlaylistUpdateDTO;
import org.example.youtube.entity.PlayListEntity;
import org.example.youtube.enums.PlaylistStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlayListController {
    private final PlaylistService playListService;

    @PostMapping("/create")
    public ResponseEntity<PlaylistDTO> create(@Valid @RequestBody PlaylistCreateDTO dto) {
        PlaylistDTO response = playListService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{playlistId}")
    public ResponseEntity<PlaylistDTO> update(@PathVariable Long playlistId,
                                              @Valid @RequestBody PlaylistUpdateDTO dto) {
        PlaylistDTO response = playListService.update(playlistId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/status/{playlistId}")
    public ResponseEntity<String> updateStatus(@PathVariable Long playlistId,
                                               @RequestParam PlaylistStatus status) {
        playListService.updateStatus(playlistId, status);
        return ResponseEntity.status(HttpStatus.OK).body("playlist status updated");
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<String> delete(@PathVariable Long playlistId) {
        playListService.delete(playlistId);
        return ResponseEntity.status(HttpStatus.OK).body("playlist deleted");
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<PlaylistDTO>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                    @RequestParam(defaultValue = "5") int pageSize) {
        Page<PlaylistDTO> response = playListService.getAll(pageNumber-1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<List<PlaylistDTO>> getByUserId(@PathVariable Long userId) {
        List<PlaylistDTO> response = playListService.getByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<List<PlaylistDTO>> getByCurrentUserId() {
        List<PlaylistDTO> response = playListService.getByCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byChanelId/{chanelId}")
    public ResponseEntity<List<PlaylistDTO>> getByChanelId(@PathVariable String chanelId) {
        List<PlaylistDTO> response = playListService.getByChanelId(chanelId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byId")
    public ResponseEntity<PlayListEntity> getByPlaylistId(@RequestParam Long playlistId) {
        PlayListEntity response = playListService.getByPlaylistId(playlistId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
