package com.rentalcar.crud.rentalcarapi.repository;

import com.rentalcar.crud.rentalcarapi.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Camada de acesso aos dados | Algumas queries já estão prontas, mas se necessário posso criar as minhas.
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long>, CrudRepository<CategoryEntity, Long> {

    //No optional, se existir o "objeto" procurado, ele retorna o mesmo.
    Optional<CategoryEntity> findByName(String name);

    Optional<CategoryEntity> findByGuid(String guid);
}