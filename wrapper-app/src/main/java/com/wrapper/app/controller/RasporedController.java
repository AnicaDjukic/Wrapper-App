package com.wrapper.app.controller;

import com.wrapper.app.dto.optimizator.MeetingSchedule;
import com.wrapper.app.service.RasporedService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/raspored")
public class RasporedController {

    private final RasporedService service;

    public RasporedController(RasporedService service) {
        this.service = service;
    }

    @PostMapping("/generate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void generate(@PathVariable String id) {
        service.startGenerating(id);
    }

    @PostMapping("/finish/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void finish(@PathVariable String id, @RequestParam("raspored") MultipartFile raspored) {
        service.finish(id, raspored);
    }

//    @PostMapping("/finish/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public void finish(@PathVariable String id, @RequestParam("raspored") MultipartFile raspored) {
//        service.finish(id, raspored);
//    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadPDFResource(@PathVariable String filename) throws IOException {
        Resource file = service.download(filename);
        Path path = file.getFile()
                .toPath();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


}
