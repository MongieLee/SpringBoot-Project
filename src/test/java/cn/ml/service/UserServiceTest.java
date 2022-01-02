package cn.ml.service;

import cn.ml.entity.User;
import cn.ml.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        // invoke userService
        // verify userService transmit request to UserMapper

        // given
        when(mockEncoder.encode("testPassword")).thenReturn("testPassword");
        // when
        userService.save("testUser", "testPassword");
        // then
        verify(mockMapper).save("testUser", "testPassword");
    }

    @Test
    public void testGetUserByName() {
        userService.getUserByName("testUser");
        verify(mockMapper).findUserByUsername("testUser");
    }

    @Test
    public void throwExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("testUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound() {
        when(mockMapper.findUserByUsername("testUser"))
                .thenReturn(new User(1, "testUser", "testPassword"));
        UserDetails testUser = userService.loadUserByUsername("testUser");
        Assertions.assertEquals("testUser", testUser.getUsername());
        Assertions.assertEquals("testPassword", testUser.getPassword());
    }
}