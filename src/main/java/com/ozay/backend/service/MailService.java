package com.ozay.backend.service;

import com.ozay.backend.config.JHipsterProperties;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.*;
import com.ozay.backend.repository.OrganizationRepository;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;
import com.ozay.backend.web.rest.form.NotificationFormDTO;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private OrganizationRepository organizationRepository;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendNewOrganizationUserWelcomeEmail(OrganizationUserDTO organizationUserDTO, String baseUrl) {
        log.debug("Sending organization user welcome e-mail to '{}'", organizationUserDTO.getEmail());
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("user", organizationUserDTO);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("organizationUserWelcomeEmail", context);
        String subject = messageSource.getMessage("email.organizationUserWelcome.title", null, locale);
        sendEmail(organizationUserDTO.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendOrganizationUserInvitationMail(OrganizationUserDTO organizationUserDTO, String baseUrl, String activationKey) {
        log.debug("Sending organization user invitation e-mail to '{}'", organizationUserDTO.getEmail());
        Locale locale = Locale.forLanguageTag("en");

        String name = organizationUserDTO.getFirstName() + " " + organizationUserDTO.getLastName();
        Organization organization = organizationRepository.findOne(organizationUserDTO.getOrganizationId());
        Context context = new Context(locale);
        context.setVariable("user", organizationUserDTO);
        context.setVariable("name", name);
        context.setVariable("organization", organization.getName());
        context.setVariable("activationKey", activationKey);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("organizationUserInvitationEmail", context);
        String subject = messageSource.getMessage("email.organizationUser.title", null, locale);
        sendEmail(organizationUserDTO.getEmail(), subject, content, false, true);
    }

    @Async
    public void inviteMember(Member member, Building building, InvitedMember invitedMember, String baseUrl){
        log.debug("Sending invitation e-mail to {}", member);
        Locale locale = Locale.forLanguageTag(invitedMember.getLangKey());
        Context context = new Context(locale);
        context.setVariable("name", member.getFirstName() + " " + member.getLastName());
        context.setVariable("building", building.getName());
        context.setVariable("activationKey", invitedMember.getActivationKey());
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("memberInvitationEmail", context);
        String subject = messageSource.getMessage("email.member.subject", null, locale);
        sendEmail(member.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendNotification(NotificationFormDTO notificationFormDTO, String[] to) {
        Notification notification = notificationFormDTO.getNotification();
        log.debug("Sending notification e-mail to {}", to);
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("body", notification.getNotice());
        String content = templateEngine.process("notificationEmail", context);
        String subject = notification.getSubject();
        log.debug("About to send email");
        this.sendMultipleEmails(notificationFormDTO, to, subject, content, false, true);
    }

    @Async
    private void sendMultipleEmails(NotificationFormDTO notificationFormDTO, String[] to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);

            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            for(String emailAddress:to){
                message.setTo(emailAddress);
                javaMailSender.send(mimeMessage);
            }
        } catch (Exception e) {
            log.warn("E-mail could not be sent to users '{}', exception is: {}", to, e.getMessage());
        }
        log.debug("Sent e-mail to Emails '{}'", to);
    }
}
