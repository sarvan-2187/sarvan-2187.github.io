package com.ngoconnect.service;

import com.ngoconnect.dto.UserRegistrationDto;
import com.ngoconnect.model.Role;
import com.ngoconnect.model.User;
import com.ngoconnect.repository.UserRepository;
import com.ngoconnect.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    public User registerUser(UserRegistrationDto registrationDto) {
        // Check if user already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Validate password
        if (!passwordUtil.isValidPassword(registrationDto.getPassword())) {
            throw new RuntimeException("Password must be at least 6 characters long and contain letters and numbers");
        }

        // Check password confirmation
        if (!registrationDto.isPasswordMatching()) {
            throw new RuntimeException("Passwords do not match");
        }

        // Create new user
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(passwordUtil.encodePassword(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPhone(registrationDto.getPhone());
        user.setIsVerified(true); // For demo purposes
        user.setIsActive(true);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean validatePassword(User user, String password) {
        return passwordUtil.matches(password, user.getPasswordHash());
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> findActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    public long countUsersByRole(Role role) {
        return userRepository.countByRole(role);
    }

    public void deactivateUser(Long userId) {
        User user = findById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = findById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }
}

