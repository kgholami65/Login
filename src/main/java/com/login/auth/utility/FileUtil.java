package com.login.auth.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;




@Slf4j
public class FileUtil {

    private static String FILEPATH = "C:\\spring\\profiles\\";


    public void downloadFile(String userName, HttpServletResponse response, String fileName) throws IOException {
        File file = new File(FILEPATH + userName + "\\" + fileName);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType == null)
            mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        inputStream.close();
    }



    public boolean uploadFile(String userName, String fileName, MultipartFile multipartFile)  {
        Path path = Paths.get(FILEPATH + userName);
        if(!Files.exists(path))
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("error while uploading the file");
            }
        try (InputStream inputStream = multipartFile.getInputStream();
        OutputStream outputStream = new FileOutputStream(FILEPATH + userName + "\\" + fileName)) {
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("error in uploading file");
            return false;
        }
        return true;
    }

}
