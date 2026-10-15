package com.usermanagement.api.service;

import com.usermanagement.api.dto.*;
import com.usermanagement.api.entity.User;
import com.usermanagement.api.exception.*;
import com.usermanagement.api.mapper.UserMapper;
import com.usermanagement.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- ADMIN operations ----------

    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException("Email already used: " + request.email());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllActive() {
        return userRepository.findAllByActiveTrue().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(getActiveUser(id));
    }

    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = getActiveUser(id);
        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new EmailAlreadyUsedException("Email already used: " + request.email());
        }
        userMapper.updateEntity(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public void deactivate(Long id) {
        User user = getActiveUser(id);
        user.setActive(false);
        userRepository.save(user);
    }

    // ---------- Operations on the current user (/me) ----------

    @Transactional(readOnly = true)
    public UserResponse getMe(Long currentUserId) {
        return userMapper.toResponse(getActiveUser(currentUserId));
    }

    public UserResponse updateMe(Long currentUserId, UserUpdateRequest request) {
        return update(currentUserId, request);
    }

    public void changePassword(Long currentUserId, String oldPassword, String newPassword) {
        User user = getActiveUser(currentUserId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AccessDeniedException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // ---------- helper ----------

    private User getActiveUser(Long id) {
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}