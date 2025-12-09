package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.dto;

import javax.validation.constraints.Min;

public record SellerInquireProductRequest(@Min(0) Integer page,
                                          @Min(10) Integer size) {
}
