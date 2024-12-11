package com.example.server.utill.FileManager;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileManagerRequest {
        @Lob
        private String base64;
        private String fileName;
        private String filePath;

}
