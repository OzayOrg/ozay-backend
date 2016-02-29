package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.model.NotificationRecord;
import com.ozay.backend.repository.NotificationRecordRepository;
import com.ozay.backend.repository.NotificationRepository;
import com.ozay.backend.service.NotificationService;
import com.ozay.backend.web.rest.form.NotificationFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by naofumiezaki on 11/23/15.
 */
@RestController
@RequestMapping("/api/notification-record")
public class NotificationRecordResource {

    private final Logger log = LoggerFactory.getLogger(NotificationRecordResource.class);

    @Inject
    NotificationRecordRepository notificationRecordRepository;


    /**
     * PUT  /notification -> Create a new notification.
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateNotificationRecord(@RequestBody NotificationRecord notificationRecord) {
        log.debug("REST request to update NotificationRecord : {}", notificationRecord);
        notificationRecordRepository.update(notificationRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
