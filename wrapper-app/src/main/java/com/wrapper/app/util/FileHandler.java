package com.wrapper.app.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileHandler {

    private static final String LOCAL_PATH = "src/main/resources/files/";

    private static final String STUDENTI_FOLDER = "studenti";

    private static final String PROSTORIJE_FOLDER = "prostorije";

    private static final String PREDAVACI_FOLDER = "predavaci";

    public String crateFolder(String folderName) {
        File folder = new File(LOCAL_PATH + folderName);
        folder.mkdir(); // Create the folder

        // Create some files in the folder (optional)
        File file1 = new File(folder, STUDENTI_FOLDER);
        File file2 = new File(folder, PROSTORIJE_FOLDER);
        File file3 = new File(folder, PREDAVACI_FOLDER);

        file1.mkdir();
        file2.mkdir();
        file3.mkdir();

        try {
            Files.copy(Path.of(LOCAL_PATH + "styles.css"), file1.toPath().resolve("styles.css"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(LOCAL_PATH + "styles.css"), file2.toPath().resolve("styles.css"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of(LOCAL_PATH + "styles.css"), file3.toPath().resolve("styles.css"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folder.getAbsolutePath().replace("\\", "/");
    }

    public void zipFolder(String sourceFolderPath, String zipFilePath) {
        try {
            File sourceFolder = new File(LOCAL_PATH + sourceFolderPath);
            FileOutputStream fos = new FileOutputStream(LOCAL_PATH + zipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            zipFile(sourceFolder, sourceFolder.getName(), zipOut);
            zipOut.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
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
