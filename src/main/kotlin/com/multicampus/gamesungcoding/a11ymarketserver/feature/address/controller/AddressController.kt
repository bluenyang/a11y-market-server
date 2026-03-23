package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.controller

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.AddressResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.dto.DefaultAddressRequest
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service.AddressService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@RestController
@RequestMapping("/api/v1/users/me")
class AddressController(
    private val addressService: AddressService
) {
    @GetMapping("/address")
    fun getAddressList(
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<AddressResponse> = addressService.getAddressList(userDetails.username)

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    fun insertAddress(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: AddressRequest,
        httpResponse: HttpServletResponse
    ): AddressResponse {
        val result = addressService.insertAddress(userDetails.username, request)
        httpResponse.setHeader(HttpHeaders.LOCATION, "/api/address/${result.addressId}")
        return result
    }

    @PutMapping("/address/{addressId}")
    fun updateAddress(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable addressId: String,
        @Valid @RequestBody request: AddressRequest
    ): AddressResponse =
        addressService.updateAddress(userDetails.username, addressId, request)

    @DeleteMapping("/address/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAddress(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable addressId: String
    ) {
        addressService.deleteAddress(userDetails.username, addressId)
    }

    @GetMapping("/default-address")
    fun getDefaultAddress(
        @AuthenticationPrincipal userDetails: UserDetails
    ): AddressResponse = addressService.getDefaultAddress(userDetails.username)

    @PatchMapping("/default-address")
    fun updateDefaultAddress(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: DefaultAddressRequest
    ): String {
        addressService.setDefaultAddress(
            userDetails.username,
            UUID.fromString(request.addressId)
        )
        return "SUCCESS"
    }
}
