package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Where(clause = "is_deleted=false")//this clause will automatically be added/concatenated
// to whatever repository method that uses the User entity, it helps to add more filter
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    //to avoid duplicated username, and should not be null
    @Column(unique = true, nullable = false)

    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;

    //mapping to create the relationship, many user can have one role
    @ManyToOne
    @JoinColumn(name="role_id")//good practice to name the jpa generated foreign key
    private Role role;

    @Enumerated(EnumType.STRING)//to avoid the default 0 & 1 ordinal values
    private Gender gender;
}
