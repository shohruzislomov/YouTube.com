package org.example.youtube.Service;


import org.example.youtube.dto.AttachDTO;
import org.example.youtube.entity.AttachEntity;
import org.example.youtube.exp.AppBadException;
import org.example.youtube.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachService {

    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    AttachRepository attachRepository;


    @Value("${attach.upload.url}")
    private String attachUrl;

    public AttachDTO saveAttach(MultipartFile file) {
        try {
            String pathFolder = getYmDString(); // 2024/06/08
            File folder = new File(attachUrl + pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename())); // dasda.asdas.dasd.jpg
            // save to system
            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUrl + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);
            // save to db
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension); // dasdasd-dasdasda-asdasda-asdasd.jpg
            entity.setPath(pathFolder); // 2024/06/08
            entity.setOriginalName(file.getOriginalFilename());
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveToSystem(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename()); // attaches/zari.jpg
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadImage(String fileName) {
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));
        } catch (Exception e) {
            return new byte[0];
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public byte[] load(String attachId) {
        BufferedImage originalImage;
        try {
            // read from db
            AttachEntity entity = get(attachId);
            originalImage = ImageIO.read(new File(attachUrl + entity.getPath() + "/" + attachId));
            // read from system
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, entity.getExtension(), baos);

            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] open_general(String attachId) {
        byte[] data;
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;
            Path file = Paths.get(attachUrl + path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public ResponseEntity download(String attachId) {
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;
            Path file = Paths.get(attachUrl + path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2024/06/08
    }

    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setCreatedData(entity.getCreatedData());
        dto.setExtension(entity.getExtension());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOriginalName());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId());
        return dto;
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new AppBadException("Attach not found"));
    }

    public AttachEntity getById(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new AppBadException("Attach not found"));
    }

    public AttachDTO getDTOWithURL(String attachId) {
        AttachDTO dto = new AttachDTO();
        dto.setId(attachId);
        dto.setUrl(serverUrl + "/attach/open/" + attachId);
        return dto;
    }


}
