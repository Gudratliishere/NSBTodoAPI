package com.gudratli.nsbtodoapi.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil
{
    public static final String  CV_FILE_DIRECTORY = "C:\\Users\\Dunay Gudratli\\Documents\\NSBUpload\\CV\\";
    public static final String  ATTACHMENT_FILE_DIRECTORY = "C:\\Users\\Dunay Gudratli\\Documents\\NSBUpload\\Document\\";
    public static final String  RESULT_FILE_DIRECTORY = "C:\\Users\\Dunay Gudratli\\Documents\\NSBUpload\\Result\\";

    public static void saveFile (String uploadDirectory, String fileName, MultipartFile multipartFile)
            throws IOException
    {
        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath))
            Files.createDirectory(uploadPath);

        try (InputStream inputStream = multipartFile.getInputStream())
        {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex)
        {
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }
}
