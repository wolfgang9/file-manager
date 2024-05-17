package com.rest.demo.controllers;

import com.rest.demo.repository.Xfile;
import com.rest.demo.repository.XfileRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class RestDemoController {
    @Autowired
    XfileRepository xFileRepo;

    @PostMapping("/uploader")
    public ResponseEntity uploader(@RequestParam("fileGroup") String fileGroup, @RequestParam("files") MultipartFile [] files) {
        try {
            for(MultipartFile file: files) {
                Xfile x= new Xfile();
                x.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
                x.setFileGroup(fileGroup);
                x.setFiles(file.getBytes());
                x.setId(file.getOriginalFilename()+"_"+file.getSize());
                xFileRepo.save(x);
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("All files saved successfully");
        }  catch(Exception e) {
            return ResponseEntity.internalServerError().body("File not uploaded: "+e.getMessage());
        }
    }

    @GetMapping("/downloader")
    public ResponseEntity downloader(HttpServletResponse response, @RequestParam("fileGroup") String fileGroup) throws IOException {
         List<Xfile> dwnldList = xFileRepo.findByfileGroup(fileGroup);
         if(dwnldList.size() > 1) {
             String zipFileName = "attachment;filename=" + fileGroup + ".zip";
             response.setContentType("application/octet-stream");
             response.setHeader("Content-Disposition", zipFileName);
             response.setStatus(200);
             ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
             for (Xfile xfile : dwnldList) {
                 ZipEntry zipEntry = new ZipEntry(xfile.getFileName());
//            File f=new File("D:/",xfile.getFileName());
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(xfile.getFiles());
                 zipEntry.setSize(xfile.getFiles().length);
                 zipOutputStream.putNextEntry(zipEntry);
                 zipOutputStream.write(xfile.getFiles());
//            fos.close();
             }
             zipOutputStream.closeEntry();
             zipOutputStream.close();
             return ResponseEntity.ok().body("Files downloaded successfully");
         } else {
             HttpHeaders headers = new HttpHeaders();
             headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + dwnldList.getFirst().getFileName());
             ByteArrayResource resource = new ByteArrayResource(dwnldList.getFirst().getFiles());
             return ResponseEntity.ok().headers(headers).contentLength(dwnldList.getFirst().getFiles().length).contentType(MediaType.APPLICATION_OCTET_STREAM)
                     .body(resource);
         }
    }

}
