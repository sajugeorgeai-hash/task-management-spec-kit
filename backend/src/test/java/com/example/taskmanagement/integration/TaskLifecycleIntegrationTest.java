package com.example.taskmanagement.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskLifecycleIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void createViewUpdateSearchFilterPageAndDeleteLifecycle() throws Exception {
        String body="{\"title\":\"Integration task\",\"description\":\"Test task\",\"status\":\"TODO\",\"priority\":\"HIGH\"}";
        String response=mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.title").value("Integration task"))
            .andExpect(jsonPath("$.priority").value("HIGH")).andReturn().getResponse().getContentAsString();
        JsonNode task=mapper.readTree(response); String id=task.get("id").asText();
        mvc.perform(get("/api/tasks/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Integration task"));
        mvc.perform(put("/api/tasks/"+id).contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"Updated task\",\"description\":\"Updated\",\"status\":\"IN_PROGRESS\",\"priority\":\"MEDIUM\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Updated task"));
        mvc.perform(get("/api/tasks").param("search","Updated").param("status","IN_PROGRESS").param("priority","MEDIUM").param("page","0").param("size","10"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.content",hasSize(greaterThanOrEqualTo(1))));
        mvc.perform(delete("/api/tasks/"+id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tasks/"+id)).andExpect(status().isNotFound());
    }

    @Test void rejectsInvalidInputAndReportsFieldErrors() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"\",\"status\":\"UNKNOWN\",\"priority\":\"UNKNOWN\"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fieldErrors").exists());
        mvc.perform(get("/api/tasks/00000000-0000-0000-0000-000000000000"))
            .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        mvc.perform(get("/api/tasks").param("page","-1").param("size","101"))
            .andExpect(status().isBadRequest());
    }
}
