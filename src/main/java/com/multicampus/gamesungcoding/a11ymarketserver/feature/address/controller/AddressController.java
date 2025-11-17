package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service.AddressService;
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
    public ResponseEntity<List<AddressResponse>> getAddressList(
            HttpSession session
            //@RequestParam String uuid
    ) {
        UUID userId = (UUID) session.getAttribute("userId");
        // UUID userId = UUID.fromString(uuid);
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

    // 기본 배송지 조회
    @GetMapping("/v1/users/me/default-address")
    public ResponseEntity<AddressResponse> getDefaultAddress(
            HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        // 기본 배송지가 없을 때 204 반환
        return addressService.getDefaultAddress(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // 기본 배송지 변경
    @PatchMapping("/v1/users/me/default-address")
    public ResponseEntity<AddressResponse> updateDefaultAddress(
            HttpSession session,
            @Valid @RequestBody AddressRequest request) {

        UUID userId = (UUID) session.getAttribute("userId");

        AddressResponse response = addressService.setDefaultAddress(userId, request);
        return ResponseEntity.ok(response);
    }

}
