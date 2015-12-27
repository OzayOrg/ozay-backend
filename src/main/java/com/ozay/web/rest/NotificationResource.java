package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Notification;
import com.ozay.model.NotificationRecord;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.NotificationRecordRepository;
import com.ozay.repository.NotificationRepository;
import com.ozay.repository.UserRepository;
import com.ozay.service.NotificationService;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private NotificationService notificationService;

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private NotificationRecordRepository notificationRecordRepository;

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/notifications",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody NotificationDTO notificationDto, @RequestParam(value = "building") Long buildingId, HttpServletRequest request) {
        log.debug("REST request to save Notification : {}", notificationDto);

        String baseUrl = request.getScheme() +
            "://" +
            request.getServerName() +
            ":" +
            request.getServerPort();


        int emailCount = notificationService.sendNotice(notificationDto, baseUrl);
        if(emailCount == 0){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        JsonResponse json = new JsonResponse();

        String message = "Notice is successfully scheduled to " + emailCount + " recipients";
        json.setResponse(message);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

    /**
     * GET  /notifications -> get all the notifications. with limit
     */
    @RequestMapping(value = "/notifications/latest/limit/{limit}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Notification> getAllSearchResultsByBuilding(@RequestParam(value = "building") Long buildingId, @PathVariable Long limit) {
        log.debug("REST request to get all Notifications results by serach and Building");
        return notificationRepository.searchNotificationWithLimit(buildingId, limit);
    }

    /**
     * GET  /notifications -> get all the notifications.
     */
    @RequestMapping(value = "/notifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NotificationRecord> getAllByBuilding(@RequestParam(value = "building") Long buildingId) {
        log.debug("REST request to get all Notifications by Building");
        //return notificationRepository.findAllByBuilding(buildingId);
        return notificationRecordRepository.findAllTrackedByBuildingId(buildingId);
    }

    /**
     * GET  /notifications/:id -> get the "id" notification.
     */
    @RequestMapping(value = "/notifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Notification> get(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        return Optional.ofNullable(notificationRepository.findOne(id))
            .map(notification -> {
                notification.setNotificationRecordList(notificationRecordRepository.getAllByNotificationId(id));
                return
                new ResponseEntity<>(
                    notification,
                    HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notifications/:id -> delete the "id" notification.
     */
//    @RequestMapping(value = "/notifications/{id}",
//        method = RequestMethod.DELETE,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public void delete(@PathVariable Long id) {
//        log.debug("REST request to delete Notification : {}", id);
//        notificationRepository.delete(id);
//    }
}
