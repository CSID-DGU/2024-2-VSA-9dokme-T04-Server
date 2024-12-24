package org.vsa.server.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.vsa.server.gpt.dto.ChatGPTResponse;
import org.vsa.server.gpt.dto.Message;
import org.vsa.server.gpt.dto.PdfChatRequest;
import org.vsa.server.gpt.entity.QnA;
import org.vsa.server.gpt.repository.QnARepository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CustomBotController.class)
class CustomBotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private QnARepository qnaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testChatWithPdf() throws Exception {
        // Given: Mock 요청 데이터
        PdfChatRequest request = new PdfChatRequest();
        request.setUrl("http://example.com/sample.pdf");
        request.setQuestion("책에 관련된 질문입니다.");
        request.setBookName("샘플 책 제목");

        // Mock GPT 응답 데이터
        ChatGPTResponse mockResponse = new ChatGPTResponse();
        ChatGPTResponse.Choice mockChoice = new ChatGPTResponse.Choice();
        Message mockMessage = new Message("assistant", "모의 GPT 응답입니다.");
        mockChoice.setMessage(mockMessage);
        mockResponse.setChoices(Collections.singletonList(mockChoice));

        Mockito.when(restTemplate.postForObject(any(String.class), any(), any()))
                .thenReturn(mockResponse);

        // Mock QnA 저장
        Mockito.when(qnaRepository.save(any(QnA.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When: 요청 전송
        mockMvc.perform(post("/api/chat/pdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then: 응답 상태 및 내용 검증
                .andExpect(status().isOk())
                .andExpect(content().string("모의 GPT 응답입니다."));

        // Verify: QnA 저장 확인
        Mockito.verify(qnaRepository).save(any(QnA.class));
    }
}
