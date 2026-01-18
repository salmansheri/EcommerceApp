package com.Ecommerce.EcommerceApp.Interfaces;

import java.util.List;

import com.Ecommerce.EcommerceApp.Dtos.AddressDTO;
import com.Ecommerce.EcommerceApp.Models.User;

public interface AddressService {

  
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressesById(Long addressId);

   

    List<AddressDTO> getUserAddress(User currentUser);

    AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO);

    AddressDTO deleteAddressById(Long addressId);
    
}
