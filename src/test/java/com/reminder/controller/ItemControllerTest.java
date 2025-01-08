package com.reminder.controller;

import com.reminder.dto.ItemDto;
import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.service.ItemService;
import com.reminder.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemController itemController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void testCreateUserItem_Success() {
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

        when(userService.getCurrentUser()).thenReturn(user);
        when(itemService.saveItem(itemDto, user)).thenReturn(savedItem);

        // Act
        ResponseEntity<?> response = itemController.createUserItem(itemDto);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Item saved successfully", response.getBody());
        verify(userService, times(1)).getCurrentUser();
        verify(itemService, times(1)).saveItem(itemDto, user);
    }

    @Test
    void testGetAllItemsForAuthenticatedUser_Success() {
        // Arrange
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        List<Item> items = Arrays.asList(item1, item2);

        when(userService.getCurrentUser()).thenReturn(user);
        when(itemService.getItemsByUser(user)).thenReturn(items);

        // Act
        List<Item> result = itemController.getAllItemsForAuthenticatedUser();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getName());
        assertEquals("Item 2", result.get(1).getName());
        verify(userService, times(1)).getCurrentUser();
        verify(itemService, times(1)).getItemsByUser(user);
    }
}
