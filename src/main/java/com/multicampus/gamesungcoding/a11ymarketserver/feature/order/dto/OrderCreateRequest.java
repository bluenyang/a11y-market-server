package com.multicampus.gamesungcoding.a11ymarketserver.feature.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotNull
    private String addressId;

    @NotNull
    private List<String> orderItemIds;

}
