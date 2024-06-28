package com.rentalcar.crud.rentalcarapi.service;

import com.rentalcar.crud.rentalcarapi.entity.CategoryEntity;
import com.rentalcar.crud.rentalcarapi.entity.ChecklistItemEntity;
import com.rentalcar.crud.rentalcarapi.exception.ResourceNotFoundException;
import com.rentalcar.crud.rentalcarapi.repository.CategoryRepository;
import com.rentalcar.crud.rentalcarapi.repository.ChecklistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Service
@Slf4j
public class ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private void validateChecklistItemData(String description, Boolean isCompleted, LocalDate deadline, String categoryGuid) {

        if(!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("O item do checklist deve ter uma descrição");
        }

        if(!StringUtils.hasText(categoryGuid)) {
            throw new IllegalArgumentException("O item do checklist deve ter o identificador da categoria");
        }

        if(isCompleted == null) {
            throw new IllegalArgumentException("O item do checklist deve ter um status");
        }

        if(deadline == null) {
            throw new IllegalArgumentException("O item do checklist deve ter um prazo final");
        }
    }

    public ChecklistItemEntity updateChecklistItem(String guid, String description, Boolean isCompleted, LocalDate deadline, String categoryGuid) {

        if(!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O identificador do item deve ser fornecido");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item do checklist nao encontrado.")
        );

        if(StringUtils.hasText(description)) {
            retrievedItem.setDescription(description);
        }

        if(isCompleted != null) {
            retrievedItem.setIsCompleted(isCompleted);
        }

        if(deadline != null) {
            retrievedItem.setDeadline(deadline);
        }

        if(categoryGuid != null) {
            CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid).orElseThrow(
                    () -> new ResourceNotFoundException("Categoria não encontrada."));
            retrievedItem.setCategory(retrievedCategory);
        }

        log.debug("Updating checklist item [ checklistItem = {} ]", retrievedItem.toString());

        return this.checklistItemRepository.save(retrievedItem);
    }

    public ChecklistItemEntity addNewChecklistItem(String description, Boolean isCompleted, LocalDate deadline, String categoryGuid) {
        this.validateChecklistItemData(description, isCompleted, deadline, categoryGuid);

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid).orElseThrow(
                () -> new ResourceNotFoundException("Categoria não encontrada."));

        ChecklistItemEntity checklistItemEntity = new ChecklistItemEntity();
        checklistItemEntity.setGuid(UUID.randomUUID().toString());
        checklistItemEntity.setDescription(description);
        checklistItemEntity.setIsCompleted(isCompleted);
        checklistItemEntity.setDeadline(deadline);
        checklistItemEntity.setPostedDate(LocalDate.now());
        checklistItemEntity.setCategory(retrievedCategory);

        log.debug("Adding new checklist item [ checklistItem = {} ]", checklistItemEntity);

        return checklistItemRepository.save(checklistItemEntity);
    }

    public Iterable<ChecklistItemEntity> findAllChecklistItems() {
        return this.checklistItemRepository.findAll();
    }

    public ChecklistItemEntity deleteChecklistItem(String guid) {
        if(!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O identificador do item deve ser fornecido");
        }
        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item do checklist não encontrado.")
        );

        log.debug("Deleting checklist item [ guid = {} ]", guid);

        this.checklistItemRepository.delete(retrievedItem);

        return retrievedItem;
    }

    public ChecklistItemEntity findChecklistItemByGuid(String guid) {

        if(!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O identificador do item deve ser fornecido");
        }

        Optional<ChecklistItemEntity> checklistItem = this.checklistItemRepository.findByGuid(guid);

        if (checklistItem.isPresent()) {
            return checklistItem.get();
        } else {
            throw new ResourceNotFoundException("Item do checklist não encontrado.");
        }
    }

    public Double getProgress() {
        long count = this.checklistItemRepository.count();
        long countCompleted = this.checklistItemRepository.countByIsCompleted(true);

        if (count == 0) return 0.0;

        BigDecimal bigDecimal = BigDecimal.valueOf(((double) countCompleted / (double) count) * 100);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }
}

