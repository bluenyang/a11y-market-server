package com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.service;

import com.multicampus.gamesungcoding.a11ymarketserver.util.gemini.dto.ProductAnalysisResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class ProductAnalysisService {
    private final ChatClient chatClient;

    public ProductAnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ProductAnalysisResult analysisProductImage(Resource imageResource) {
        String prompt = """
                당신은 쇼핑몰 상품 등록 도우미입니다.
                제공된 상품 이미지를 분석하여 다음 정보를 한국어 및 존댓말로 작성해주세요.
                
                1. 상품에 대한 간단한 한 줄 요약
                2. 사용처(어디에, 어떤 상황에서 사용하는지).
                3. 사용 방법 (영양제라면 복용법, 기계라면 간단한 작동법, 의류라면 세탁 및 관리법 등).
                
                이미지에 텍스트가 있다면 해당 내용을 우선적으로 참고하세요.
                정보가 명확하지 않다면 이미지의 외형을 보고 추론하세요.
                """;

        return chatClient.prompt()
                .user(u -> u
                        .text(prompt)
                        .media(MimeTypeUtils.IMAGE_PNG, imageResource)
                )
                .call()
                .entity(ProductAnalysisResult.class);
    }
}
