package com.reminder.controller;

import com.reminder.dto.ItemDto;
import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.service.ItemService;
import com.reminder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService; // Para buscar o usuário autenticado no banco

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("/item")
    public ResponseEntity<?> createUserItem(@Valid @RequestBody ItemDto itemDto) {

        if(itemDto.getChangeDaysInterval() < 1){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Invalid data provided. Change interval must be positive.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        User user = userService.getCurrentUser();
        Item savedItem = itemService.saveItem(itemDto, user);
        return new ResponseEntity<>("Item saved successfully", HttpStatus.OK);
    }

    @GetMapping("/items")
    public List<Item> getAllItemsForAuthenticatedUser() {
        User user = userService.getCurrentUser();
        return itemService.getItemsByUser(user);
    }
}
