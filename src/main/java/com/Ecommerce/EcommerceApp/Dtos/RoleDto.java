package com.Ecommerce.EcommerceApp.Dtos;

import com.Ecommerce.EcommerceApp.Models.AppRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class RoleDto {

    private Long roleId;


    private AppRole roleName;

    public RoleDto(AppRole roleName) {
        this.roleName = roleName;
    }
}
