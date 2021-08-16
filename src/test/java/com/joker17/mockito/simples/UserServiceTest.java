package com.joker17.mockito.simples;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class UserServiceTest {

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


    @Test
    public void testRegister() {
        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.queryByUsername("zhangsan123")).thenReturn(null);
        UserService userService = new UserServiceImpl(userDao);
        UserRegisterModel userRegisterModel = new UserRegisterModel();
        userRegisterModel.setUsername("zhangsan123");
        userRegisterModel.setPassword("zhangsan123");
        userRegisterModel.setEmail("zhangsan123@163.com");
        userService.register(userRegisterModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterWithUserExist() {
        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.queryByUsername("jack")).thenReturn(getMockJackUser());
        UserService userService = new UserServiceImpl(userDao);
        UserRegisterModel userRegisterModel = new UserRegisterModel();
        userRegisterModel.setUsername("jack");
        userRegisterModel.setPassword("jack12312");
        userRegisterModel.setEmail("jack111222@163.com");
        try {
            userService.register(userRegisterModel);
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex instanceof IllegalArgumentException);
            Assert.assertTrue("jack已被注册！".equals(ex.getMessage()));
            throw ex;
        }
    }


    private User getMockJackUser() {
        User user = new User();
        user.setId(12L);
        user.setUsername("jack");
        user.setPassword("jack111");
        user.setEmail("jack@163.com");
        return user;
    }
}
