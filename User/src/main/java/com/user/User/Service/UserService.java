package com.user.User.Service;


import com.user.User.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService
{

    List<User> getAllUsers();

    Optional<User> findUserById(int id);
}
