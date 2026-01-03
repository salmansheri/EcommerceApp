package com.Ecommerce.EcommerceApp.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ecommerce.EcommerceApp.Models.AppRole;
import com.Ecommerce.EcommerceApp.Models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(AppRole name);

}
