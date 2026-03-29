package com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.service

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.Cart
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.entity.CartItems
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartItemRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.repository.CartRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    private val log = LoggerFactory.getLogger(CartService::class.java)

    @Transactional(readOnly = true)
    fun getCartItems(userEmail: String): CartItemListResponse {
        val cart = getCartByUserEmail(userEmail)
        val list = cartItemRepository.findAllByCart(cart).map { CartItemDto.fromEntity(it) }
        val total = list.sumOf { it.quantity * it.productPrice }

        val groupedList = list.groupBy { it.sellerName }
            .map { (sellerName, items) ->
                CartItemListDto(
                    sellerName = sellerName,
                    sellerId = items.first().sellerId,
                    groupTotal = items.sumOf { it.productPrice * it.quantity },
                    items = items
                )
            }

        return CartItemListResponse(groupedList, total)
    }

    @Transactional
    fun addItem(req: CartAddRequest, userEmail: String): CartItemUpdatedResponse {
        val cart = getCartByUserEmail(userEmail)
        val productId = UUID.fromString(req.productId)
        val productProxy = productRepository.getReferenceById(productId)

        val cartItem = cartItemRepository.findByCartAndProduct_ProductId(cart, productId)?.apply {
            changeQuantity(this.quantity + req.quantity)
        } ?: CartItems.builder()
            .cart(cart)
            .product(productProxy)
            .quantity(req.quantity)
            .build()

        return CartItemUpdatedResponse.fromEntity(cartItemRepository.save(cartItem))
    }

    @Transactional
    fun updateQuantity(cartItemId: UUID, quantity: Int, userEmail: String): CartItemUpdatedResponse {
        val cart = getCartByUserEmail(userEmail)

        val cartItem = cartItemRepository.findByIdOrNull(cartItemId)
            ?: throw DataNotFoundException("Cart item not found: $cartItemId")

        if (cartItem.cart.cartId != cart.cartId) {
            throw DataNotFoundException("Cart item does not belong to user: $cartItemId")
        }

        cartItem.changeQuantity(quantity)

        return CartItemUpdatedResponse.fromEntity(cartItemRepository.save(cartItem))
    }

    @Transactional
    fun deleteItems(request: CartItemDeleteRequest, userEmail: String) {
        val itemIds = request.itemIds.map { UUID.fromString(it) }
        val cart = getCartByUserEmail(userEmail)

        val invalidItems = cartItemRepository.findAllById(itemIds)
            .filter { it.cart.cartId != cart.cartId }
            .map { it.cartItemId }

        if (invalidItems.isNotEmpty()) {
            throw DataNotFoundException("Some cart items do not belong to user: $invalidItems")
        }

        cartItemRepository.deleteAllByIdInBatch(itemIds)
    }

    private fun getCartByUserEmail(userEmail: String): Cart {
        val user = userRepository.findByUserEmail(userEmail)
            ?: throw DataNotFoundException("사용자를 찾을 수 없습니다. userEmail=$userEmail")

        // Optional 없이 순수 코틀린 문법으로 처리!
        return cartRepository.findByUser(user) ?: run {
            log.debug("장바구니가 없어 새로 생성합니다. userEmail={}", userEmail)
            cartRepository.save(
                Cart.builder()
                    .user(user)
                    .build()
            )
        }
    }

    @Transactional(readOnly = true)
    fun getCartItemCount(userEmail: String): CartItemCountResponse {
        return CartItemCountResponse(
            cartItemRepository.countByCart(getCartByUserEmail(userEmail))
        )
    }
}