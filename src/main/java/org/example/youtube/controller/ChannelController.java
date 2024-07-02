package org.example.youtube.controller;

import jakarta.validation.Valid;
import org.example.youtube.Service.ChannelService;
import org.example.youtube.dto.channel.ChannelCreateDTO;
import org.example.youtube.dto.channel.ChannelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;



    @PostMapping("/create")
    public ResponseEntity<ChannelDTO> create(@Valid @RequestBody ChannelCreateDTO channel) {
        return ResponseEntity.ok(channelService.create(channel));
    }////CREATE


    @PutMapping("/update/{id}")
    public ResponseEntity<ChannelDTO> update(@Valid @RequestBody ChannelCreateDTO channel,
                                             @PathVariable("id") String id) {
        return ResponseEntity.ok(channelService.update(id, channel));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        Boolean result = channelService.delete(id);
        return ResponseEntity.ok().body(result);
    }


//    @PreAuthorize("hasRole('PUBLISHER')")
//    @PutMapping("/change_status/{id}")
//    public ResponseEntity<Boolean> changeStatus(@PathVariable("id") Integer id,
//                                                @RequestBody ArticleStatus status) {
//        Boolean result = articleService.changeStatus(id, status);
//        return ResponseEntity.ok().body(result);
//    }

}
