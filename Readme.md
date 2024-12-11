# FileManager Component README

## Overview

The `FileManager` class provides a set of methods to manage file operations such as uploading, selecting, updating, deleting, and downloading files in a Spring Boot application. It utilizes Base64 encoding for file uploads and downloads, making it suitable for handling image files and other binary data.

## Package Structure

```plaintext
com.example.server.utill
    └── FileManager.java
```

## Dependencies

- **Spring Framework**: For creating the component and handling HTTP responses.
- **Lombok**: For logging and reducing boilerplate code.

## Class Annotations

- `@Slf4j`: Provides a logger for the class.
- `@Component`: Indicates that this class is a Spring-managed component.

## Methods

### 1. Upload File

```java
public ResponseEntity<FileManagerResponse> uploadFile(FileManagerRequest request)
```

- **Description**: Uploads a file encoded in Base64 format.
- **Parameters**:
    - `FileManagerRequest request`: Contains the Base64 string, file path, and optional file name.
- **Returns**: `ResponseEntity<FileManagerResponse>` with status and message.

**Response Codes**:
- `200 OK`: File saved successfully.
- `400 BAD REQUEST`: Invalid Base64 string.
- `409 CONFLICT`: File already exists.
- `500 INTERNAL SERVER ERROR`: Error saving file.

### 2. Select File

```java
public ResponseEntity<FileManagerResponse> selectFile(String filePath)
```

- **Description**: Selects a file based on the provided file path.
- **Parameters**:
    - `String filePath`: The path of the file to select.
- **Returns**: `ResponseEntity<FileManagerResponse>` with file details.

**Response Codes**:
- `200 OK`: File selected successfully.
- `404 NOT FOUND`: File not found.

### 3. Update File

```java
public ResponseEntity<FileManagerResponse> updateFile(FileManagerRequest request)
```

- **Description**: Updates an existing file with a new Base64 encoded string.
- **Parameters**:
    - `FileManagerRequest request`: Contains the Base64 string, file path, and file name.
- **Returns**: `ResponseEntity<FileManagerResponse>` with status and message.

**Response Codes**:
- `200 OK`: File updated successfully.
- `400 BAD REQUEST`: Invalid Base64 string.
- `404 NOT FOUND`: File not found.
- `500 INTERNAL SERVER ERROR`: Error updating file.

### 4. Delete File

```java
public ResponseEntity<FileManagerResponse> deleteFile(String fileName, String filePath)
```

- **Description**: Deletes a specified file.
- **Parameters**:
    - `String fileName`: The name of the file to delete.
    - `String filePath`: The path where the file is located.
- **Returns**: `ResponseEntity<FileManagerResponse>` indicating the result of the deletion.

**Response Codes**:
- `200 OK`: File deleted successfully.
- `404 NOT FOUND`: File not found.
- `500 INTERNAL SERVER ERROR`: Error deleting file.

### 5. Select File (with Base64)

```java
public ResponseEntity<FileManagerResponse> selectFile(String fileName, String filePath)
```

- **Description**: Selects a file and returns its content as a Base64 encoded string.
- **Parameters**:
    - `String fileName`: The name of the file to select.
    - `String filePath`: The path where the file is located.
- **Returns**: `ResponseEntity<FileManagerResponse>` with Base64 encoded content.

**Response Codes**:
- `200 OK`: File found and returned as Base64.
- `404 NOT FOUND`: File not found.
- `500 INTERNAL SERVER ERROR`: Error reading file.

### 6. Download File

```java
public ResponseEntity<byte[]> downloadFile(String fileName, String filePath)
```

- **Description**: Downloads a specified file.
- **Parameters**:
    - `String fileName`: The name of the file to download.
    - `String filePath`: The path where the file is located.
- **Returns**: `ResponseEntity<byte[]>` containing the file bytes.

**Response Codes**:
- `200 OK`: File downloaded successfully.
- `404 NOT FOUND`: File not found.
- `500 INTERNAL SERVER ERROR`: Error reading file.

## Usage

To use the `FileManager` component, simply autowire it into your Spring service or controller:

```java
@Autowired
private FileManager fileManager;
```

You can then call the methods as needed:

```java
ResponseEntity<FileManagerResponse> response = fileManager.uploadFile(request);
```

## Error Handling

The `FileManager` class provides various response codes to indicate the success or failure of operations. Ensure to handle these appropriately in your application to provide a better user experience.

## Conclusion

The `FileManager` component is a robust solution for managing file uploads, retrievals, updates, and deletions in a Spring Boot application. By utilizing Base64 encoding, it simplifies the handling of binary data, making it ideal for applications that require file management capabilities.