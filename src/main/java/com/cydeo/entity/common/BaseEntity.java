package com.cydeo.entity.common;

import com.cydeo.service.impl.BaseEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, updatable = false)//to make the column not updatable and not null
    public LocalDateTime insertDateTime;
    @Column(nullable = false, updatable = false)
    public Long insertUserId;//security dynamically tracks the changes made by the app users
    @Column(nullable = false)
    public LocalDateTime lastUpdateDateTime;//security puts the date and time in the table
    @Column(nullable = false)
    public Long lastUpdateUserId;//security tracks the changes, puts the id in the db

    //in real life deleting a row from a database is not good practice,
    // however most companies implement triggering, and use flags
    public Boolean isDeleted=false;
}
