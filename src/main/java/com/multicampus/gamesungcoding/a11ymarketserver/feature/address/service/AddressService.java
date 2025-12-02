package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.AddressInfo;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.entity.Addresses;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    // 배송지 목록 조회
    public List<AddressResponse> getAddressList(String userEmail) {
        return addressRepository.findByUser_UserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressResponse insertAddress(String userEmail, AddressRequest req) {

        Addresses address = Addresses.builder()
                .user(this.getUserByEmail(userEmail))
                .addressInfo(
                        AddressInfo.builder()
                                .addressName(req.addressName())
                                .receiverName(req.receiverName())
                                .receiverPhone(req.receiverPhone())
                                .receiverZipcode(req.receiverZipcode())
                                .receiverAddr1(req.receiverAddr1())
                                .receiverAddr2(req.receiverAddr2())
                                .build()
                )
                .isDefault(req.isDefault() != null ? req.isDefault() : false)
                .build();

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    // 배송지 수정
    @Transactional
    public AddressResponse updateAddress(String userEmail, String addressId, AddressRequest dto) {
        // 사용자 소유의 주소인지 확인
        addressRepository.findByAddressIdAndUser_UserEmail(
                        UUID.fromString(addressId),
                        userEmail)
                .orElseThrow(() -> new DataNotFoundException("해당 사용자의 주소를 찾을 수 없습니다."));

        Addresses address = addressRepository.findById(UUID.fromString(addressId))
                .orElseThrow(() -> new DataNotFoundException("해당 주소를 찾을 수 없습니다."));

        address.updateAddrInfo(
                AddressInfo.builder()
                        .addressName(dto.addressName())
                        .receiverName(dto.receiverName())
                        .receiverPhone(dto.receiverPhone())
                        .receiverZipcode(dto.receiverZipcode())
                        .receiverAddr1(dto.receiverAddr1())
                        .receiverAddr2(dto.receiverAddr2())
                        .build()
        );

        if (dto.isDefault() != null && dto.isDefault()) {
            // 기본 배송지로 설정
            this.setDefaultAddressByAddressId(userEmail, UUID.fromString(addressId));
        }

        return AddressResponse.fromEntity(address);
    }

    // 배송지 삭제
    @Transactional
    public void deleteAddress(String userEmail, String addressId) {
        var address = addressRepository.findByAddressIdAndUser_UserEmail(
                        UUID.fromString(addressId),
                        userEmail)
                .orElseThrow(() -> new DataNotFoundException("해당 사용자의 주소를 찾을 수 없습니다."));
        if (address.getIsDefault()) {
            throw new InvalidRequestException("기본 배송지는 삭제할 수 없습니다.");
        }
        addressRepository.delete(address);
    }

    /**
     * Default_Address
     */

    // 기본 배송지 조회
    public AddressResponse getDefaultAddress(String userEmail) {
        return addressRepository.findByUser_UserEmailAndIsDefaultTrue(userEmail)
                .map(AddressResponse::fromEntity)
                .orElseThrow(() -> new DataNotFoundException("기본 배송지가 설정되어 있지 않습니다."));
    }

    // 기본 배송지 변경
    @Transactional
    public void setDefaultAddress(String userEmail, UUID addressId) {
        // 사용자의 기본 배송지 정보가 존재하는지 확인 후 업데이트 또는 생성
        var defaultAddress = addressRepository.findByUser_UserEmailAndIsDefaultTrue(userEmail);

        if (defaultAddress.isPresent()) {
            Addresses currentDefault = defaultAddress.get();
            if (currentDefault.getAddressId().equals(addressId)) {
                // 이미 기본 배송지인 경우 아무 작업도 수행하지 않음
                return;
            }
            // 기존 기본 배송지의 isDefault를 false로 변경
            currentDefault.setDefault(false);
        }

        // 새로운 기본 배송지 설정
        setDefaultAddressByAddressId(userEmail, addressId);
    }

    private void setDefaultAddressByAddressId(String userEmail, UUID addressId) {
        Addresses address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DataNotFoundException("Address not found"));
        if (!address.getUser().getUserEmail().equals(userEmail)) {
            throw new InvalidRequestException("Address does not belong to the user");
        }
        address.setDefault(true);
    }

    private Users getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
}