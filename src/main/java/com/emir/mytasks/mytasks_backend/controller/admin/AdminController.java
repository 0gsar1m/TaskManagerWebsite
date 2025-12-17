package com.emir.mytasks.mytasks_backend.controller.admin;

import com.emir.mytasks.mytasks_backend.repository.ProjectRepository;
import com.emir.mytasks.mytasks_backend.repository.TaskRepository;
import com.emir.mytasks.mytasks_backend.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public AdminController(UserRepository userRepository,
                           ProjectRepository projectRepository,
                           TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    // Login sayfası (SecurityConfig zaten /admin/login'i kullanıyor)
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";   // templates/admin/login.html
    }

    // Dashboard sayfası
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long userCount = userRepository.count();
        long projectCount = projectRepository.count();
        long taskCount = taskRepository.count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("projectCount", projectCount);
        model.addAttribute("taskCount", taskCount);

        return "admin/dashboard";  // templates/admin/dashboard.html
    }
}
