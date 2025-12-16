package org.tzi.use.api_security.security;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.tzi.use.api_security.repository.UserSecurityRepository;
import org.tzi.use.api_security.model.UserComposite;
import org.tzi.use.api_security.model.UserMetaModel;
import org.tzi.use.api_security.model.UserSecurityModel;
import org.tzi.use.api_security.repository.UserMetaRepository;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserMetaRepository userMetaRepository;

    @Autowired
    private UserSecurityRepository userSecurityRepository;

    private final Argon2 passwordEncoder = Argon2Factory.create();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserComposite user = loadUserByUsername(email);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        if (!passwordEncoder.verify(userDetails.getPassword(), password.toCharArray())) {
            int failedLogInAttempts = user.getUserSecurity().getFailedLogInAttempts() + 1;

            user.getUserSecurity().setFailedLogInAttempts(failedLogInAttempts);
            this.userSecurityRepository.save(user.getUserSecurity());

            throw new BadCredentialsException("Invalid password");
        }

        user.getUserSecurity().setFailedLogInAttempts(0);
        user.getUserSecurity().setLastLogInAt(new Timestamp(new Date().getTime()));
        this.userSecurityRepository.save(user.getUserSecurity());

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private UserComposite loadUserByUsername(String email) throws UsernameNotFoundException {
        UserMetaModel userMeta = userMetaRepository.findByEmail(email) //
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        UserSecurityModel userSecurity = userSecurityRepository.findById(userMeta.getUserId()).get();

        return new UserComposite(userMeta, userSecurity);
    }
}