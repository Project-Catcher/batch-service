package com.catcher.batch.resource;

import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.domain.CommandExecutor;
import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.core.service.CampingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CampingController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class CampingControllerTest {

    @MockBean
    private CatcherFeignService catcherFeignService;

    @MockBean
    private CommandExecutor commandExecutor;

    @MockBean
    private CampingService campingService;


    @Autowired
    private MockMvc mockMvc;

    @DisplayName("SUCCESS : 캠핑 api 응답 200 테스트")
    @Test
    void getCampingDataByFeign() throws Exception {
        CampingApiResponse campingApiResponse = new CampingApiResponse();

        when(catcherFeignService.parseService(new HashMap<>(), CampingApiResponse.class))
                .thenReturn(campingApiResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/camping/feign-batch")
                        .param("page", "1")
                        .param("count", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
