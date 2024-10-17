package com.assessment.springboot.controller;

import com.assessment.springboot.dto.ItemDto;
import com.assessment.springboot.exception.ArgumentNotValidException;
import com.assessment.springboot.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // GET method to retrieve items (with pagination)
    @GetMapping
    public ResponseEntity<Page<ItemDto>> getItems(@RequestParam(defaultValue = "0", required = false) int page,
                                                  @RequestParam(defaultValue = "10", required = false) int size) {
        Page<ItemDto> items = itemService.getAllItems(PageRequest.of(page, size));
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/byCategory/{categoryName}")
    public ResponseEntity<Page<ItemDto>> searchItemsByCategory(@PathVariable String categoryName,
                                                  @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Page<ItemDto> items = itemService.getItemsByCategory(categoryName, page, size);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        ItemDto byID = itemService.getByID(id);
        if (byID == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(byID);
    }

    @GetMapping("/google")
    public ResponseEntity<String> callGoogle() {
        return ResponseEntity.ok(itemService.callExternalAPI());
    }

    // POST method to create an item
    @PostMapping
    public ResponseEntity<ItemDto> createItem( @RequestBody ItemDto item) throws ArgumentNotValidException {
        ItemDto createdItem = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    // PUT method to update an item
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto item) {
        ItemDto updatedItem = itemService.updateItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    // DELETE method to remove an item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
