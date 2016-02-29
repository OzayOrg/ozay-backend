package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.model.Building;
import com.ozay.backend.model.Notification;
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
@RequestMapping("/api/notification")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    NotificationService notificationService;


    /**
     * POST  /notification -> Create a new notification.
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createBuilding(@RequestBody NotificationFormDTO notificationFormDTO) {
        log.debug("REST request to save Notification : {}", notificationFormDTO);
        try{
            notificationService.processNotification(notificationFormDTO);

            return new ResponseEntity<>(notificationFormDTO, HttpStatus.CREATED);

        } catch(Exception e){
            return new ResponseEntity<>(notificationFormDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
