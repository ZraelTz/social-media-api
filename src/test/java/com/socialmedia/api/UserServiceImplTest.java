package com.socialmedia.api;

import com.socialmedia.api.core.exception.ApiException;
import com.socialmedia.api.core.exception.NotFoundException;
import com.socialmedia.api.dto.request.UserRegistrationRequest;
import com.socialmedia.api.dto.request.update.UserFollowingUpdateRequest;
import com.socialmedia.api.dto.response.ApiResponse;
import com.socialmedia.api.dto.response.RegistrationResponse;
import com.socialmedia.api.mapper.UserMapper;
import com.socialmedia.api.model.entity.User;
import com.socialmedia.api.model.entity.projection.UserView;
import com.socialmedia.api.model.entity.projection.impl.RestPage;
import com.socialmedia.api.repository.UserRepository;
import com.socialmedia.api.service.AuthenticationService;
import com.socialmedia.api.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserFollowingUpdateRequest followingRequest() {
        UserFollowingUpdateRequest followingRequest = new UserFollowingUpdateRequest();
        followingRequest.setUsername("test");
        followingRequest.setFollow(true);
        return followingRequest;
    }

    private UserRegistrationRequest registrationRequest() {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setEmail("test@test.com");
        registrationRequest.setUsername("test");
        registrationRequest.setPassword("$test123");

        return registrationRequest;
    }

    private User testUser() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setUsername("test");
        testUser.setPassword("$test123");
        testUser.setFollowers(new LinkedHashSet<>());
        testUser.setFollowings(new LinkedHashSet<>());
        return testUser;
    }

    private User authUser() {
        User authUser = new User();
        authUser.setId(2L);
        authUser.setEmail("auth@auth.com");
        authUser.setUsername("authUser");
        authUser.setPassword("$auth123");
        authUser.setFollowers(new LinkedHashSet<>());
        authUser.setFollowings(new LinkedHashSet<>());
        return authUser;
    }

    @Test
    public void registerUser_success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userMapper.createUser(any())).thenReturn(testUser());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(testUser());

        ApiResponse<RegistrationResponse> response = userService.registerUser(registrationRequest());

        assertNotNull(response);
        assertEquals("Registration successful", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testUser().getUsername(), response.getData().getUsername());
        assertEquals(testUser().getEmail(), response.getData().getEmail());

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userMapper, times(1)).createUser(any());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void registerUser_emailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ApiException.class, () -> userService.registerUser(registrationRequest()));

        verify(userRepository, times(1)).existsByEmail(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void registerUser_usernameExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(ApiException.class, () -> userService.registerUser(registrationRequest()));

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void updateFollowing_follow_success() {
        User testUser = testUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        User authUser = authUser();
        when(authService.getAuthUser()).thenReturn(authUser);

        ApiResponse<User> response = userService.updateFollowing(followingRequest());

        assertNotNull(response);
        assertEquals("User followed", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testUser.getUsername(), response.getData().getUsername());

        assertTrue(authUser.getFollowings().stream().anyMatch(user-> user.getUsername().equals(testUser.getUsername())));
        assertTrue(testUser.getFollowers().stream().anyMatch(user-> user.getUsername().equals(authUser.getUsername())));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(authService, times(1)).getAuthUser();
        verify(userRepository, times(2)).save(any());
    }

    @Test
    public void updateFollowing_follow_alreadyFollowed() {
        User testUser = testUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        User authUser = authUser();
        when(authService.getAuthUser()).thenReturn(authUser);

        authUser.addFollowing(testUser);
        testUser.addFollower(authUser);

        ApiResponse<User> response = userService.updateFollowing(followingRequest());

        assertNotNull(response);
        assertEquals("User already followed", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testUser, response.getData());

        assertTrue(authUser.getFollowings().contains(testUser));
        assertTrue(testUser.getFollowers().contains(authUser));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(authService, times(1)).getAuthUser();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateFollowing_unfollow_success() {
        UserFollowingUpdateRequest followingRequest = followingRequest();
        followingRequest.setFollow(false);

        User testUser = testUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        User authUser = authUser();
        when(authService.getAuthUser()).thenReturn(authUser);

        authUser.addFollowing(testUser);
        testUser.addFollower(authUser);

        ApiResponse<User> response = userService.updateFollowing(followingRequest);

        assertNotNull(response);
        assertEquals("User unfollowed", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testUser, response.getData());

        assertFalse(authUser.getFollowings().contains(testUser));
        assertFalse(testUser.getFollowers().contains(authUser));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(authService, times(1)).getAuthUser();
        verify(userRepository, times(2)).save(any());
    }

    @Test
    public void updateFollowing_unfollow_alreadyUnfollowed() {
        UserFollowingUpdateRequest followingUpdateRequest = followingRequest();
        followingUpdateRequest.setFollow(false);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser()));
        when(authService.getAuthUser()).thenReturn(authUser());

        ApiResponse<User> response = userService.updateFollowing(followingUpdateRequest);

        assertNotNull(response);
        assertEquals("User already unfollowed", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testUser().getUsername(), response.getData().getUsername());

        assertFalse(authUser().getFollowings().contains(testUser()));
        assertFalse(testUser().getFollowers().contains(authUser()));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(authService, times(1)).getAuthUser();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateFollowing_usernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateFollowing(followingRequest()));

        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(authService);
    }

    @Test
    public void getFollowings_success() {
        int page = 0;
        int pageSize = 10;

        when(authService.getAuthUser()).thenReturn(authUser());
        when(userMapper.toUserViews(any())).thenReturn(new ArrayList<>());

        ApiResponse<RestPage<UserView>> response = userService.getFollowings(page, pageSize);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(0, response.getData().getTotalElements());

        verify(authService, times(1)).getAuthUser();
        verify(userMapper, times(1)).toUserViews(any());
    }

    @Test
    public void getFollowers_success() {
        int page = 0;
        int pageSize = 10;

        when(authService.getAuthUser()).thenReturn(authUser());
        when(userMapper.toUserViews(any())).thenReturn(new ArrayList<>());

        ApiResponse<RestPage<UserView>> response = userService.getFollowers(page, pageSize);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(0, response.getData().getTotalElements());

        verify(authService, times(1)).getAuthUser();
        verify(userMapper, times(1)).toUserViews(any());
    }
}
