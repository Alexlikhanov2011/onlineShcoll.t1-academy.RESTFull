package org.likhanov_2011.task_management_service_OnlineScooll_T1.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.config.MailConfig;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class NotificationService {
    private final JavaMailSender mailSender;
    private final MailConfig mailConfig;

    public void sendNotification(TaskStatusUpdateDTO event) {
        if (!mailConfig.isEnabled()) return;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    mailConfig.getEncoding()
            );

            helper.setFrom(mailConfig.getFrom());
            helper.setTo(mailConfig.getTo());
            helper.setSubject(mailConfig.getSubject());
            helper.setText(String.format(
                    "Статус задачи #%d изменен на: %s",
                    event.getTaskId(),
                    event.getNewStatus()
            ), true);

            mailSender.send(message);
            log.info("Уведомление отправлено на: {}", mailConfig.getTo());
        } catch (MessagingException e) {
            log.error("Ошибка отправки: {}", e.getMessage());
        }
    }
}
