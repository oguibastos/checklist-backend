package com.rentalcar.crud.rentalcarapi.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@MappedSuperclass
public class BaseEntity {

    private String guid;
}
