package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class NotificationService {
    private final JavaMailSender mailSender;


    public void sendNotification(TaskStatusUpdateDTO event) {
        String to = "likhanov-2018@yandex.ru";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Обновление статуса задачи");
            helper.setText(String.format("Статус задачи #%d изменен на: %s", event.getTaskId(), event.getNewStatus()));
            mailSender.send(message);
            log.info("Уведомление отправлено на почту: {}", to);
        } catch (MessagingException e) {
            log.error("Ошибка при отправке письма: {}", e.getMessage());
        }
    }
}
