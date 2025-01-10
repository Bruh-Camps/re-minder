package com.reminder.service;

import com.reminder.dto.ItemDto;
import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void testSaveItem_Success() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange("01/12/2024");
        itemDto.setChangeDaysInterval(30);

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Test Item");
        savedItem.setDateLastChange(LocalDate.parse("01/12/2024", formatter));
        savedItem.setChangeDaysInterval(30);
        savedItem.setDateNextChange(LocalDate.parse("31/12/2024", formatter));
        savedItem.setUser(user);

        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // Act
        ResponseEntity<?> response = itemService.saveItem(itemDto, user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemRepository).save(argThat(item -> Objects.equals(item.getName(), "Test Item")));
        verify(itemRepository).save(argThat(item -> Objects.equals(item.getDateLastChange(), LocalDate.parse("01/12/2024", formatter))));
        verify(itemRepository).save(argThat(item -> Objects.equals(item.getDateNextChange(), LocalDate.parse("31/12/2024", formatter))));
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testTrySavingItemsWithMissingFields() {
        // Arrange
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Invalid Item");
        Map<String, String> expectedErrorResponse = new HashMap<>();
        expectedErrorResponse.put("error", "Missing required fields");
        expectedErrorResponse.put("message", "Fields name, dateLastChange and changeDaysInterval are required.");

        // Act
        ResponseEntity<?> response = itemService.saveItem(itemDto, user);

        //Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals(expectedErrorResponse, response.getBody());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void testGetAllItems() {
        // Arrange
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        // Act
        List<Item> items = itemService.getAllItems();

        // Assert
        assertNotNull(items);
        assertEquals(2, items.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetItemsByUser() {
        // Arrange
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("User Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("User Item 2");

        when(itemRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(item1, item2));

        // Act
        List<Item> items = itemService.getItemsByUser(user);

        // Assert
        assertNotNull(items);
        assertEquals(2, items.size());
        verify(itemRepository, times(1)).findByUserId(user.getId());
    }
}
