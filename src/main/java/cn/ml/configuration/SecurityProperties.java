package cn.ml.configuration;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SecurityProperties {
    private String corsPath;
    private List<String> corsAllowedMethods;
    private List<String> corsAllowedHeaders;
    private List<String> corsAllowedOrigins;

    private final static List<String> CORS_ALLOWED_HEADERS = Arrays.asList("*");
    private final static List<String> CORS_ALLOWED_METHODS = Arrays.asList("GET", "POST", "DELETE", "PUT");

    public SecurityProperties() {
        this.corsPath = "/**";
        setDefaultHeadersAndMethods();
    }

    public SecurityProperties(String corsPath) {
        this.corsPath = corsPath;
        setDefaultHeadersAndMethods();
    }

    public void setDefaultHeadersAndMethods() {
        this.corsAllowedHeaders = SecurityProperties.CORS_ALLOWED_HEADERS;
        this.corsAllowedMethods = SecurityProperties.CORS_ALLOWED_METHODS;
    }

    public String getCorsPath() {
        return corsPath;
    }

    public List<String> getCorsAllowedMethods() {
        return corsAllowedMethods;
    }

    public List<String> getCorsAllowedHeaders() {
        return corsAllowedHeaders;
    }

    public List<String> getCorsAllowedOrigins() {
        return corsAllowedOrigins;
    }
}
