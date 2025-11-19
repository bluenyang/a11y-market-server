package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.DefaultAddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
            @AuthenticationPrincipal Authentication authentication) {

        return ResponseEntity.ok(
                addressService.getAddressList(authentication.getName())
        );
    }

    // 배송지 등록
    @PostMapping("/v1/users/me/address")
    public ResponseEntity<AddressResponse> insertAddress(
            @AuthenticationPrincipal Authentication authentication,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.insertAddress(authentication.getName(), request);
        String uri = "/api/v1/users/me/address/" + response.addressId();

        return ResponseEntity
                .created(URI.create(uri))
                .body(response);
    }

    // 배송지 정보 수정
    @PutMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal Authentication authentication,
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(authentication.getName(), addressId, request)
        );
    }

    // 배송지 삭제
    @DeleteMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal Authentication authentication,
            @PathVariable String addressId) {

        addressService.deleteAddress(authentication.getName(), addressId);
        return ResponseEntity.noContent().build();
    }

    // 기본 배송지 조회
    @GetMapping("/v1/users/me/default-address")
    public ResponseEntity<AddressResponse> getDefaultAddress(
            @AuthenticationPrincipal Authentication authentication) {

        return ResponseEntity.ok(
                addressService.getDefaultAddress(authentication.getName())
        );
    }

    // 기본 배송지 변경
    @PatchMapping("/v1/users/me/default-address")
    public ResponseEntity<String> updateDefaultAddress(
            @AuthenticationPrincipal Authentication authentication,
            @Valid @RequestBody DefaultAddressRequest request) {

        addressService.setDefaultAddress(
                authentication.getName(),
                UUID.fromString(request.addressId()));

        return ResponseEntity.ok("SUCCESS");
    }

}
