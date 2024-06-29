package org.example.youtube.controller;

import lombok.RequiredArgsConstructor;
import org.example.youtube.Service.AttachService;
import org.example.youtube.dto.AttachDTO;
import org.example.youtube.entity.AttachEntity;
import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attach")
public class AttachController {
    private final AttachService attachService;
//    @Value("${server.url}")
//    private String serverUrl;


//    @PostMapping("/upload")
//    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
//        String fileName = attachService.saveToSystem(file);
//        return ResponseEntity.ok().body(fileName);
//    }

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO response = attachService.saveAttach(file);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getById/{id}")
    private ResponseEntity<AttachEntity> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(attachService.getById(id));
    }


    @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        return attachService.download(fileName);
    }


//    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
//    public byte[] open(@PathVariable("fileName") String fileName) {
//        return this.attachService.load(fileName);
//    }

//    @GetMapping(value = "/open_general/{fileName}", produces = MediaType.ALL_VALUE)
//    public byte[] open_general(@PathVariable("fileName") String fileName) {
//        return attachService.open_general(fileName);
//    }


}
