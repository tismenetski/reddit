package com.tismenetski.reddit.service;

import com.tismenetski.reddit.exceptions.SpringRedditException;
import com.tismenetski.reddit.model.NotificationMail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    public void sendMail(NotificationMail notificationMail)
    {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationMail.getRecipient());
            messageHelper.setSubject(notificationMail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationMail.getBody()));

        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation mail sent!!");
        }catch (MailException e)
        {
            throw new SpringRedditException("Exception occurred when sending mail to " + notificationMail.getRecipient());
        }
    }
}
