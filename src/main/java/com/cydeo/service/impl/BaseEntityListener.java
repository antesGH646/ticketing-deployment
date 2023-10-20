package com.cydeo.service.impl;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.entity.common.UserPrincipal;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Base entities do not extend from other classes
 * for this reason the class is created to extend from
 * AuditingEntityListener.
 * This class is designed to capture the user id of the role who is making
 * the changes/updates in the UI. It listens then captures when the base entity id is used.
 * Therefore, the BaseEntity has to be annotated with the @EntityListeners(BaseEntityListener.class)
 */
@Component
public class BaseEntityListener extends AuditingEntityListener {
    /**
     * For every action in the database this method will be executed to
     * record the user id with date and time
     * The fields are initialized in this method
     * Spring understands this method by the @PrePersis annotation
     */
    @PrePersist//marks/tells spring to execute this code for every action done in the db
    public void onPrePersist(BaseEntity baseEntity) {

       //security capturing the user's id
       final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.insertDateTime = LocalDateTime.now();
        //baseEntity.insertUserId= 1L;//hard coded
        baseEntity.lastUpdateDateTime = LocalDateTime.now();

        //  this.lastUpdateUserId = 1L;//hard coded
        if(authentication != null && !authentication.getName().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            baseEntity.insertUserId = ((UserPrincipal) principal).getId();
            baseEntity.lastUpdateUserId = ((UserPrincipal) principal).getId();
        }
    }

    /**
     * For update in the database this method will be executed to
     * record the user id with date and time
     * The fields are initialized in this method
     * Spring understands this method by the @PreUpdate annotation
     */
    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {

        //security capturing the user's id
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.lastUpdateDateTime = LocalDateTime.now();

        //this.lastUpdateUserId = 1L;//hard coded
        if(authentication != null && !authentication.getName().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            baseEntity.lastUpdateUserId = ((UserPrincipal) principal).getId();
        }
    }
}
