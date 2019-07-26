/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class FileDownloadUtil {

    private static final Logger logger = LogManager.getLogger(FileDownloadUtil.class);

    public static void download(HttpServletResponse response, String fileName, String fileContent){

        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (!StringUtils.hasText(mimeType)){
            mimeType = "application/octet-stream";
            logger.debug("Downloadable file " + fileName + " mimetype is not detectable, will take default: " + mimeType);
        }

        logger.debug("Downloadable file " + fileName + " mimetype : " + mimeType);
        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

        byte[] fileContentBytes = fileContent.getBytes(Charset.forName("UTF-8"));
        response.setContentLength(fileContentBytes.length);

        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(fileContentBytes));
        try {
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
