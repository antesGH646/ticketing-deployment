package com.cydeo.service.impl;

import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class fetches a user from the database and validates/compares
 * with the UI User details entry. It has a method(the loadUserByUsername())
 * that returns a user details from UserDetails interface implemented by the User class.
 * However, you need map the extracted user details
 * (extracted from the User that implements UserDetails)
 * into the User entity(the User class entity that you created) using the mapper class.
 * The mapper class must implement the UserDetails
*/
@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;//to call a method that extracts user details

    //injection through a constructor
    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetching the user details of a given username from db, this user is an entity user
        User user = userRepository.findByUserName(username);
        //if the given username is not in the database throws an exception
        if(user == null) {
            throw new UsernameNotFoundException("This user does not exist");
        }
        //takes a user entity and assigns it to the spring's user to validate using its methods
        return new UserPrincipal(user);//UserPrinciple maps/compares/validates automatically
    }
}
