package com.ngoconnect.controller;

import com.ngoconnect.model.Role;
import com.ngoconnect.model.User;
import com.ngoconnect.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        // Add some sample data for the home page
        model.addAttribute("totalNGOs", userService.countUsersByRole(Role.NGO));
        model.addAttribute("totalDonors", userService.countUsersByRole(Role.DONOR));
        model.addAttribute("totalVolunteers", userService.countUsersByRole(Role.VOLUNTEER));
        
        return "public/home";
    }

    @GetMapping("/about")
    public String about() {
        return "public/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "public/contact";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        // Redirect to role-specific dashboard
        return switch (currentUser.getRole()) {
            case ADMIN -> "redirect:/dashboard/admin";
            case NGO -> "redirect:/dashboard/ngo";
            case DONOR -> "redirect:/dashboard/donor";
            case VOLUNTEER -> "redirect:/dashboard/volunteer";
        };
    }

    @GetMapping("/dashboard/donor")
    public String donorDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != Role.DONOR) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("pageTitle", "Donor Dashboard");
        return "dashboard/donor-dashboard";
    }

    @GetMapping("/dashboard/volunteer")
    public String volunteerDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != Role.VOLUNTEER) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("pageTitle", "Volunteer Dashboard");
        return "dashboard/volunteer-dashboard";
    }

    @GetMapping("/dashboard/ngo")
    public String ngoDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != Role.NGO) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("pageTitle", "NGO Dashboard");
        return "dashboard/ngo-dashboard";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("totalUsers", userService.findActiveUsers().size());
        model.addAttribute("totalNGOs", userService.countUsersByRole(Role.NGO));
        model.addAttribute("totalDonors", userService.countUsersByRole(Role.DONOR));
        model.addAttribute("totalVolunteers", userService.countUsersByRole(Role.VOLUNTEER));
        
        return "dashboard/admin-dashboard";
    }

    @GetMapping("/ngos")
    public String ngoDirectory(Model model) {
        model.addAttribute("pageTitle", "NGO Directory");
        return "public/ngo-directory";
    }

    @GetMapping("/campaigns")
    public String campaignDirectory(Model model) {
        model.addAttribute("pageTitle", "Campaigns");
        return "public/campaigns";
    }

    @GetMapping("/events")
    public String eventDirectory(Model model) {
        model.addAttribute("pageTitle", "Events");
        return "public/events";
    }

    @GetMapping("/transparency")
    public String transparencyPage(Model model) {
        model.addAttribute("pageTitle", "Transparency");
        return "public/transparency";
    }
}

