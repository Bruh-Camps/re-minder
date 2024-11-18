package com.reminder.service;

import com.reminder.model.Item;
import com.reminder.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item saveItem(Item item) {
        item.setDateNextChange(item.getDateLastChange().plusDays(item.getChangeDaysInterval()));
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    // Outros métodos para atualizar e verificar lembretes
}
