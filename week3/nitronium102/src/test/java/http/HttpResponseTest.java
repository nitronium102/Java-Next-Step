package http;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 1) 응답 헤더 정보를 Map<String, String>으로 반환
 * 2) forward(): HTML, CSS, JS 파일을 직접 읽어 응답으로 보내는 메서드
 * 3) redirect(): 다른 URL로 리다이렉트하는 메서드
 */
class HttpResponseTest {
    private static final String TEST_DIRECTORY = "src/test/resources/";
    private OutputStream outputStream;
    private File outputFile;
    private String currentTestFileName;

    @BeforeEach
    void setUp() {
        // 테스트 디렉토리가 없으면 생성
        new File(TEST_DIRECTORY).mkdirs();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }

        // 테스트 결과 확인 후 파일 내용 출력 (디버깅용)
        if (outputFile != null && outputFile.exists()) {
            System.out.println("=== " + currentTestFileName + " 내용 ===");
            Files.lines(Paths.get(outputFile.getPath())).forEach(System.out::println);
            System.out.println("===============================");
        }
    }

    @Test
    @DisplayName("HTML 파일을 응답으로 보내는 forward 메서드 테스트")
    void shouldForwardHtmlContentInResponseBody() throws IOException {
        // given
        currentTestFileName = "Http_Forward.txt";
        outputStream = createOutputStream(currentTestFileName);
        HttpResponse response = new HttpResponse(outputStream);

        // when
        response.forward("/index.html");

        // then
        String responseContent = getFileContent(currentTestFileName);
        assertTrue(responseContent.contains("<!DOCTYPE html>") ||
                        responseContent.contains("<html>"),
                "응답에 HTML 콘텐츠가 포함되어야 합니다");
    }

    @Test
    @DisplayName("다른 URL로 리다이렉트하는 메서드 테스트")
    void shouldSetLocationHeaderOnRedirect() throws IOException {
        // given
        currentTestFileName = "Http_Redirect.txt";
        outputStream = createOutputStream(currentTestFileName);
        HttpResponse response = new HttpResponse(outputStream);

        // when
        response.sendRedirect("/index.html");

        // then
        String responseContent = getFileContent(currentTestFileName);
        assertTrue(responseContent.contains("Location: /index.html"),
                "응답 헤더에 리다이렉트 위치가 포함되어야 합니다");
        assertTrue(responseContent.contains("HTTP/1.1 302"),
                "리다이렉트 상태 코드가 포함되어야 합니다");
    }

    @Test
    @DisplayName("응답 헤더에 쿠키 정보 추가 테스트")
    void shouldAddCookieHeaderToResponse() throws IOException {
        // given
        currentTestFileName = "Http_Cookie.txt";
        outputStream = createOutputStream(currentTestFileName);
        HttpResponse response = new HttpResponse(outputStream);

        // when
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");

        // then
        String responseContent = getFileContent(currentTestFileName);
        assertTrue(responseContent.contains("Set-Cookie: logined=true"),
                "응답 헤더에 쿠키 정보가 포함되어야 합니다");
    }

    private OutputStream createOutputStream(String fileName) throws IOException {
        outputFile = new File(TEST_DIRECTORY + fileName);
        return Files.newOutputStream(outputFile.toPath());
    }

    private String getFileContent(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(TEST_DIRECTORY + fileName)));
    }
}
