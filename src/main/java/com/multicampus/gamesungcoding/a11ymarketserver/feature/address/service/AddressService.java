package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.DefaultAddress;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.DefaultAddressRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final DefaultAddressRepository defaultAddressRepository;

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
                .addressName(dto.getAddressName())
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
        Addresses address = addressRepository.findById(addressId)
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

    /**
     * Default_Address
     */

    // 기본 배송지 조회
    public Optional<AddressResponse> getDefaultAddress(UUID userId) {
        return defaultAddressRepository.findByUserId(userId)
                .flatMap(defaultAddr -> addressRepository.findById(defaultAddr.getAddressId()))
                .map(AddressResponse::fromEntity);
    }

    // 기본 배송지 변경
    @Transactional
    public AddressResponse setDefaultAddress(UUID userId, AddressRequest request) {

        AddressResponse response;

        // 1. 기본 배송지 조회
        Optional<DefaultAddress> defaultOpt = defaultAddressRepository.findByUserId(userId);

        if (defaultOpt.isPresent()) { // 기본 배송지 있는 경우

            DefaultAddress defaultAddress = defaultOpt.get();

            // 2. addressId로 addresses 테이블에서 배송지 정보 조회
            Addresses address = addressRepository
                    .findByAddressIdAndUserId(defaultAddress.getAddressId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다"));

            // 3. 배송지 정보 변경
            address.updateAddrInfo(
                    request.getAddressName(),
                    request.getReceiverName(),
                    request.getReceiverPhone(),
                    request.getReceiverZipcode(),
                    request.getReceiverAddr1(),
                    request.getReceiverAddr2()
            );

            response = AddressResponse.fromEntity(address);

        } else { // 기본 배송지 없는 경우
            response = insertAddress(userId, request);

            // 새로 등록한 배송지를 기본 배송지로 지정
            DefaultAddress defaultAddress = DefaultAddress.builder()
                    .userId(userId)
                    .addressId(response.getAddressId())
                    .build();
            defaultAddressRepository.save(defaultAddress);
        }

        return response;


    }

}