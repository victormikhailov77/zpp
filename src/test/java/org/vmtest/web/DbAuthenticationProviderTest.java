package org.vmtest.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.vmtest.persistence.service.UserService;
import org.vmtest.web.authentication.DbAuthenticationProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by victor on 28.10.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DbAuthenticationProviderTest {

    class TestAuthenticationProvider extends DbAuthenticationProvider {
        @Override
        public UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
            return super.retrieveUser(userName, usernamePasswordAuthenticationToken);
        }
    }

    private static final String userName = "myuser";

    private static final String password = "mypassword";

    private TestAuthenticationProvider provider = new TestAuthenticationProvider();

    private UserService userService = mock(UserService.class);

    private UsernamePasswordAuthenticationToken userPasswordToken = mock(UsernamePasswordAuthenticationToken.class);

    private org.vmtest.persistence.entity.User dbUser = new org.vmtest.persistence.entity.User();

    @Before()
    public void initialize() {
        ReflectionTestUtils.setField(provider, "userService", userService);
        dbUser.setUserName(userName);
        dbUser.setPassword(password);
    }

    @Test
    public void shouldAuthenticateUserWithCorrectCredentials() {
        when(userService.findUserByLoginName(userName)).thenReturn(dbUser);
        when(userPasswordToken.getCredentials()).thenReturn(password);
        UserDetails userDetails = provider.retrieveUser(userName, userPasswordToken);
        verify(userService, times(1)).findUserByLoginName(userName);
        assertEquals(userName, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
    }

    @Test(expected = InternalAuthenticationServiceException.class)
    public void shouldRejectUserWithNonExistentLogin() {
        when(userService.findUserByLoginName(userName)).thenThrow(RuntimeException.class);
        UserDetails userDetails = provider.retrieveUser(userName, userPasswordToken);
        verify(userService, times(1)).findUserByLoginName(userName);
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldRejectUserWithMismatchedPassword() {
        when(userService.findUserByLoginName(userName)).thenReturn(dbUser);
        when(userPasswordToken.getCredentials()).thenReturn(null);
        UserDetails userDetails = provider.retrieveUser(userName, userPasswordToken);
        verify(userService, times(1)).findUserByLoginName(userName);
    }
}
