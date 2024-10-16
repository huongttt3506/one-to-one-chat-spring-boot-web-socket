package com.example.websocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void saveUser(UserEntity user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(UserEntity user) {
        var storedUser = repository.findById(user.getId()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }
    public List<UserEntity> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
}
