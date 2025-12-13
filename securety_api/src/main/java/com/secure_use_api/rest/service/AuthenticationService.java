package com.secure_use_api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.transaction.Transactional;
import com.secure_use_api.exceptions.Exceptions.UserAlreadyExists;
import com.secure_use_api.model.UserMetaModel;
import com.secure_use_api.model.UserSecurityModel;
import com.secure_use_api.repository.UserMetaRepository;
import com.secure_use_api.repository.UserSecurityRepository;
import com.secure_use_api.security.Role;
import com.secure_use_api.security.UserDetailsImpl;
import com.secure_use_api.token.AccessToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserMetaRepository userMetaRepo;

    @Autowired
    private UserSecurityRepository userSecurityRepo;

    @Autowired
    private AuthenticationManager authManager;

    // * Must be the same as in the CustomAuthProvider.java
    private final Argon2 passwordEncoder = Argon2Factory.create();

    @Transactional
    public UUID register(String username, String password) throws UserAlreadyExists {

        // Check if user already exists
        if (userMetaRepo.findByEmail(username).isPresent()) {
            throw new UserAlreadyExists("Username is already taken.");
        }

        // Create new user
        ArrayList<String> roles = new ArrayList<>();
        UserMetaModel userMeta;

        roles.add(new Role(Role.Roles.USER).toString());

        userMeta = new UserMetaModel();

        userMeta.setEmail(username);
        userMeta.setRoles(roles);
        userMetaRepo.save(userMeta);

        UserSecurityModel userSecurity = new UserSecurityModel();
        String passwordHash = passwordEncoder.hash(22, 65536, 1, password.toCharArray());

        userSecurity.setUserMeta(userMeta);
        userSecurity.setPassword(passwordHash);
        userSecurityRepo.save(userSecurity);

        return userMeta.getUserId();
    }

    @Transactional(dontRollbackOn = AuthenticationException.class)
    public AccessToken login(String username, String password) throws AuthenticationException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        Collection<Role> roles = user.getAuthorities();

        AccessToken accessToken = new AccessToken(user.getUserId(), user.getUsername(), new ArrayList<>(roles));

        return accessToken;
    }

    public void delete(UUID userId) {
        userMetaRepo.deleteById(userId);
    }
}