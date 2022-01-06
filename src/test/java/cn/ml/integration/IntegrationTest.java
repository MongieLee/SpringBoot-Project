package cn.ml.integration;

import cn.ml.Application;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class IntegrationTest {
    @Inject
    Environment environment;

    @Test
    public void firstIntegrationTest() {
        String port = environment.getProperty("local.server.port");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:" + port + "/auth")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            String string = response.body().string();
            Assertions.assertEquals(200, response.code());
            Assertions.assertTrue(string.contains("用户没有登录"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
