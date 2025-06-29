package util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
public class ResponseUtils {

    /**
     * webapp 디렉토리에서 파일을 읽어 바이트 배열로 반환.
     * 파일이 없을 경우 "File Not Found" 메시지 반환.
     */
    public static byte[] readWebappFile(String url) {
        try {
            return Files.readAllBytes(new File("./webapp" + url).toPath());
        } catch (IOException e) {
            log.warn("File not found: {}", url);
            return "File Not Found".getBytes(StandardCharsets.UTF_8);
        }
    }
}
