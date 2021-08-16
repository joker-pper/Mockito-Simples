package com.joker17.mockito.simples;

import lombok.Data;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(Suite.class)
@Suite.SuiteClasses({MockAnnotationsTest.MockExtendTest.class, MockAnnotationsTest.MockRunnerTest.class})
public class MockAnnotationsTest {

    @Data
    static class UserRegisterModel {
        private String username;
        private String password;
        private String email;
    }

    @Data
    static class User {
        private Long id;
        private String username;
        private String password;
        private String email;
    }


    interface UserDao {
        User queryByUsername(String username);

        User saveUser(User user);
    }

    interface UserService {

        void register(UserRegisterModel userRegisterModel);

    }

    static class UserServiceImpl implements UserService {

        private UserDao userDao;

        public UserServiceImpl(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        public void register(UserRegisterModel userRegisterModel) {
            String username = userRegisterModel.getUsername();
            User user = userDao.queryByUsername(username);
            if (user != null) {
                throw new IllegalArgumentException(String.format("%s已被注册！", username));
            }
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(userRegisterModel.getPassword());
            newUser.setEmail(userRegisterModel.getEmail());
            userDao.saveUser(newUser);
        }
    }


    static class SpyExampleService {

        int calc(int a, int b) {
            return a + b;
        }

    }

    public static class BaseAnnotationsTest {

        private AutoCloseable closeable;

        @Before
        public void openMocks() {
            closeable = MockitoAnnotations.openMocks(this);
        }

        @After
        public void releaseMocks() throws Exception {
            closeable.close();
        }
    }


    public static class MockExtendTest extends BaseAnnotationsTest {
        @Mock
        private UserDao userDao;

        @InjectMocks
        private UserServiceImpl userService;

        @Spy
        private SpyExampleService spyExampleService;

        @Test
        public void test() {
            Assert.assertNotNull(userDao);
            Assert.assertNotNull(userService);
            Assert.assertEquals(UserServiceImpl.class, userService.getClass());

            Assert.assertNotNull(spyExampleService);
            Assert.assertEquals(SpyExampleService.class, spyExampleService.getClass());
            Assert.assertEquals(8, spyExampleService.calc(1, 7));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class MockRunnerTest {
        @Mock
        private UserDao userDao;

        @InjectMocks
        private UserServiceImpl userService;

        @Spy
        private SpyExampleService spyExampleService;

        @Test
        public void test() {
            Assert.assertNotNull(userDao);
            Assert.assertNotNull(userService);
            Assert.assertEquals(UserServiceImpl.class, userService.getClass());

            Assert.assertNotNull(spyExampleService);
            Assert.assertEquals(SpyExampleService.class, spyExampleService.getClass());
            Assert.assertEquals(8, spyExampleService.calc(1, 7));
        }
    }

}
