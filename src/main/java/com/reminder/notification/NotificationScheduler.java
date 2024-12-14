package com.reminder.notification;

import com.reminder.model.Item;
import com.reminder.service.ItemService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class NotificationScheduler {

    private final ItemService itemService;
    private final JavaMailSender mailSender;

    public NotificationScheduler(ItemService itemService, JavaMailSender mailSender) {
        this.itemService = itemService;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "0 0 9 * * *")  // Executa todos os dias às 09:00
    public void checkAndNotifyForItemReplacements() {
        List<Item> items = itemService.getAllItems();  // Obtemos todos os itens registrados

        for (Item item : items) {
            if (item.getDateNextChange().isEqual(LocalDate.now())) {
                sendNotificationEmail(item);
            }
        }
    }

    protected void sendNotificationEmail(Item item) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Aqui usamos o e-mail do usuário associado ao item
        String userEmail = item.getUser().getEmail();  // Presumindo que 'getUser()' retorne o usuário associado ao item

        if (userEmail != null && !userEmail.isEmpty()) {
            message.setTo(userEmail);
            message.setSubject("Lembrete: Troca de " + item.getName());
            message.setText("Olá! Lembre-se de trocar o item: " + item.getName() + ". A troca é recomendada para hoje.");

            mailSender.send(message);
        }
    }
}
