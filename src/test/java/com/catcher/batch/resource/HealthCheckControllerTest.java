package com.catcher.batch.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = HealthCheckController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@MockBean(JpaMetamodelMappingContext.class)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Health check 검사가 성공적으로 반환된다")
    @Test
    void health_check_and_receive_ok() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/health")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
         resultActions.andExpect(status().isOk());
    }
}