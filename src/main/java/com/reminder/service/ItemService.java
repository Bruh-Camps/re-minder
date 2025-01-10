package com.reminder.service;

import com.reminder.dto.ItemDto;
import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<?> saveItem(ItemDto itemDto, User user) {

        if(itemDto.getDateLastChange() == null || itemDto.getChangeDaysInterval() == null || itemDto.getName() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing required fields");
            errorResponse.put("message", "Fields name, dateLastChange and changeDaysInterval are required.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Verifica se a data está no formato "dd/MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateLastChange;
        try {
            dateLastChange = LocalDate.parse(itemDto.getDateLastChange(), formatter);
        } catch (DateTimeParseException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Date");
            errorResponse.put("message", "Date must be in the format dd/MM/yyyy and valid.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if(itemDto.getChangeDaysInterval() < 1){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Invalid data provided. Change interval must be positive.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Converte DTO para entidade
        Item item = getItem(itemDto, user);

        itemRepository.save(item);

        return new ResponseEntity<>("Item saved successfully", HttpStatus.OK);
    }

    private static Item getItem(ItemDto itemDto, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateLastChange= LocalDate.parse(itemDto.getDateLastChange(), formatter);

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDateLastChange(dateLastChange);
        item.setChangeDaysInterval(itemDto.getChangeDaysInterval());
        item.setUser(user);

        // Calcula dateNextChange com base em dateLastChange e changeDaysInterval
        if (item.getDateLastChange() != null && item.getChangeDaysInterval() != null) {
            item.setDateNextChange(item.getDateLastChange().plusDays(item.getChangeDaysInterval()));
        } else {
            throw new IllegalArgumentException("dateLastChange and changeDaysInterval are required");
        }
        return item;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsByUser(User user) {
        return itemRepository.findByUserId(user.getId());
    }

}
