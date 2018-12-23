package com.zing.boot.house.common.utils;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static String getResourcePath() {
        File file = new File(".");
        String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }

    public static List<String> getImgPaths(String filePath, List<MultipartFile> files) {
        if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
        }
        String newFilePath = filePath;
        List<String> paths = new ArrayList<>();
        files.forEach(file -> {
            File localFile = null;
            if (!file.isEmpty()) {
                try {
                    localFile = saveToLocal(file, newFilePath);
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), newFilePath);
                    paths.add(path);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return paths;
    }

    private static File saveToLocal(MultipartFile file, String filePath) throws IOException {
        File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
        if (!newFile.exists()) {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        Files.write(file.getBytes(), newFile);
        return newFile;
    }

}
