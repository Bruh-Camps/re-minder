package com.reminder.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    @Test
    void testGettersAndSetters() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange("14/12/2023");
        itemDto.setChangeDaysInterval(30);

        assertEquals("Test Item", itemDto.getName());
        assertEquals("14/12/2023", itemDto.getDateLastChange());
        assertEquals(30, itemDto.getChangeDaysInterval());
    }

    @Test
    void testNameNullValidation() {
        ItemDto itemDto = new ItemDto();
        itemDto.setDateLastChange("14/12/2023");
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
        itemDto.setDateLastChange("14/12/2023");

        assertNull(itemDto.getChangeDaysInterval());
    }

    @Test
    void testEqualsSameObject() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDateLastChange("14/12/2023");
        itemDto.setChangeDaysInterval(30);

        assertEquals(itemDto, itemDto);
    }

    @Test
    void testEqualsDifferentObjects() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Test Item");
        itemDto1.setDateLastChange("14/12/2023");
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Test Item");
        itemDto2.setDateLastChange("14/12/2023");
        itemDto2.setChangeDaysInterval(30);

        assertEquals(itemDto1, itemDto2);
    }

    @Test
    void testEqualsNotTrue() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Test Item");
        itemDto1.setDateLastChange("14/12/2023");
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Test different Item");
        itemDto2.setDateLastChange("14/12/2023");
        itemDto2.setChangeDaysInterval(20);

        assertNotEquals(itemDto1, itemDto2);
    }

    @Test
    void testHashCode() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Test Item");
        itemDto1.setDateLastChange("14/12/2023");
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Test Item");
        itemDto2.setDateLastChange("14/12/2023");
        itemDto2.setChangeDaysInterval(30);

        assertEquals(itemDto1.hashCode(), itemDto2.hashCode());
    }

    @Test
    void testNotEqualsDifferentValues() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Item A");
        itemDto1.setDateLastChange("14/12/2023");
        itemDto1.setChangeDaysInterval(30);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Item B");
        itemDto2.setDateLastChange("01/01/2024");
        itemDto2.setChangeDaysInterval(15);

        assertNotEquals(itemDto1, itemDto2);
    }
}
