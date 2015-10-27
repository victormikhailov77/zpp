package org.vmtest.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.vmtest.persistence.service.UserService;

import java.util.Collection;
import java.util.HashSet;


/**
 * Created by victor on 27.10.15.
 */
@Service
public class DbAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        UserDetails loadedUser = null;

        try {
            org.vmtest.persistence.entity.User dbUser = userService.findUserByLoginName(userName);
            String password = (String) usernamePasswordAuthenticationToken.getCredentials();
            if (dbUser.getPassword().equals(password)) {
                Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                authorities.add(new GrantedAuthorityImpl("USER"));
                loadedUser = new User(dbUser.getUserName(), dbUser.getPassword(), authorities);
                logger.debug("User " + userName + " successfully registered");
            }
        } catch (Exception e) {
            logger.error(e);
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }

        if (loadedUser == null) {
            logger.error("User " + userName + " supplied bad credentials");
            throw new BadCredentialsException(
                    "Password mismatch");
        }
        return loadedUser;
    }
}
