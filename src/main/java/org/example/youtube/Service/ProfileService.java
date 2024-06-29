package org.example.youtube.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.youtube.dto.profile.ProfileCreateDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.example.youtube.dto.profile.ProfileUpdateDTO;
import org.example.youtube.entity.ProfileEntity;
import org.example.youtube.enums.ProfileStatus;
import org.example.youtube.exp.AppBadException;
import org.example.youtube.repository.ProfileRepository;
import org.example.youtube.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthService authService;

    public ProfileDTO create(ProfileCreateDTO dto) {
        ProfileEntity entity = new ProfileEntity();


        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setRole(dto.getRole());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));
        entity.setPhoto(dto.getPhoto_id());

        profileRepository.save(entity);
        return toDTO(entity);
    }

    public ProfileDTO update(Integer id, ProfileUpdateDTO dto) {


        ProfileEntity entity = new ProfileEntity();

        entity = get(id);

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());


        profileRepository.save(entity);

        return toDTO(entity);
    }

    public Boolean delete(Integer id) {

        profileRepository.deleteById(id);

        return true;
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            log.error("Profile not found id = {}", id);
            throw new AppBadException("Profile not found");
        });
    }


    public List<ProfileDTO> getAll() {
        Iterable<ProfileEntity> regions = profileRepository.findAll();
        List<ProfileDTO> list = new ArrayList<>();
        for (ProfileEntity region : regions) {
            list.add(toDTO(region));
        }
        return list;
    }


    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setVisible(entity.getVisible());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPhoto(entity.getPhoto());
        dto.setStatus(entity.getStatus());
        return dto;
    }


    public ProfileDTO updateUser(Integer id, ProfileUpdateDTO profile) {
        ProfileEntity entity = new ProfileEntity();

        entity = get(id);

        entity.setName(profile.getName());
        entity.setSurname(profile.getSurname());
        if (profile.getPhotoId() != null) {
            entity.setPhoto(profile.getPhotoId());
        }
        entity.setPassword(profile.getPassword());
        profileRepository.save(entity);

        return toDTO(entity);
    }


    public void updateEmail(Integer id, String newemail) {

        ProfileEntity entity = new ProfileEntity();

        entity = get(id);

        entity.setEmail(newemail);

        entity.setStatus(ProfileStatus.REGISTRATION);

        profileRepository.save(entity);

        authService.sendRegistrationEmail(id, newemail);
    }
}
