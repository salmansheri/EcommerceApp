package com.Ecommerce.EcommerceApp.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import com.Ecommerce.EcommerceApp.Dtos.AddressDTO;
import com.Ecommerce.EcommerceApp.Interfaces.AddressService;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;
import com.Ecommerce.EcommerceApp.Models.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AuthUtils authUtils;

    @PostMapping()
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<AddressDTO>(addressService.createAddress(addressDTO), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressDTOs = addressService.getAllAddresses();
        return new ResponseEntity<List<AddressDTO>>(addressDTOs, HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AddressDTO>> getUserAddress() {
        User currentUser = authUtils.loggedInUser();
        List<AddressDTO> addressDTOs = addressService.getUserAddress(currentUser);
        return new ResponseEntity<List<AddressDTO>>(addressDTOs, HttpStatus.OK);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,
            @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO updateAddressDTO = addressService.updateAddressById(addressId, addressDTO);
        return new ResponseEntity<AddressDTO>(updateAddressDTO, HttpStatus.OK);
    }


      @DeleteExchange("/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddressById(@PathVariable Long addressId) {
        AddressDTO deletedAddressDTO = addressService.deleteAddressById(addressId);
        return new ResponseEntity<AddressDTO>(deletedAddressDTO, HttpStatus.OK);
    }

}
