package com.catcher.batch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

//@SpringBootTest
@MockBean(JpaMetamodelMappingContext.class)
class BatchApplicationTests {

    @Test
    void contextLoads() {
    }

}
