package com.rentalcar.crud.rentalcarapi.repository;

import com.rentalcar.crud.rentalcarapi.entity.ChecklistItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Camada de acesso aos dados | Algumas queries já estão prontas, mas se necessário posso criar as minhas.
@Repository
public interface ChecklistItemRepository extends PagingAndSortingRepository<ChecklistItemEntity, Long>, CrudRepository<ChecklistItemEntity, Long>{

    //No optional, se existir o "objeto" procurado, ele retorna o mesmo.
    Optional<ChecklistItemEntity> findByGuid(String guid);

    Optional<ChecklistItemEntity> findByDescriptionAndIsCompleted(String description, Boolean isCompleted);

    long countByIsCompleted(Boolean isCompleted);

    List<ChecklistItemEntity> findByCategoryGuid(String guid);
}