package cn.ml.controller;

import cn.ml.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authenticationManager, userService)).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect((result) -> assertTrue(result.getResponse().getContentAsString().contains("\"isLogin\":false")));
    }

    @Test
    void testLogin() throws Exception {
        Map<String, String> usernamePassword = new HashMap<>();
        usernamePassword.put("username", "testUser");
        usernamePassword.put("password", "testPassword");
        when(userService.loadUserByUsername("testUser"))
                .thenReturn(new User("testUser", bCryptPasswordEncoder.encode("testPassword"), Collections.emptyList()));
        when(userService.getUserByName("testUser"))
                .thenReturn(new cn.ml.entity.User(123, "testUser", bCryptPasswordEncoder.encode("testPassword")));

        MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(usernamePassword)))
                .andExpect(status().isOk())
                .andExpect((result) -> assertTrue(result.getResponse().getContentAsString().contains("\"isLogin\":true")))
                .andReturn();
        HttpSession session = mvcResult.getRequest().getSession();

        // check again login status,if logged response's data field have user Object instance Json
        assert session != null;
        mockMvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect((result) -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect((result) -> assertTrue(result.getResponse().getContentAsString().contains("testUser")));
    }

    @Test
    void register() {
    }

    @Test
    void logout() {
    }

}