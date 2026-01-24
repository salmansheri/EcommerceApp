package com.Ecommerce.EcommerceApp.Services;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.Ecommerce.EcommerceApp.Dtos.AddressDTO;
import com.Ecommerce.EcommerceApp.Exceptions.ApiException;
import com.Ecommerce.EcommerceApp.Exceptions.ResourceNotFoundException;
import com.Ecommerce.EcommerceApp.Interfaces.AddressService;
import com.Ecommerce.EcommerceApp.Lib.Utils.AuthUtils;
import com.Ecommerce.EcommerceApp.Mappers.AddressMapper;
import com.Ecommerce.EcommerceApp.Models.Address;
import com.Ecommerce.EcommerceApp.Models.User;
import com.Ecommerce.EcommerceApp.Repositories.AddressRepository;
import com.Ecommerce.EcommerceApp.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    private final AuthUtils authUtils;

    @Override
    @CacheEvict(value = "ecommerce::addressList", allEntries = true)
    public @Nullable AddressDTO createAddress(AddressDTO addressDTO) {

        User user = authUtils.loggedInUser();

        Address address = addressMapper.toEntity(addressDTO);

        List<Address> addressList = user.getAddresses();

        addressList.add(address);

        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return addressMapper.toDto(savedAddress);

    }

    @Override
    @Cacheable(value = "ecommerce::addressList")
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        if (addresses.isEmpty()) {
            throw new ApiException("No Addresses Created Yet!. Please Create the address and try again!");
        }

        return addressMapper.toDTOList(addresses);
    }

    @Override
    @Cacheable(value = "ecommerce::addressById", key = "#addressId")
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));
        return addressMapper.toDto(address);
    }

    @Override
    public List<AddressDTO> getUserAddress(User user) {

        List<Address> userAddresses = user.getAddresses();

        return addressMapper.toDTOList(userAddresses);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "ecommerce::addressList", allEntries = true),
            @CacheEvict(value = "ecommerce::addressById", key = "#addressId")

    })
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
        User currentUser = authUtils.loggedInUser();

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

        if (existingAddress.getUser().getUserId() != currentUser.getUserId()) {
            throw new ApiException("you are Not allowed to update this address");

        }

        existingAddress.setAddressId(addressId);
        addressMapper.updateAddressFromDto(addressDTO, existingAddress);

        Address updatedAddress = addressRepository.save(existingAddress); 

        User user = existingAddress.getUser(); 

        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId)); 

        user.getAddresses().add(updatedAddress); 
        userRepository.save(user);

        return addressMapper.toDto(updatedAddress);

    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "ecommerce::addressList", allEntries = true),
            @CacheEvict(value = "ecommerce::addressById", key = "#addressId")

    })
    public AddressDTO deleteAddressById(Long addressId) {

        User currentUser = authUtils.loggedInUser();

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

        if (existingAddress.getUser().getUserId() != currentUser.getUserId()) {
            throw new ApiException("you are Not allowed to update this address");

        }

        User user = existingAddress.getUser(); 

        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId)); 

        userRepository.save(user); 



        addressRepository.delete(existingAddress);

        return addressMapper.toDto(existingAddress);

    }

}
