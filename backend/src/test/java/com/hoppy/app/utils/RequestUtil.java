package com.hoppy.app.utils;

import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName    : com.hoppy.app.utility
 * fileName       : RequestUtility
 * author         : Kim
 * date           : 2022-09-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-02        Kim       최초 생성
 */
public class RequestUtil {

    public static ResultActions getRequest(MockMvc mvc, String url) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    public static ResultActions deleteRequest(MockMvc mvc, String url) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    public static ResultActions postRequest(MockMvc mvc, String url, String content) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .post(url)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    public static ResultActions patchRequest(MockMvc mvc, String url, String content) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .patch(url)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    public static void createDocument(ResultActions result, String documentName) throws Exception {
        result.andDo(document(documentName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        )).andDo(print());
    }
}
