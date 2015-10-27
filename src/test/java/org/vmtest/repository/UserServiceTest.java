package org.vmtest.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.vmtest.TestConfiguration;
import org.vmtest.persistence.entity.User;
import org.vmtest.persistence.repository.UserRepository;
import org.vmtest.persistence.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by victor on 27.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceTest {

    private final String testLogin = "testUser";

    private final String testPassword = "testPassword";

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    private UserRepository repository = mock(UserRepository.class);

    private User testUser = new User();

    @Before
    public void initializeUser() {
        testUser.setUserName(testLogin);
        testUser.setPassword(testPassword);
        // ugly trick to inject mock into autowired dependency
        ReflectionTestUtils.setField(userService, "repository", repository);
    }

    @Test
    public void shouldReturnUserByLoginName() {
        when(repository.findByUserName(testLogin)).thenReturn(testUser);
        User found = userService.findUserByLoginName(testLogin);
        verify(repository, times(1)).findByUserName(testLogin);
        assertEquals(testUser, found);
    }

    @Test
    public void shouldAddNewUser() {
        userService.addNewUser(testUser);
        verify(repository, times(1)).findByUserName(testLogin);
        verify(repository, times(1)).save(testUser);
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowToAddDuplicateUser() {
        userService.addNewUser(testUser);
        when(repository.findByUserName(testLogin)).thenReturn(testUser);
        userService.addNewUser(testUser);
        verify(repository, times(1)).findByUserName(testLogin);
        verify(repository, times(0)).save(testUser);
    }
}
