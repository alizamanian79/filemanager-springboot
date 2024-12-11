Here's a comprehensive README for your `FileManager` class, which provides an overview of its functionality, usage, and methods:

---

# FileManager

The `FileManager` class is a utility component for managing file uploads, downloads, selections, and deletions in a Spring Boot application. It supports handling files in Base64 format, ensuring file size limits, and providing responses for various file operations.

## Table of Contents
- [Features](#features)
- [Dependencies](#dependencies)
- [Usage](#usage)
- [Methods](#methods)
  - [uploadFile](#uploadfile)
  - [selectFile](#selectfile)
  - [deleteFile](#deletefile)
  - [downloadFile](#downloadfile)
- [Response Structure](#response-structure)
- [Error Handling](#error-handling)
- [License](#license)

## Features
- Upload files in Base64 format with automatic file type detection.
- Limit file size to 5MB.
- Select files and return their content in Base64 format.
- Delete files from the server.
- Download files with appropriate headers for file transfer.

## Dependencies
This component requires the following dependencies:
- Spring Boot
- Lombok

Ensure that you have these dependencies in your `pom.xml` or `build.gradle` file.

## Usage
To use the `FileManager`, simply autowire it into your Spring service or controller:

```java
import com.example.server.utill.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    @Autowired
    private FileManager fileManager;
    
    // Use fileManager methods here
}
```

## Methods

### `uploadFile(FileManagerRequest request)`
Uploads a file from a Base64 encoded string. If the file already exists, it updates the file; otherwise, it creates a new one.

**Parameters:**
- `FileManagerRequest request`: Contains the Base64 string, optional file name, and file path.

**Returns:**
- `FileManagerResponse`: Contains status, message, file name, and file path.

### `selectFile(String filePath)`
Selects a file by its path.

**Parameters:**
- `String filePath`: The path of the file to select.

**Returns:**
- `FileManagerResponse`: Contains status, message, file name, and file path.

### `deleteFile(String fileName, String filePath)`
Deletes a specified file from the server.

**Parameters:**
- `String fileName`: The name of the file to delete.
- `String filePath`: The path where the file is located.

**Returns:**
- `FileManagerResponse`: Contains status and message regarding the deletion.

### `selectFile(String fileName, String filePath)`
Selects a file and returns its content in Base64 format.

**Parameters:**
- `String fileName`: The name of the file to select.
- `String filePath`: The path where the file is located.

**Returns:**
- `FileManagerResponse`: Contains status, message, Base64 file content, and file path.

### `downloadFile(String fileName, String filePath)`
Downloads a file as a byte array.

**Parameters:**
- `String fileName`: The name of the file to download.
- `String filePath`: The path where the file is located.

**Returns:**
- `ResponseEntity<byte[]>`: Contains the file bytes and HTTP headers for the download.

## Response Structure
The responses from the `FileManager` methods are encapsulated in the `FileManagerResponse` class, which includes:
- `int status`: HTTP status code.
- `String message`: Description of the operation result.
- `String fileName`: Name of the file involved in the operation (if applicable).
- `String filePath`: Path of the file involved in the operation (if applicable).

## Error Handling
The `FileManager` handles various errors, including:
- Invalid Base64 strings.
- File size exceeding 5MB.
- File not found during selection or deletion.
- IO exceptions during file operations.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

Feel free to customize any sections as needed or add additional information relevant to your project!