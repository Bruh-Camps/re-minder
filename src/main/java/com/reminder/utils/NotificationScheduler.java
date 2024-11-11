package com.reminder.utils;

import com.reminder.model.Item;
import com.reminder.service.ItemService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling  // Habilita o agendamento de tarefas
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
            if (item.getDataProximaTroca().isEqual(LocalDate.now())) {
                sendNotificationEmail(item);
            }
        }
    }

    private void sendNotificationEmail(Item item) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("usuario@exemplo.com"); // Aqui você usaria o e-mail do usuário
        message.setSubject("Lembrete: Troca de " + item.getNome());
        message.setText("Olá! Lembre-se de trocar o item: " + item.getNome() + ". A troca é recomendada para hoje.");

        /*
        * Nota: Substitua seu-email@gmail.com e sua-senha pelas credenciais reais.
        * Caso você esteja usando o Gmail, talvez seja necessário permitir o acesso
        * a aplicativos menos seguros ou gerar uma senha de aplicativo.
        * */

        mailSender.send(message);
    }
}
