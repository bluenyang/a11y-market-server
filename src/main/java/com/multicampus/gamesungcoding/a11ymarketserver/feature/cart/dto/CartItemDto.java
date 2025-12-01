package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems;

import java.util.UUID;

public record CartItemDto(
        UUID cartItemId,
        UUID cartId,
        UUID productId,
        UUID sellerId,
        String sellerName,
        String productName,
        Integer productPrice,
        String categoryName,
        Integer quantity,
        String productImageUrl) {

    public static CartItemDto fromEntity(CartItems cartItems) {
        var cart = cartItems.getCart();
        var product = cartItems.getProduct();
        var seller = product.getSeller();
        
        return new CartItemDto(
                cartItems.getCartItemId(),
                cart.getCartId(),
                product.getProductId(),
                seller.getSellerId(),
                seller.getSellerName(),
                product.getProductName(),
                product.getProductPrice(),
                product.getCategory().getCategoryName(),
                cartItems.getQuantity(),
                product.getProductImages().isEmpty()
                        ? null : product.getProductImages().getFirst().getImageUrl()
        );
    }
}