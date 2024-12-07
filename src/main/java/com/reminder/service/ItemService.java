package com.reminder.service;

import com.reminder.dto.ItemDto;
import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item saveItem(ItemDto itemDto, User user) {
        // Converte DTO para entidade
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDateLastChange(itemDto.getDateLastChange());
        item.setChangeDaysInterval(itemDto.getChangeDaysInterval());
        item.setUser(user);

        // Calcula dateNextChange com base em dateLastChange e changeDaysInterval
        if (item.getDateLastChange() != null && item.getChangeDaysInterval() != null) {
            item.setDateNextChange(item.getDateLastChange().plusDays(item.getChangeDaysInterval()));
        } else {
            throw new IllegalArgumentException("dateLastChange and changeDaysInterval are required");
        }

        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsByUser(User user) {
        return itemRepository.findByUserId(user.getId());
    }

}
