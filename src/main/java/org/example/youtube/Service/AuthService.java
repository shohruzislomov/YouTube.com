package org.example.youtube.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.youtube.dto.auth.LoginDTO;
import org.example.youtube.dto.auth.RegistrationDTO;
import org.example.youtube.dto.profile.ProfileDTO;
import org.example.youtube.entity.ProfileEntity;
import org.example.youtube.enums.LanguageEnum;
import org.example.youtube.enums.ProfileRole;
import org.example.youtube.enums.ProfileStatus;
import org.example.youtube.exp.AppBadException;
import org.example.youtube.repository.ProfileRepository;
import org.example.youtube.util.JWTUtil;
import org.example.youtube.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private ResourceBundleMessageSource resourceBundleMessageSource;



    public String registrationByEmail(RegistrationDTO dto, LanguageEnum lang) {

        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());

        if (optional.isPresent()) {
            log.warn("Email already exists email : {}", dto.getEmail());
            String message = resourceBundleMessageSource.getMessage("email.exists",null,new Locale(lang.name()));
            throw new AppBadException(message);
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.REGISTRATION);

        profileRepository.save(entity);


        // send email

        String url = "http://localhost:8080/auth/verificationByEmail/" + entity.getId();
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

        mailSenderService.send(dto.getEmail(), "Complete registration", text);

        emailHistoryService.checkEmailLimit(dto.getEmail());
        emailHistoryService.crete(entity.getEmail(), text);
        emailHistoryService.isNotExpiredEmail(dto.getEmail());

        return "To complete your registration please verify your email.";

    }

    public String authorizationVerificationByEmail(Integer userId) {
        Optional<ProfileEntity> optional = profileRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new AppBadException("User not found");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadException("Registration not completed");
        }
        if (entity.getCreatedDate().plusMinutes(1).isBefore(LocalDateTime.now())) {
            entity.setCreatedDate(LocalDateTime.now());

            mailSenderService.send(entity.getId().toString(), "Your account has expired", entity.getEmail());

            return "Registration expired, please try again";
        }
        profileRepository.updateStatus(userId, ProfileStatus.ACTIVE);
        return "Success";
    }

    public String registrationResendByEmail(String email) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(email);
        if (optional.isEmpty()) {
            throw new AppBadException("Email not exists");
        }
        ProfileEntity entity = optional.get();

        if (!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadException("Registration not completed");
        }
        emailHistoryService.checkEmailLimit(email);
        sendRegistrationEmail(entity.getId(), email);
        return "To complete your registration please verify your email.";
    }

    public void sendRegistrationEmail(Integer profileId, String email) {
        // send email
        String url = "http://localhost:8080/auth/verificationByEmail/" + profileId;
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
        mailSenderService.send(email, "Complete registration", text);
        emailHistoryService.crete(email, text); // create history
    }



    public ProfileDTO login(LoginDTO dto) {
        Optional<ProfileEntity> dto1 = profileRepository.findByEmailAndPasswordAndVisibleIsTrue(dto.getEmail(), MD5Util.getMD5(dto.getPassword()));
        if (dto1.isEmpty()) {
            throw new AppBadException("Email or password incorrect");
        }
        if (dto1.get().getStatus() != ProfileStatus.ACTIVE) {
            throw new AppBadException("Profile is not active");
        }
        ProfileDTO dto2 = new ProfileDTO();
        dto2.setId(dto1.get().getId());
        dto2.setSurname(dto1.get().getSurname());
        dto2.setName(dto1.get().getName());
        dto2.setRole(dto1.get().getRole());
        dto2.setVisible(dto1.get().getVisible());
        dto2.setPassword(dto1.get().getPassword());
        dto2.setStatus(dto1.get().getStatus());
        dto2.setPhoto(dto1.get().getPhoto());
        dto2.setCreatedDate(dto1.get().getCreatedDate());
        dto2.setEmail(dto1.get().getEmail());
        dto2.setJwt(JWTUtil.encode(dto1.get().getId(), dto1.get().getRole(),dto1.get().getEmail()));
        return dto2;
    }
}


