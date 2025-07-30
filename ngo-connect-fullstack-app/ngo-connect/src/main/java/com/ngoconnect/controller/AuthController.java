package com.ngoconnect.controller;

import com.ngoconnect.dto.LoginDto;
import com.ngoconnect.dto.UserRegistrationDto;
import com.ngoconnect.model.Role;
import com.ngoconnect.model.User;
import com.ngoconnect.service.UserService;
import com.ngoconnect.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute LoginDto loginDto,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/login";
        }

        try {
            Optional<User> userOptional = userService.findByEmail(loginDto.getEmail());
            
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
                return "redirect:/auth/login";
            }

            User user = userOptional.get();
            
            if (!user.getIsActive()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Account is deactivated");
                return "redirect:/auth/login";
            }

            if (!userService.validatePassword(user, loginDto.getPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
                return "redirect:/auth/login";
            }

            // Store user in session
            session.setAttribute("currentUser", user);
            session.setAttribute("userRole", user.getRole().toString());

            redirectAttributes.addFlashAttribute("successMessage", "Welcome back, " + user.getFirstName() + "!");
            
            // Redirect based on role
            return switch (user.getRole()) {
                case ADMIN -> "redirect:/dashboard/admin";
                case NGO -> "redirect:/dashboard/ngo";
                case DONOR -> "redirect:/dashboard/donor";
                case VOLUNTEER -> "redirect:/dashboard/volunteer";
            };

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Login failed: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        model.addAttribute("roles", Role.values());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute UserRegistrationDto userRegistrationDto,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            User user = userService.registerUser(userRegistrationDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Please login to continue.");
            return "redirect:/auth/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out successfully");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        
        // Refresh user data from database
        currentUser = userService.findById(currentUser.getId());
        model.addAttribute("user", currentUser);
        return "auth/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        try {
            // Update only allowed fields
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setPhone(user.getPhone());
            
            User updatedUser = userService.updateUser(currentUser);
            session.setAttribute("currentUser", updatedUser);
            
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully");
            return "redirect:/auth/profile";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Profile update failed: " + e.getMessage());
            return "redirect:/auth/profile";
        }
    }
}

