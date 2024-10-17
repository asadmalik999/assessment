package com.assessment.springboot.service;

import com.assessment.springboot.dto.ItemDto;
import com.assessment.springboot.exception.ArgumentNotValidException;
import com.assessment.springboot.mapper.ItemMapper;
import com.assessment.springboot.model.Category;
import com.assessment.springboot.model.Item;
import com.assessment.springboot.repository.CategoryRepository;
import com.assessment.springboot.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestTemplate restTemplate;


    // Pagination support for GET request
    public Page<ItemDto> getAllItems(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        // Map the content of Page<Item> to List<ItemResponse> using MapStruct
        List<ItemDto> itemResponseList = items.getContent()
                .stream()
                .map(ItemMapper.INSTANCE::itemToItemResponse)
                .collect(Collectors.toList());

        // Return a new PageImpl containing the mapped ItemResponse and pagination info
        return new PageImpl<>(itemResponseList, pageable, items.getTotalElements());
    }

    // Pagination support for GET request
    public Page<ItemDto> getItemsByCategory(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Item> items = itemRepository.findByCategoryName(categoryName, pageable);
        List<ItemDto> itemResponseList = items.getContent()
                .stream()
                .map(ItemMapper.INSTANCE::itemToItemResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(itemResponseList, pageable, items.getTotalElements());
    }

    // Save an item
    public ItemDto saveItem(ItemDto item) throws ArgumentNotValidException {
        if (item.getName() == null || item.getCategoryName() == null ||
                item.getName().isEmpty() || item.getCategoryName().isEmpty()) {
            throw new ArgumentNotValidException("Either item name or category name is missing!");
        }
        // Find or create the Category
        Category category = categoryRepository.findByName(item.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(item.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        // Create and save the Item
        Item itemEntity = new Item();
        itemEntity.setName(item.getName());
        itemEntity.setDescription(item.getDescription());
        itemEntity.setCategory(category); // Set the Category

        return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(itemEntity));
    }

    //Get By ID
    public ItemDto getByID(Long itemId) {
        return itemRepository.findById(itemId).map(ItemMapper.INSTANCE::itemToItemResponse).orElse(null);
    }

    // Update an item
    public ItemDto updateItem(Long id, ItemDto item) {
        Optional<Item> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            Item updatedItem = existingItem.get();
            updatedItem.setName(item.getName().isEmpty() ? updatedItem.getName() : item.getName());
            updatedItem.setDescription(item.getDescription().isEmpty() ? updatedItem.getDescription() : item.getDescription());
            return ItemMapper.INSTANCE.itemToItemResponse(itemRepository.save(updatedItem));
        } else {
            throw new RuntimeException("Item not found");
        }
    }

    // Delete an item
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public String callExternalAPI() {
        String response = restTemplate.getForObject("https://www.google.com", String.class);
        return response;
    }

}
