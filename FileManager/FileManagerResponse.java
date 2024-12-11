package com.example.server.utill.FileManager;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileManagerResponse {
    private Integer status;
    private String message;
    private String fileName;
    private String filePath;
}
