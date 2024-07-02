package org.example.youtube.Service;

import jakarta.validation.constraints.AssertFalse;
import org.example.youtube.dto.channel.ChannelCreateDTO;
import org.example.youtube.dto.channel.ChannelDTO;
import org.example.youtube.entity.ChannelEntity;
import org.example.youtube.entity.ProfileEntity;
import org.example.youtube.exp.AppBadException;
import org.example.youtube.repository.ChannelRepository;
import org.example.youtube.util.SecurityUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;


    public ChannelDTO create(ChannelCreateDTO channel) {
        ChannelEntity entity = new ChannelEntity();
        ProfileEntity moderator = SecurityUtil.getProfile();

        entity.setName(channel.getName());
        entity.setDescription(channel.getDescription());
        entity.setPhotoId(channel.getPhotoId());
        entity.setBannerId(channel.getBannerId());
        channelRepository.save(entity);

        return toDTO(entity);
    }


    public ChannelDTO update(String articleId, ChannelCreateDTO dto) {
        ChannelEntity entity = get(articleId);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPhotoId(dto.getPhotoId());
        entity.setBannerId(dto.getBannerId());
        channelRepository.save(entity);

        return toDTO(entity);
    }


    public ChannelEntity get(String id) {
        return channelRepository.findByIdAndStatusTrue(id).orElseThrow(() -> new AppBadException("Channel not found"));
    }


    public Boolean delete(String id) {
        ChannelEntity entity = get(id);
        if (entity != null && entity.getVisible() != Boolean.FALSE) {
            entity.setVisible(Boolean.FALSE);
            channelRepository.save(entity);
            return true;
        } else
            throw new AppBadException("Channel not found or already deleted");

    }

    public ChannelDTO updateBanner(String articleId, String newBannerId) {
        ChannelEntity entity = get(articleId);
        entity.setBannerId(newBannerId);
        channelRepository.save(entity);

        return toDTO(entity);
    }

    public ChannelDTO updatePhoto(String articleId, String newPhotoId) {
        ChannelEntity entity = get(articleId);
        entity.setBannerId(newPhotoId);
        channelRepository.save(entity);

        return toDTO(entity);
    }


    public ChannelDTO toDTO(ChannelEntity entity) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setBannerId(entity.getBannerId());
        dto.setPhotoId(entity.getPhotoId());
        dto.setOwnerId(entity.getOwnerId());
        return dto;

    }


}






