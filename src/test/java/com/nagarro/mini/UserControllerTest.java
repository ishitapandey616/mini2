package com.nagarro.mini;

import com.nagarro.mini.controller.UserController;
import com.nagarro.mini.entity.User;
import com.nagarro.mini.pagination.PageInfoDTO;
import com.nagarro.mini.service.UserService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUsers() throws Exception {
        int size = 5; // Provide a valid size for testing
        List<User> users = Collections.singletonList(new User()); // Create a mock list of users

        when(userService.createUsers(anyInt())).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("size", String.valueOf(size)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void testGetUsers() throws Exception {
        String sortType = "Name"; // Provide a valid sort type for testing
        String sortOrder = "even1"; // Provide a valid sort order for testing
        int limit = 5; // Provide a valid limit for testing
        int offset = 0; // Provide a valid offset for testing
        Map<String, Object> response = new HashMap<>(); // Create a mock response map
        response.put("pageInfo",new PageInfoDTO());
      
        List<User> userlist=new ArrayList<User>();
        userlist.add(new User());
        userlist.add(new User());
        userlist.add(new User());
        userlist.add(new User());
        userlist.add(new User());
        response.put("users",userlist);
        
        
        
        when(userService.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sortType", sortType)
                .param("sortOrder", sortOrder)
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users", Matchers.hasSize(5))); // Ensure the "users" array has one item
}
    }

