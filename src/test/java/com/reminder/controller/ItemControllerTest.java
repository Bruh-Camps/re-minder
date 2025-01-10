package com.reminder.controller;

import com.reminder.dto.ItemDto;
import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.repository.ItemRepository;
import com.reminder.service.ItemService;
import com.reminder.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

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
    void testControllerCallServiceToSaveItem() {
        // Arrange
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange("01/12/2024");
        itemDto.setChangeDaysInterval(30);
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        ResponseEntity<?> response = itemController.createUserItem(itemDto);

        // Assert
        verify(itemService, times(1)).saveItem(itemDto, user);
    }

    @Test
    void testControllerCallServiceToGetUserItems() {
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
        verify(itemService, times(1)).getItemsByUser(user);
    }
}
