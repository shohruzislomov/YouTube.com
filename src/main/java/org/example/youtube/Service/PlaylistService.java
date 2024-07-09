package org.example.youtube.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youtube.dto.AttachDTO;
import org.example.youtube.dto.channel.ChannelDTO;
import org.example.youtube.dto.playlist.PlaylistCreateDTO;
import org.example.youtube.dto.playlist.PlaylistDTO;
import org.example.youtube.dto.playlist.PlaylistUpdateDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.example.youtube.entity.PlayListEntity;
import org.example.youtube.entity.ProfileEntity;
import org.example.youtube.enums.PlaylistStatus;
import org.example.youtube.enums.ProfileRole;
import org.example.youtube.exp.AppBadException;
import org.example.youtube.repository.PlaylistRepository;
import org.example.youtube.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final AttachService attachService;

    public PlaylistDTO create(PlaylistCreateDTO dto) {
        PlayListEntity entity = toEntity(dto);
        PlayListEntity saved = playlistRepository.save(entity);
        return toDto(saved);
    }

    public PlaylistDTO update(Long playlistId, PlaylistUpdateDTO dto) {
        isOwner(playlistId);

        PlayListEntity playListEntity = getById(playlistId);
        if (dto.getName() != null) {
            playListEntity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            playListEntity.setDescription(dto.getDescription());
        }

        if (dto.getOrderNumber() != null) {
            playListEntity.setOrderNumber(dto.getOrderNumber());
        }
        PlayListEntity saved = playlistRepository.save(playListEntity);
        return toDto(saved);
    }

    public void updateStatus(Long playlistId, PlaylistStatus status) {
        isAdminOrOwner(playlistId);
        playlistRepository.updateStatus(playlistId, status);
    }

    public void delete(Long playlistId) {
        isAdminOrOwner(playlistId);
        playlistRepository.deleteById(playlistId);
    }

    public Page<PlaylistDTO> getAll(int pageNumber, int pageSize) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        Page<PlayListEntity> entityPage = playlistRepository.findAllBy(pageable);
        List<PlaylistDTO> list = entityPage.getContent()
                .stream()
                .map((PlayListEntity entity) -> {
                    PlaylistDTO fullInfo = toFullInfo(entity);
                    return fullInfo;
                })
                .toList();
        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, (org.springframework.data.domain.Pageable) pageable, totalElements);
    }

    public List<PlaylistDTO> getByUserId(Long userId) {
        return playlistRepository.getByUserId(userId)
                .stream()
                .map(this::toFullInfo)
                .toList();
    }

    public List<PlaylistDTO> getByCurrentUserId() {
        Long userId = Long.valueOf(SecurityUtil.getProfileId());
        return playlistRepository.getByUserId(userId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    public List<PlaylistDTO> getByChanelId(String chanelId) {
        return playlistRepository.findAllByChanelId(chanelId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    public PlayListEntity getByPlaylistId(Long playlistId) {
        return getById(playlistId);
    }

    private PlaylistDTO toShortInfo(PlayListEntity entity) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoCount(playlistRepository.videoCount());
        ///////////// video list

        // create channel
        ChannelDTO chanel = new ChannelDTO();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getName());
        dto.setChanel(chanel);
        return dto;
    }

    private PlaylistDTO toFullInfo(PlayListEntity entity) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());

        // create channel
        ChannelDTO chanel = new ChannelDTO();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getName());
        // create chanel photo
        AttachDTO chanelAttach = attachService.getDTOWithURL(entity.getChanel().getPhotoId());
        chanel.setPhoto(chanelAttach);

        dto.setChanel(chanel);

        // create profile
        ProfileDTO profile = new ProfileDTO();
        profile.setId(Math.toIntExact(entity.getChanel().getProfileId()));
        profile.setName(entity.getChanel().getProfile().getName());
        profile.setSurname(entity.getChanel().getProfile().getSurname());
        // create profile photo
        AttachDTO profileAttach = attachService.getDTOWithURL(entity.getChanel().getProfile().getPhoto());
//        profile.set(profileAttach);

        dto.setProfile(profile);
        return dto;
    }

    private PlayListEntity toEntity(PlaylistCreateDTO dto) {
        PlayListEntity entity = new PlayListEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setChanelId(dto.getChanelId());
        entity.setOrderNumber(dto.getOrderNumber());
        return entity;
    }

    private PlaylistDTO toDto(PlayListEntity entity) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setChanelId(entity.getChanelId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isAdminOrOwner(Long playlistId) {
        ProfileEntity profile = SecurityUtil.getProfile();
        PlayListEntity playListEntity = getById(playlistId);
        if (playListEntity.getChanel().getProfileId().equals(profile.getId()) || !profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("You dont have permission to delete this playlist");
        }
    }

    private void isOwner(Long playlistId) {
        Long profileId = Long.valueOf(SecurityUtil.getProfileId());
        PlayListEntity playListEntity = getById(playlistId);
        if (playListEntity.getChanel().getProfileId() != profileId) {
            throw new AppBadException("You dont have permission to update this playlist");
        }
    }

    public PlayListEntity getById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppBadException("Playlist not found"));
    }
}
