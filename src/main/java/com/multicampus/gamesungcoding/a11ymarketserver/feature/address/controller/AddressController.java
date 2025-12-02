package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.DefaultAddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/v1/users/me/address")
    public ResponseEntity<List<AddressResponse>> getAddressList(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                addressService.getAddressList(userDetails.getUsername())
        );
    }

    // 배송지 등록
    @PostMapping("/v1/users/me/address")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AddressResponse> insertAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.insertAddress(userDetails.getUsername(), request);
        String uri = "/api/v1/users/me/address/" + response.addressId();

        return ResponseEntity
                .created(URI.create(uri))
                .body(response);
    }

    // 배송지 정보 수정
    @PutMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(userDetails.getUsername(), addressId, request)
        );
    }

    // 배송지 삭제
    @DeleteMapping("/v1/users/me/address/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String addressId) {

        addressService.deleteAddress(userDetails.getUsername(), addressId);
        return ResponseEntity.noContent().build();
    }

    // 기본 배송지 조회
    @GetMapping("/v1/users/me/default-address")
    public ResponseEntity<AddressResponse> getDefaultAddress(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                addressService.getDefaultAddress(userDetails.getUsername())
        );
    }

    // 기본 배송지 변경
    @PatchMapping("/v1/users/me/default-address")
    public ResponseEntity<String> updateDefaultAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DefaultAddressRequest request) {

        addressService.setDefaultAddress(
                userDetails.getUsername(),
                UUID.fromString(request.addressId()));

        return ResponseEntity.ok("SUCCESS");
    }

}
