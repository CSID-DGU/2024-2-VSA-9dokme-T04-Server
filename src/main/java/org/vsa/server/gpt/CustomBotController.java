package org.vsa.server.gpt;

import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.vsa.server.gpt.dto.ChatGPTRequest;
import org.vsa.server.gpt.dto.ChatGPTResponse;
import org.vsa.server.gpt.dto.Message;
import org.vsa.server.gpt.dto.PdfChatRequest;
import org.vsa.server.gpt.entity.QnA;
import org.vsa.server.gpt.repository.QnARepository;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
@RequestMapping("/api/chat")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    private QnARepository qnaRepository;

    @PostMapping("/pdf")
    public String chatWithPdf(@RequestBody PdfChatRequest request) {
        String pdfUrl = request.getUrl();
        String question = request.getQuestion();
        String bookName = request.getBookName();

        try {
            // 1. PDF 파일 다운로드 및 텍스트 추출
            String pdfContent = extractTextFromPdf(pdfUrl);

            // 2. 프롬프팅 메시지
            String systemMessage = String.format(
                    "한국어로 답변을 진행해주세요. '%s' 책의 PDF 내용을 바탕으로만 질문에 답변하세요. 그리고, 질문에 대한 지식이 책의 pdf에 없는 경우 책의 제목을 참고해서 해당 분야의 지식을 참고해서 대답해주세요.:\n%s",
                    bookName,
                    pdfContent
            );
            ChatGPTRequest gptRequest = new ChatGPTRequest(model, question);
            gptRequest.getMessages().add(0, new Message("system", systemMessage));

            // 3. GPT API 호출
            ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

            // 4. 응답 처리
            String answer;
            if (chatGPTResponse != null && chatGPTResponse.getChoices() != null && !chatGPTResponse.getChoices().isEmpty()) {
                answer = chatGPTResponse.getChoices().get(0).getMessage().getContent();
            } else {
                answer = "응답이 반환되지 않았습니다.";
            }

            // 5. QnA 저장
            QnA qna = new QnA();
            qna.setBookName(bookName);
            qna.setUrl(pdfUrl);
            qna.setQuestion(question);
            qna.setAnswer(answer);
            qnaRepository.save(qna);

            return answer;

        } catch (Exception e) {
            return "에러가 발생했습니다: " + e.getMessage();
        }
    }

    private String extractTextFromPdf(String pdfUrl) throws Exception {
        try (InputStream inputStream = new URL(pdfUrl).openStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String extractedText = pdfStripper.getText(document);

//            if (extractedText == null || extractedText.trim().isEmpty()) {
//                // PDFBox로 추출 실패 시 OCR 시도
//                return extractTextWithOCR(document);
//            }
            return extractedText;

        } catch (IOException e) {
            System.err.println("PDF 다운로드 또는 읽기 중 오류 발생: " + e.getMessage());
            throw new Exception("PDF 다운로드 또는 읽기 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("PDFBox에서 텍스트 추출 중 오류 발생: " + e.getMessage());
            throw new Exception("PDFBox에서 텍스트 추출 중 오류 발생: " + e.getMessage());
        }
    }

    private String extractTextWithOCR(PDDocument document) throws Exception {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/share/tessdata"); // Tessdata 경로 설정
        tesseract.setLanguage("kor"); // 한국어 OCR(각자 환경에 다운로드 필요)

        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder ocrResult = new StringBuilder();

        try {
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // 고해상도 렌더링
                String pageText = tesseract.doOCR(image);
                ocrResult.append(pageText).append("\n");
            }
        } catch (IOException e) {
            throw new Exception("OCR 처리 중 오류 발생: " + e.getMessage());
        }
        return ocrResult.toString();
    }
}