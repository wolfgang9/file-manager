package com.rest.demo.dataModels;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class FileRequest {
    private String fileGroup;
    private MultipartFile [] files;
}
