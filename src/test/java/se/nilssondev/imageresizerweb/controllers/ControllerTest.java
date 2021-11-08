package se.nilssondev.imageresizerweb.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {


    @Autowired
    private MockMvc mockMVc;


    @Test
    void index() throws Exception {
        mockMVc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Index has been changed")));
    }

    @Test
    void test1() throws Exception {
        mockMVc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Testing something else")));
    }

}