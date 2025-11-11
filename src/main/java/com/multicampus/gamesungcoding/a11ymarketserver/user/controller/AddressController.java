package com.multicampus.gamesungcoding.a11ymarketserver.user.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.user.service.AddressService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    // 배송지 목록 조회
    @GetMapping("/v1/users/me/address")
    public ResponseEntity<List<AddressResponse>> getAddressList(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        List<AddressResponse> addresses = addressService.getAddressList(userId);
        return ResponseEntity.ok(addresses);
    }

    // 배송지 등록
    @PostMapping("/v1/users/me/address")
    public ResponseEntity<AddressResponse> insertAddress(
            HttpSession session,
            @Valid @RequestBody AddressRequest request) {
        UUID userId = (UUID) session.getAttribute("userId");
        AddressResponse response = addressService.insertAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 배송지 정보 수정
    @PutMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            HttpSession session,
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest request) {
        UUID userId = (UUID) session.getAttribute("userId");
        AddressResponse response = addressService.updateAddress(userId, addressId, request);
        return ResponseEntity.ok(response);
    }

    // 배송지 삭제
    @DeleteMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            HttpSession session,
            @PathVariable UUID addressId) {
        UUID userId = (UUID) session.getAttribute("userId");
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
