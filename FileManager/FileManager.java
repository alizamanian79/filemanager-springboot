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

    // UploadFile method
    public ResponseEntity<FileManagerResponse> uploadFile(FileManagerRequest request) {
        try {
            // Check if the string contains the prefix and extract the format
            String format = "";
            String prefix = "data:image/";
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

            // Set default file path if not provided
            if (request.getFilePath() == null || request.getFilePath().isEmpty()) {
                request.setFilePath("static/uploads"); // Corrected to set file path instead of file name
            }

            // Ensure the directory exists
            File directory = new File(request.getFilePath());
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory
            }

            // Generate a unique file name if not provided
            String fileName = request.getFileName();
            if (fileName == null || fileName.isBlank()) {
                fileName = UUID.randomUUID().toString(); // Generate a unique name
            }

            // Generate the full file path
            String fullFilePath = request.getFilePath() + "/" + fileName + "." + format;
            String filenameSave=fileName + "." + format;
            File file = new File(fullFilePath);

            // Check if the file already exists
            if (file.exists()) {
                FileManagerResponse res = new FileManagerResponse();
                res.setStatus(409); // Conflict status
                res.setMessage("File already exists: " + fileName);
                res.setFileName(null);
                res.setFilePath(null);

                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            }

            // Write the bytes to the file
            try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
                fos.write(decodedBytes);
                fos.flush();
            }

            // Create a response object
            FileManagerResponse res = new FileManagerResponse();
            res.setStatus(200);
            res.setMessage("File saved successfully");
            res.setFileName(filenameSave);
            res.setFilePath(fullFilePath);

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Handle invalid Base64 string
            FileManagerResponse res = new FileManagerResponse();
            res.setStatus(400);
            res.setMessage("Invalid Base64 string");
            res.setFileName(null);
            res.setFilePath(null);

            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            FileManagerResponse res = new FileManagerResponse();
            res.setStatus(500);
            res.setMessage("Error saving file: " + e.getMessage());
            res.setFileName(null);
            res.setFilePath(null);

            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // SelectFile method
    public ResponseEntity<FileManagerResponse> selectFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ResponseEntity<>(new FileManagerResponse(404, "File not found", null, null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new FileManagerResponse(200, "File selected", file.getName(), filePath), HttpStatus.OK);
    }

    // UpdateFile method
    public ResponseEntity<FileManagerResponse> updateFile(FileManagerRequest request) {
        try {
            // Check if the Base64 string starts with the prefix
            String prefix = "data:image/";
            if (request.getBase64().startsWith(prefix)) {
                int endIndex = request.getBase64().indexOf(";", prefix.length());
                if (endIndex != -1) {
                    // Remove the prefix and base64 part
                    request.setBase64(request.getBase64().substring(endIndex + 8)); // Skip over ";base64,"
                }
            }

            // Decode the Base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(request.getBase64());

            // Ensure the directory exists
            File directory = new File(request.getFilePath());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate the full file path
            String fullFilePath = request.getFilePath() + "/" + request.getFileName();
            File file = new File(fullFilePath);

            // Check if the file exists
            if (!file.exists()) {
                return new ResponseEntity<>(new FileManagerResponse(404, "File not found", null, null), HttpStatus.NOT_FOUND);
            }

            // Write the bytes to the file
            try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
                fos.write(decodedBytes);
                fos.flush();
            }

            return new ResponseEntity<>(new FileManagerResponse(200, "File updated successfully", request.getFileName(), fullFilePath), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new FileManagerResponse(400, "Invalid Base64 string: " + e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new FileManagerResponse(500, "Error updating file: " + e.getMessage(), null, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DeleteFile method
    public ResponseEntity<FileManagerResponse> deleteFile(String fileName, String filePath) {
        // Construct the full file path
        String fullFilePath = filePath + "/" + fileName;
        File file = new File(fullFilePath);

        if (!file.exists()) {
            return new ResponseEntity<>(new FileManagerResponse(404, "File not found", null, null), HttpStatus.NOT_FOUND);
        }
        if (file.delete()) {
            return new ResponseEntity<>(new FileManagerResponse(200, "File deleted successfully", null, null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new FileManagerResponse(500, "Error deleting file", null, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Select method (present base64)
    public ResponseEntity<FileManagerResponse> selectFile(String fileName, String filePath) {
        // Construct the full file path
        String fullFilePath = filePath + "/" + fileName;
        File file = new File(fullFilePath);

        if (!file.exists()) {
            return new ResponseEntity<>(new FileManagerResponse(404, "File not found", null, null), HttpStatus.NOT_FOUND);
        }

        try {
            // Read the file into a byte array
            byte[] fileBytes = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileBytes);
            }

            // Encode the byte array to Base64
            String base64File = Base64.getEncoder().encodeToString(fileBytes);

            return new ResponseEntity<>(new FileManagerResponse(200, "File found", base64File, fullFilePath), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new FileManagerResponse(500, "Error reading file: " + e.getMessage(), null, null), HttpStatus.INTERNAL_SERVER_ERROR);
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
