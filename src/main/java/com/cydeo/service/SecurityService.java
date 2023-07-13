package com.cydeo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Security services should extend the UserDetailsService,
 * because the UserDetailsService will automatically authenticate
 * the user in the UI
 * The SecurityServiceImpl Overrides to implement the
 * loadUserByUsername() method
 */
public interface SecurityService extends UserDetailsService {
}
