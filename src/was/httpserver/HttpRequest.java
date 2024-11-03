package was.httpserver;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;

/** HttpRequest 클래스를 만드는 이유
 * 브라우저가 보낸 httpMessage는 같은 구조를 갖고 있다.
 * 서버에서는 httpMessage를 socket의 stream으로 받는다.
 * 이 Stream 데이터를 httpMessage 구조에 맞춰 데이터를 파싱한다.
 * 각 파싱된 데이터를 활용한다.
 * 예를 들어 시작라인의 요청 메서드를 파싱해서 쓸 수 있도록 만드는 것이다.
 * - 그렇다면 뭐가 필요할까?
 * 클라이언트가 보낸 HttpRequestMessage byte 데이터 객첵가 필요 -> BufferReader 객체를 쓴다.
 * */
public class HttpRequest {
    private String method;
    private String path;
    private final Map<String,String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();


    /**
     * 객체 생성시 초기화까지 진행함
     * HttpMessage 구조에 맞춰 순서대로 파싱을 진행해야 함
     */
    public HttpRequest(BufferedReader reader) throws IOException {
        // 1.start line 파싱 -> start line에 path va
        parseRequestLine(reader);

        // 2.header 파싱
        parseHeaders(reader);
        // 3.message body 파싱
    }

    private void parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();

        if(requestLine == null){
            throw new IOException("EOF: No request line received");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Invalid request line: " + requestLine);
        }

        method = parts[0];
        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0];

        if (pathParts.length > 1) {
            parseQueryParameters(pathParts[1]);
        }
    }

    private void parseQueryParameters(String queryString) {
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            String key = URLDecoder.decode(keyValue[0], UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }

    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(":");
            // trim() 앞 뒤에 공백 제거
            headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String name) {
        return queryParameters.get(name);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                ", headers=" + headers +
                '}';
    }
}
