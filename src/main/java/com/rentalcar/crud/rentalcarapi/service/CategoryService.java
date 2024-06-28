package com.rentalcar.crud.rentalcarapi.service;

import com.rentalcar.crud.rentalcarapi.dto.CardDTO;
import com.rentalcar.crud.rentalcarapi.entity.CategoryEntity;
import com.rentalcar.crud.rentalcarapi.entity.ChecklistItemEntity;
import com.rentalcar.crud.rentalcarapi.exception.CategoryServiceException;
import com.rentalcar.crud.rentalcarapi.exception.ResourceNotFoundException;
import com.rentalcar.crud.rentalcarapi.repository.CategoryRepository;
import com.rentalcar.crud.rentalcarapi.repository.ChecklistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CategoryService {

    ChecklistItemRepository checklistItemRepository;
    CategoryRepository categoryRepository;

    //Não é mais necessário utilizar o @AutoWired, a injeção é feita no construtor, mostrando o que
    //ele espera receber nas variáveis de referência
    public CategoryService(ChecklistItemRepository checklistItemRepository, CategoryRepository categoryRepository) {
        this.checklistItemRepository = checklistItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity addNewCategory(String name) {

        if(!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio ou nulo");
        }

        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setGuid(UUID.randomUUID().toString());
        newCategory.setName(name);

        log.debug("Adding a new category with name [ name = {} ]", name);

        return this.categoryRepository.save(newCategory);
    }

    public CategoryEntity updateCategory(String guid, String name) {
        if(guid == null || !StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O identificador da categoria deve ser fornecido");
        }

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Categoria não encontrada.")
        );

        retrievedCategory.setName(name);
        log.debug("Updating category [ guid = {}, newName = {} ]", guid, name);

        return this.categoryRepository.save(retrievedCategory);
    }

    public CategoryEntity deleteCategory(String guid) {

        if(!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O identificador da categoria deve ser fornecido");
        }

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Categoria não encontrada.")
        );


        //Verifica se a Categoria passada está sendo utilizada por alguma checklist
        List<ChecklistItemEntity> checklistItems = this.checklistItemRepository.findByCategoryGuid(guid);
        if(!CollectionUtils.isEmpty(checklistItems)) {
            throw new CategoryServiceException("Não é possível excluir essa categoria porque ela está sendo usada por um ou mais itens do checklist");
        }

        log.debug("Deleting category [ guid = {} ]", guid);

        this.categoryRepository.delete(retrievedCategory);

        return retrievedCategory;
    }

    public Iterable<CategoryEntity> findAllCategories() {

        return this.categoryRepository.findAll();
    }

    public CategoryEntity findCategoryByGuid(String guid) {

        if(!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O identificador da categoria deve ser fornecido");
        }

        return this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Categoria não encontrada.")
        );
    }

    public List<CardDTO> getAmountLateByCategory() {
        List<CardDTO> cardDTOList = new ArrayList<>();

        Iterable<CategoryEntity> categories = this.categoryRepository.findAll();

        for (CategoryEntity category : categories) {
            List<ChecklistItemEntity> listItems = this.checklistItemRepository.findByCategoryGuid(category.getGuid());

            int amountLate = 0;

            for (ChecklistItemEntity checklistItem : listItems) {
                if (!checklistItem.getIsCompleted()) {
                    if (checklistItem.getDeadline().isBefore(LocalDate.now())) {
                        amountLate++;
                    }
                }
            }

            CardDTO cardDTO = CardDTO.builder().categoryName(category.getName()).amountItemsLate(amountLate).build();
            cardDTOList.add(cardDTO);
        }
        return cardDTOList;
    }
}
