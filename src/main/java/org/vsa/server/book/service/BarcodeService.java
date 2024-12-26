package org.vsa.server.book.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class BarcodeService {

    public String extractISBN(String imagePath) {
        try {
            // 이미지 파일 읽기
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));

            // 바코드 리더 초기화
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader barcodeReader = new MultiFormatReader();

            // 바코드 인식
            Result result = barcodeReader.decode(bitmap);
            String isbn = result.getText();

            // ISBN 형식 검증
            if (!isbn.matches("97[89]\\d{10}")) {
                throw new RuntimeException("유효한 ISBN 형식이 아닙니다: " + isbn);
            }

            return isbn;
        } catch (IOException | NotFoundException e) {
            throw new RuntimeException("바코드 인식 실패: " + e.getMessage(), e);
        }
    }
}
