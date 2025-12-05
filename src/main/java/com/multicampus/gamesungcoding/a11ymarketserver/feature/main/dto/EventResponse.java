package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MainPageEvents;

public record EventResponse(String eventTitle,
                            String eventDescription,
                            String eventImageUrl,
                            String eventUrl) {

    public static EventResponse fromEntity(MainPageEvents entity) {
        return new EventResponse(
                entity.getEventTitle(),
                entity.getEventDescription(),
                entity.getEventImageUrl(),
                entity.getEventUrl()
        );
    }
}
