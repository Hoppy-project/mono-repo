package com.hoppy.app.health.controller;

import com.hoppy.app.EnableMockMvc;
import com.hoppy.app.meeting.controller.MeetingController;
import com.hoppy.app.response.dto.ResponseDto;
import com.hoppy.app.response.service.ResponseService;
import com.hoppy.app.response.service.SuccessCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResponseService responseService;

    @Test
    void checkHealth() throws Exception {

        SuccessCode responseCode = SuccessCode.HEALTH_CHECK_SUCCESS;
        ResponseEntity<ResponseDto> responseEntity = new ResponseEntity<>(
                ResponseDto.commonResponse(responseCode.getStatus(), responseCode.getMessage()),
                HttpStatus.valueOf(responseCode.getStatus()));

        Mockito.when(responseService.successResult(Mockito.any(SuccessCode.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/health")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("정상 동작 중")))
                .andDo(document("health-check",
                        responseFields(
                                fieldWithPath("status").description("status"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("data")
                        )
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint())
                ));
    }
}
