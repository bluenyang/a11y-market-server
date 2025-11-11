package com.multicampus.gamesungcoding.a11ymarketserver.address.service;

import com.multicampus.gamesungcoding.a11ymarketserver.address.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.address.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.address.model.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.address.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressResponse insertAddress(UUID userId, AddressRequest dto) {
        Addresses address = Addresses.builder()
                .addressId(UUID.randomUUID())
                .userId(userId)
                .receiverName(dto.getReceiverName())
                .receiverPhone(dto.getReceiverPhone())
                .receiverZipcode(dto.getReceiverZipcode())
                .receiverAddr1(dto.getReceiverAddr1())
                .receiverAddr2(dto.getReceiverAddr2())
                .build();

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    // 배송지 수정
    @Transactional
    public AddressResponse updateAddress(UUID userId, UUID addressId, AddressRequest dto) {
        var address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        address.updateAddrInfo(
                dto.getAddressName(),
                dto.getReceiverName(),
                dto.getReceiverPhone(),
                dto.getReceiverZipcode(),
                dto.getReceiverAddr1(),
                dto.getReceiverAddr2()
        );
        return AddressResponse.fromEntity(address);
    }

    // 배송지 삭제
    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        addressRepository.findByAddressIdAndUserId(addressId, userId)
                .ifPresent(addressRepository::delete);
    }
}
