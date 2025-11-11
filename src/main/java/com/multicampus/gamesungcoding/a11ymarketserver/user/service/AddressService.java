package com.multicampus.gamesungcoding.a11ymarketserver.user.service;

import com.multicampus.gamesungcoding.a11ymarketserver.user.model.Address;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.user.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    // 배송지 목록 조회
    public List<AddressResponse> getAddressList(UUID userId) {
        return addressRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AddressResponse::from)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressResponse insertAddress(UUID userId, AddressRequest request) {

        Address address = Address.builder()
                .userId(userId)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .receiverZipcode(request.getReceiverZipcode())
                .receiverAddr1(request.getReceiverAddr1())
                .receiverAddr2(request.getReceiverAddr2())
                .build();

        return AddressResponse.from(addressRepository.save(address));
    }

    // 배송지 수정
    @Transactional
    public AddressResponse updateAddress(UUID userId, UUID addressId, AddressRequest request) {
        Address address = addressRepository.findByAddressIdAndUserId(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address ot no found : " + addressId));

        address.updateAddrInfo(request);
        addressRepository.save(address);

        return AddressResponse.from(address);
    }

    // 배송지 삭제
    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        Address address = addressRepository.findByAddressIdAndUserId(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address ot no found : " + addressId));

        addressRepository.delete(address);
    }
}
