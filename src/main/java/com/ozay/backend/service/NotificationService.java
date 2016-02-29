package com.ozay.backend.service;

import com.ozay.backend.model.*;
import com.ozay.backend.repository.*;
import com.ozay.backend.web.rest.form.NotificationFormDTO;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/18/15.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NotificationService {

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    NotificationRecordRepository notificationRecordRepository;

    @Inject
    MailService mailService;

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public void processNotification(NotificationFormDTO notificationFormDTO) throws Exception {
        Notification notification = notificationFormDTO.getNotification();
        notification.setIssueDate(new DateTime());
        notificationRepository.create(notification);

        ArrayList<String> emails = new ArrayList<String>();
        List<NotificationRecord> notificationRecords = new ArrayList<NotificationRecord>();

        Set<Long> ids = new HashSet<Long>();
        for(Member member : notificationFormDTO.getMembers()){
            ids.add(member.getId());
        }

        long validEmailCount = 0;
        List<Member> members = memberRepository.findAllByIds(ids);

        for(Member member:members){
            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setNotificationId(notification.getId());
            notificationRecord.setEmail(member.getEmail());
            notificationRecord.setMemberId(member.getId());
            // insert the function to check if tracking is selected
//            notificationRecord.setTrack(true);
            //insert the function to 1) check if tracking is selected and 2) if selected initially set to false
//            if (notificationRecord.isTrack()) {
//                notificationRecord.set(false);
//            }

            if(notificationRecord.getEmail().matches(EMAIL_PATTERN)){
                validEmailCount++;
                notificationRecord.setSuccess(true);
                emails.add(notificationRecord.getEmail());
            } else {
                notificationRecord.setSuccess(false);
                notificationRecord.setNote("Invalid Email address: " + notificationRecord.getEmail());
            }
            notificationRecordRepository.create(notificationRecord);
        }
        if(validEmailCount == 0){
            throw new Exception();
        } else {
            notification.setEmailCount(validEmailCount);
            notificationRepository.update(notification);
        }

        mailService.sendNotification(notificationFormDTO, emails.toArray(new String[emails.size()]));

    }
}
