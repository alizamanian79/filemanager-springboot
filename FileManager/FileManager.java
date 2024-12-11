package com.example.server.utill.FileManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class FileManager {

    public FileManagerResponse uploadFile(FileManagerRequest request) {
        try {
            // Check if the Base64 string contains a prefix and extract the format
            String format = "";
            String prefix = "data:";
            int startIndex = request.getBase64().indexOf(prefix);
            if (startIndex != -1) {
                int endIndex = request.getBase64().indexOf(";", startIndex);
                if (endIndex != -1) {
                    format = request.getBase64().substring(startIndex + prefix.length(), endIndex);
                    request.setBase64(request.getBase64().substring(endIndex + 8)); // Skip over ";base64,"
                }
            }

            // Decode the Base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(request.getBase64());

            // Check file size (5MB limit)
            if (decodedBytes.length > 5 * 1024 * 1024) { // 5MB in bytes
                return new FileManagerResponse(400, "File size exceeds 5MB limit", null, null);
            }

            // Set default file path if not provided
            if (request.getFilePath() == null || request.getFilePath().isEmpty()) {
                request.setFilePath("static/uploads");
            }

            // Ensure the directory exists
            File directory = new File(request.getFilePath());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique file name if not provided
            String fileName = request.getFileName();
            if (fileName == null || fileName.isBlank()) {
                fileName = UUID.randomUUID().toString();
            }

            // Determine file extension based on format
            String fileExtension = "";
            if (!format.isEmpty()) {
                String[] formatParts = format.split("/");
                if (formatParts.length > 1) {
                    fileExtension = formatParts[1]; // Get the subtype (e.g., pdf, png)
                }
            }

            // If no extension was found, default to .bin
            if (fileExtension.isEmpty()) {
                fileExtension = "bin"; // Default extension if none provided
            }

            // Generate the full file path
            String fullFilePath = request.getFilePath() + "/" + fileName + "." + fileExtension;
            File file = new File(fullFilePath);

            // Check if the file already exists
            if (file.exists()) {
                // Update the file
                try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
                    fos.write(decodedBytes);
                    fos.flush();
                }
                return new FileManagerResponse(200, "File updated successfully", fileName + "." + fileExtension, fullFilePath);
            } else {
                // Upload the file
                try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
                    fos.write(decodedBytes);
                    fos.flush();
                }
                return new FileManagerResponse(200, "File saved successfully", fileName + "." + fileExtension, fullFilePath);
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid Base64 string
            return new FileManagerResponse(400, "Invalid Base64 string: " + e.getMessage(), null, null);
        } catch (IOException e) {
            return new FileManagerResponse(500, "Error saving file: " + e.getMessage(), null, null);
        }
    }

    // SelectFile method
    public FileManagerResponse selectFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new FileManagerResponse(404, "File not found", null, null);
        }
        return new FileManagerResponse(200, "File selected", file.getName(), filePath);
    }


    // DeleteFile method
    public FileManagerResponse deleteFile(String fileName, String filePath) {
        // Construct the full file path
        String fullFilePath = filePath + "/" + fileName;
        File file = new File(fullFilePath);

        if (!file.exists()) {
            return new FileManagerResponse(404, "File not found", null, null);
        }
        if (file.delete()) {
            return new FileManagerResponse(200, "File deleted successfully", null, null);
        } else {
            return new FileManagerResponse(500, "Error deleting file", null, null);
        }
    }



    // Select method (present base64)
    public FileManagerResponse selectFile(String fileName, String filePath) {
        // Construct the full file path
        String fullFilePath = filePath + "/" + fileName;
        File file = new File(fullFilePath);

        if (!file.exists()) {
            return new FileManagerResponse(404, "File not found", null, null);
        }

        try {
            // Read the file into a byte array
            byte[] fileBytes = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileBytes);
            }

            // Encode the byte array to Base64
            String base64File = Base64.getEncoder().encodeToString(fileBytes);

            return new FileManagerResponse(200, "File found", base64File, fullFilePath);
        } catch (IOException e) {
            return new FileManagerResponse(500, "Error reading file: " + e.getMessage(), null, null);
        }
    }


    // DownloadFile method (present file)
    public ResponseEntity<byte[]> downloadFile(String fileName, String filePath) {
        // Construct the full file path
        String fullFilePath = filePath + "/" + fileName;
        File file = new File(fullFilePath);

        if (!file.exists()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        try {
            // Read the file into a byte array
            byte[] fileBytes = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileBytes);
            }

            // Set the response headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(fileName)
                    .build());
            headers.setContentLength(fileBytes.length);

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
