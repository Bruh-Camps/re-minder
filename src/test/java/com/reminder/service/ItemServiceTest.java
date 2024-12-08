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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange(LocalDate.of(2024, 12, 1));
        itemDto.setChangeDaysInterval(30);

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Test Item");
        savedItem.setDateLastChange(LocalDate.of(2024, 12, 1));
        savedItem.setChangeDaysInterval(30);
        savedItem.setDateNextChange(LocalDate.of(2024, 12, 31));
        savedItem.setUser(user);

        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // Act
        Item result = itemService.saveItem(itemDto, user);

        // Assert
        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        assertEquals(LocalDate.of(2024, 12, 31), result.getDateNextChange());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testSaveItem_MissingFields_ShouldThrowException() {
        // Arrange
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Invalid Item");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.saveItem(itemDto, user);
        });
        assertEquals("dateLastChange and changeDaysInterval are required", exception.getMessage());
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
