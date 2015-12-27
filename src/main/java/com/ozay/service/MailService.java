package com.ozay.service;

import com.ozay.domain.User;
import com.ozay.model.Building;
import com.ozay.model.Notification;
import com.ozay.model.NotificationRecord;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.BuildingRepository;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.List;
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
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private SpringTemplateEngine templateEngine;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }

    public int sendGrid(Notification notification, List<NotificationRecord> notificationRecords, String baseUrl){
        SendGrid sendgrid = new SendGrid("OzayOrg", "Ozaysyn1124");

        int sentCount = 0;

        SendGrid.Email sendGrid = new SendGrid.Email();

        for(NotificationRecord notificationRecord : notificationRecords){
            if(notificationRecord.getEmail() != null){
                if(notificationRecord.getEmail().matches(EMAIL_PATTERN)){
                    sendGrid.addSmtpApiTo(notificationRecord.getEmail());
                    sentCount++;
                    notificationRecord.setSuccess(true);
                } else {
                    notificationRecord.setSuccess(false);
                    notificationRecord.setNote("INVALID Email address");
                }
            }
        }

        Locale locale = Locale.forLanguageTag("EN");

        Context context = new Context(locale);


        String body = notification.getNotice();
        body = body.replace("&lt;", "<");
        body = body.replace("&gt;", ">");

        context.setVariable("body", body);
        context.setVariable("baseUrl", baseUrl);

        String content = templateEngine.process("notification", context);


        String buildingEmail = buildingRepository.getBuilding(notification.getBuildingId()).getEmail();
        if(buildingEmail != null) {
            sendGrid.setFrom(buildingEmail);
        }
        else {
            sendGrid.setFrom("noreply@ozay.us");
        }

        sendGrid.setSubject(notification.getSubject());
        sendGrid.setHtml(content);

        try {
            SendGrid.Response response = sendgrid.send(sendGrid);

            log.debug("Send email with sendgrid count {}", sentCount);
            System.out.println(response.getMessage());

        }
        catch (SendGridException e) {
            System.err.println(e);

            sentCount = 0;
        }

        return sentCount;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'!", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(final String email, String content, Locale locale) {
        log.debug("Sending activation e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(email, subject, content, false, true);
    }

    @Async
    public void sendInvitedUserRegisterEmail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("registerOrganizationUserEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationInvitationEmail(final String email, String content, Locale locale) {
        log.debug("Sending invitaion e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.invitation", null, locale);
        sendEmail(email, subject, content, false, true);
    }

    @Async
    public void sendActivationInvitationCompleteEmail(final String email, String content, Locale locale) {
        log.debug("Sending invitaion e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.invitation_registration_complete", null, locale);
        sendEmail(email, subject, content, false, true);
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

    // When registered user is invited by a bulding
    @Async
    public void sendInvitedMail(User user, Building building, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("building", building);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("existingUserInvited", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
}
