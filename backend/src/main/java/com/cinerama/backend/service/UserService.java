package com.cinerama.backend.service;

import java.io.ByteArrayInputStream;

public interface UserService {
    ByteArrayInputStream exportUsersToExcel();
}
