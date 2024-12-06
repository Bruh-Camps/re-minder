package com.reminder.controller;

import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.service.ItemService;
import com.reminder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    //TODO: Implementar salvamento de items
    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @GetMapping("/items")
    public List<Item> getAllItemsForAuthenticatedUser() {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        // Busca o usuário no banco
        Optional<User> user = userService.findByUsername(login);
        if (user.isEmpty() || login.equals("anonymousUser")) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Retorna os itens do usuário autenticado
        return itemService.getItemsByUserId(user.get().getId());
    }
}
