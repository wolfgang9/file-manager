package com.rest.demo.dataModels;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadResponse {
    private String status;
    private String message;
}
