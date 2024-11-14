package com.reminder.service;

import com.reminder.model.Item;
import com.reminder.repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item saveItem(Item item) {
        item.setDataProximaTroca(item.getDataUltimaTroca().plusDays(item.getIntervaloTrocaDias()));
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
