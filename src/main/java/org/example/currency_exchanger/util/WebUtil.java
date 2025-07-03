package org.example.currency_exchanger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class WebUtil {

    private static final String MESSAGE_KEY = "message";
    private static final String PARAM_SEPARATOR = "&";
    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final int MAX_PARAM_PAIR_PARTS = 2;

    private final ObjectMapper mapper = new ObjectMapper();

    public static void sendResponse(HttpServletResponse response, Object obj, int status) throws IOException {
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), obj);
    }

    public static void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        Map<String, String> error = Map.of(MESSAGE_KEY, message);
        mapper.writeValue(response.getWriter(), error);
    }

    public Map<String, String> getRequestParameters(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        if (body.isEmpty()) {
            return Collections.emptyMap();
        }

        return Arrays.stream(body.split(PARAM_SEPARATOR))
                .map(param -> param.split(KEY_VALUE_SEPARATOR, MAX_PARAM_PAIR_PARTS))
                .filter(pair -> pair.length == MAX_PARAM_PAIR_PARTS)
                .collect(Collectors.toMap(
                        pair -> decodeUrlParam(pair[0]),
                        pair -> decodeUrlParam(pair[1]),
                        (oldVal, newVal) -> newVal
                ));
    }

    private String decodeUrlParam(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return value;
        }
    }

}
