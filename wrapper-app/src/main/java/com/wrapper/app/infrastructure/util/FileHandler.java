package com.wrapper.app.infrastructure.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileHandler {

    private static final String BASE_PATH = "src/main/resources/files/";

    private static final String STUDENTI_FOLDER = "studenti";

    private static final String PROSTORIJE_FOLDER = "prostorije";

    private static final String PREDAVACI_FOLDER = "predavaci";

    public String createFiles(String folderName) {
        File folder = new File(BASE_PATH + folderName);
        folder.mkdir(); // Create the folder

        // Create some files in the folder (optional)
        File file1 = new File(folder, STUDENTI_FOLDER);
        File file2 = new File(folder, PROSTORIJE_FOLDER);
        File file3 = new File(folder, PREDAVACI_FOLDER);

        file1.mkdir();
        file2.mkdir();
        file3.mkdir();

        try {
            Files.copy(Path.of(BASE_PATH + "styles.css"), file1.toPath().resolve("styles.css"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(BASE_PATH + "styles.css"), file2.toPath().resolve("styles.css"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(BASE_PATH + "styles.css"), file3.toPath().resolve("styles.css"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folder.getAbsolutePath().replace("\\", File.separator);
    }

    public String zipFolder(String sourceFolderName, String zipFolderName) {
        try {
            File sourceFolder = new File(BASE_PATH + sourceFolderName);
            FileOutputStream fos = new FileOutputStream(BASE_PATH + zipFolderName);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            zipFile(sourceFolder, sourceFolder.getName(), zipOut);
            zipOut.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BASE_PATH + zipFolderName;
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith(File.separator)) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + File.separator));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
