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
import org.example.youtube.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private EmailHistoryService emailHistoryService;
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

    public ProfileDTO update(ProfileUpdateDTO profileUpdateDTO) {
        ProfileEntity entity = SecurityUtil.getProfile();
        Objects.requireNonNull(entity);
        entity.setName(profileUpdateDTO.getName());
        entity.setSurname(profileUpdateDTO.getSurname());
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


    public String updateEmail(String newEmail) {
        ProfileEntity entity = SecurityUtil.getProfile();
        Objects.requireNonNull(entity);

        if (!entity.getEmail().equals(newEmail)) {
            sendRegistrationEmail(entity.getId(), newEmail);
            entity.setTempEmail(newEmail);
            entity.setStatus(ProfileStatus.REGISTRATION);
            entity.setUpdatedDate(LocalDateTime.now());
            profileRepository.save(entity);
            return "To complete your registration, please verify your email";
        }
        return "Email don't changed";
    }

    public void sendRegistrationEmail(Integer profileId, String email) {
        // send email
        String url = "http://localhost:8080/profile/verification/" + profileId;
        String formatText = "<style>\n" +
                "    a:link, a:visited {\n" +
                "        background-color: #f44336;\n" +
                "        color: white;\n" +
                "        padding: 14px 25px;\n" +
                "        text-align: center;\n" +
                "        text-decoration: none;\n" +
                "        display: inline-block;\n" +
                "    }\n" +
                "\n" +
                "    a:hover, a:active {\n" +
                "        background-color: red;\n" +
                "    }\n" +
                "</style>\n" +
                "<div style=\"text-align: center\">\n" +
                "    <h1>Welcome to kun.uz web portal</h1>\n" +
                "    <br>\n" +
                "    <p>Please button lick below to complete registration</p>\n" +
                "    <div style=\"text-align: center\">\n" +
                "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                "    </div>";
        String text = String.format(formatText, url);
        String title = "Complete registration";
        mailSenderService.send(email, title, text);
        emailHistoryService.create(email, title, text); // create history
    }
}
