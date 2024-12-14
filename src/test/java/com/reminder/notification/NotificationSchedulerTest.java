package com.reminder.notification;

import com.reminder.model.Item;
import com.reminder.model.User;
import com.reminder.service.ItemService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationSchedulerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);

    @InjectMocks
    private NotificationScheduler notificationScheduler;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    private Item item;

    @BeforeEach
    public void SetUp(){
        // Mock do Item para o teste
        item = new Item();
        item.setName("Óleo de Motor");
        item.setDateNextChange(LocalDate.now());
        User testUser = new User();
        testUser.setEmail("tester@reminder.com");
        item.setUser(testUser);
    }

    @Test
    public void testCheckAndNotifyForItemReplacements_SendsEmail() {
        when(itemService.getAllItems()).thenReturn(Collections.singletonList(item));
        notificationScheduler.checkAndNotifyForItemReplacements();

        verify(mailSender, times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage sentMessage = mailMessageCaptor.getValue();

        assertEquals("tester@reminder.com", sentMessage.getTo()[0]);
        assertEquals("Lembrete: Troca de Óleo de Motor", sentMessage.getSubject());
        assertEquals(
                "Olá! Lembre-se de trocar o item: Óleo de Motor. A troca é recomendada para hoje.",
                sentMessage.getText()
        );
    }

    @Test
    public void testCheckAndNotifyForItemReplacements_DoesNotSendEmailWhenDateIsNotToday() {
        when(itemService.getAllItems()).thenReturn(Collections.singletonList(item));
        item.setDateNextChange(LocalDate.now().plusDays(1));

        notificationScheduler.checkAndNotifyForItemReplacements();

        verify(mailSender, times(0)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testCheckAndNotifyForItemReplacements_NoItems() {
        when(itemService.getAllItems()).thenReturn(new ArrayList<>());

        notificationScheduler.checkAndNotifyForItemReplacements();

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testCheckAndNotifyForItemReplacements_MultipleItems() {
        Item anotherItem = new Item();
        anotherItem.setName("Filtro de Água");
        anotherItem.setDateNextChange(LocalDate.now());
        User anotherUser = new User();
        anotherUser.setEmail("user2@reminder.com");
        anotherItem.setUser(anotherUser);

        when(itemService.getAllItems()).thenReturn(Arrays.asList(item, anotherItem));

        notificationScheduler.checkAndNotifyForItemReplacements();

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }

}

