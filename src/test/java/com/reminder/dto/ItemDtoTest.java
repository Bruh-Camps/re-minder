package com.reminder.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    @Test
    void testGettersAndSetters() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto.setChangeDaysInterval(30);

        assertEquals("Test Item", itemDto.getName());
        assertEquals(LocalDate.of(2023, 12, 14), itemDto.getDateLastChange());
        assertEquals(30, itemDto.getChangeDaysInterval());
    }

    @Test
    void testNameNullValidation() {
        ItemDto itemDto = new ItemDto();
        itemDto.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto.setChangeDaysInterval(30);

        assertNull(itemDto.getName());
    }

    @Test
    void testDateLastChangeNullValidation() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setChangeDaysInterval(30);

        assertNull(itemDto.getDateLastChange());
    }

    @Test
    void testChangeDaysIntervalNullValidation() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange(LocalDate.of(2023, 12, 14));

        assertNull(itemDto.getChangeDaysInterval());
    }

    @Test
    void testEqualsSameObject() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto.setChangeDaysInterval(30);

        assertEquals(itemDto, itemDto);
    }

    @Test
    void testEqualsDifferentObjects() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Test Item");
        itemDto1.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Test Item");
        itemDto2.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto2.setChangeDaysInterval(30);

        assertEquals(itemDto1, itemDto2);
    }

    @Test
    void testEqualsNotTrue() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Test Item");
        itemDto1.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Test different Item");
        itemDto2.setDateLastChange(LocalDate.of(2024, 12, 14));
        itemDto2.setChangeDaysInterval(20);

        assertNotEquals(itemDto1, itemDto2);
    }

    @Test
    void testHashCode() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Test Item");
        itemDto1.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Test Item");
        itemDto2.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto2.setChangeDaysInterval(30);

        assertEquals(itemDto1.hashCode(), itemDto2.hashCode());
    }

    @Test
    void testNotEqualsDifferentValues() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Item A");
        itemDto1.setDateLastChange(LocalDate.of(2023, 12, 14));
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Item B");
        itemDto2.setDateLastChange(LocalDate.of(2024, 1, 1));
        itemDto2.setChangeDaysInterval(15);

        assertNotEquals(itemDto1, itemDto2);
    }
}