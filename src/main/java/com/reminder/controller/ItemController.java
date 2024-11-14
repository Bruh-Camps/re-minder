package com.reminder.controller;

import com.reminder.model.Item;
import com.reminder.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @GetMapping("/getAll")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/user/{userId}")
    public List<Item> getAllItemsFromUser(@PathVariable Long userId) {
        return itemService.getItemsByUserId(userId);
    }
}
