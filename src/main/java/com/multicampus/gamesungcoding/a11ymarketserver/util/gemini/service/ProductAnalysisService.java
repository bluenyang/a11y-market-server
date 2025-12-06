package com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service;

import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.dto.ProductAnalysisResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductAnalysisService {
    // 이미지 업로드 테스트 이후 적용
    private final ChatClient chatClient;

    public ProductAnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ProductAnalysisResult analysisProductImage(String productName,
                                                      String userDescription,
                                                      List<MultipartFile> images) {
        // 이미지 업로드 테스트 이후 적용
        String prompt = String.format("""
                        당신은 쇼핑몰 상품 등록 도우미입니다.
                        제공된 상품 이미지를 분석하여 다음 정보를 한국어 및 존댓말로 작성해주세요.
                        
                        [분석 지침]
                        1. **판매자의 설명**을 가장 우선적으로 참고하세요.
                        2. 설명에 없는 정보는 **이미지**를 보고 추론하세요.
                        3. 이미지가 여러장일 경우, 모든 이미지를 종합적으로 분석하세요.
                        4. 불확실한 정보는 추측하지 말고 "정보 없음"으로 작성하세요.
                        
                        [상품명]
                        %s
                        
                        [판매자 설명]
                        %s
                        
                        위 내용을 바탕으로 아래 항목을 JSON으로 추출하세요.
                        1. 상품에 대한 간단한 한 줄 요약
                        2. 사용처(어디에, 어떤 상황에서 사용하는지).
                        3. 사용 방법 (영양제라면 복용법, 기계라면 간단한 작동법, 의류라면 세탁 및 관리법 등).
                        """,
                productName,
                (userDescription == null || userDescription.isBlank())
                        ? "정보 없음 (이미지만 참고)"
                        : userDescription
        );


        return chatClient.prompt()
                .user(u -> {
                    u.text(prompt);
                    if (images != null && !images.isEmpty()) {
                        for (var image : images) {
                            var contentType = image.getContentType();
                            if (contentType == null || !contentType.startsWith("image/")) {
                                continue;
                            }
                            u.media(
                                    new Media(
                                            MimeTypeUtils.parseMimeType(contentType),
                                            image.getResource()
                                    )
                            );
                        }
                    }
                })
                .call()
                .entity(ProductAnalysisResult.class);

        // dummy return
        // return new ProductAnalysisResult(
        //         productName,
        //         userDescription,
        //         images.getFirst().getName()
        // );
    }
}
