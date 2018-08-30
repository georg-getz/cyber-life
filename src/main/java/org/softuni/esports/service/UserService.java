package org.softuni.esports.service;

import org.softuni.esports.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.transaction.NotSupportedException;
import java.util.Set;

public interface UserService extends UserDetailsService {
    boolean createUser(UserServiceModel userServiceModel);

    Set<UserServiceModel> getAll();

    boolean promoteUser(String id);

    boolean demoteUser(String id);
}
