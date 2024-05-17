package com.rest.demo.controllers;

import com.rest.demo.repository.Xfile;
import com.rest.demo.repository.XfileRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
            return ResponseEntity.ok().body("All files saved successfully");
        }  catch(Exception e) {
            return ResponseEntity.internalServerError().body("File not uploaded: "+e.getMessage());
        }
    }

    @GetMapping("/downloader")
    public void downloader(@RequestParam("fileGroup") String fileGroup, HttpServletResponse response) throws IOException {
         List<Xfile> dwnldList = new ArrayList<>();
         dwnldList = xFileRepo.findByfileGroup(fileGroup);
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        InputStream inputStream = null;
        for(Xfile xfile: dwnldList) {
            ZipEntry zipEntry = new ZipEntry(xfile.getFileName());
            File f=new File("D:/",xfile.getFileName());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(xfile.getFiles());
            inputStream = new FileInputStream(f);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(xfile.getFiles());
            fos.close();
        }
        zipOutputStream.closeEntry();
        zipOutputStream.close();
        assert inputStream != null;
        inputStream.close();
        response.setContentType("application/zip");
        response.setStatus(201);
        String zipFileName = "attachment; filename="+fileGroup+".zip";
        response.setHeader("Content-Disposition", "attachment; filename=example.zip");
//        response.setHeader("Content-Disposition", zipFileName);
//        response.addHeader("Pragma", "no-cache");
//        response.addHeader("Expires", "0");

//        return ResponseEntity.ok(response);
    }

}
