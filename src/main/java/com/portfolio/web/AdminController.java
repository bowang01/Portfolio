package com.portfolio.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import com.portfolio.domain.SiteSettings;
import com.portfolio.service.AdminUserService;
import com.portfolio.service.ProjectService;
import com.portfolio.service.SiteSettingsService;
import com.portfolio.web.dto.AdminAccountForm;
import com.portfolio.web.dto.ProjectForm;
import com.portfolio.web.dto.TestAccountItem;
import com.portfolio.web.dto.TestEndpointItem;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProjectService projectService;
    private final SiteSettingsService siteSettingsService;
    private final AdminUserService adminUserService;

    public AdminController(
            ProjectService projectService,
            SiteSettingsService siteSettingsService,
            AdminUserService adminUserService
    ) {
        this.projectService = projectService;
        this.siteSettingsService = siteSettingsService;
        this.adminUserService = adminUserService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(80);
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.listAll());
        return "admin/dashboard";
    }

    @GetMapping("/projects/new")
    public String createForm(Model model) {
        ProjectForm form = new ProjectForm();
        form.getTestAccounts().add(new TestAccountItem());
        form.getTestEndpoints().add(new TestEndpointItem());
        model.addAttribute("form", form);
        model.addAttribute("pageTitle", "New project");
        return "admin/project-form";
    }

    @GetMapping("/projects/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProjectForm form = ProjectForm.from(projectService.get(id));
        if (form.getTestAccounts().isEmpty()) {
            form.getTestAccounts().add(new TestAccountItem());
        }
        if (form.getTestEndpoints().isEmpty()) {
            form.getTestEndpoints().add(new TestEndpointItem());
        }
        model.addAttribute("form", form);
        model.addAttribute("pageTitle", "Edit project");
        return "admin/project-form";
    }

    @PostMapping("/projects")
    public String create(
            @Valid @ModelAttribute("form") ProjectForm form,
            BindingResult bindingResult,
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
            @RequestParam(value = "galleryFiles", required = false) MultipartFile[] galleryFiles,
            @RequestParam(value = "removeGalleryIds", required = false) List<Long> removeGalleryIds,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return persist(form, bindingResult, coverFile, galleryFiles, removeGalleryIds, model, redirectAttributes, "Project created");
    }

    @PostMapping("/projects/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") ProjectForm form,
            BindingResult bindingResult,
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
            @RequestParam(value = "galleryFiles", required = false) MultipartFile[] galleryFiles,
            @RequestParam(value = "removeGalleryIds", required = false) List<Long> removeGalleryIds,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        form.setId(id);
        return persist(form, bindingResult, coverFile, galleryFiles, removeGalleryIds, model, redirectAttributes, "Project saved");
    }

    @PostMapping("/projects/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Project deleted");
        return "redirect:/admin";
    }

    @GetMapping("/settings")
    public String settingsForm(Model model) {
        model.addAttribute("settings", siteSettingsService.get());
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@ModelAttribute("settings") SiteSettings settings, RedirectAttributes redirectAttributes) {
        siteSettingsService.save(settings);
        redirectAttributes.addFlashAttribute("message", "Site settings updated");
        return "redirect:/admin/settings";
    }

    @GetMapping("/account")
    public String accountForm(Model model) {
        model.addAttribute("form", new AdminAccountForm());
        return "admin/account";
    }

    @PostMapping("/account")
    public String saveAccount(
            Authentication authentication,
            @ModelAttribute("form") AdminAccountForm form,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        String newPassword = form.getNewPassword() == null ? "" : form.getNewPassword();
        String confirmPassword = form.getConfirmPassword() == null ? "" : form.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirmation do not match");
            return "admin/account";
        }
        try {
            adminUserService.changePassword(authentication.getName(), form.getCurrentPassword(), newPassword);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin/account";
        }
        redirectAttributes.addFlashAttribute("message", "Password updated");
        return "redirect:/admin/account";
    }

    private String persist(
            ProjectForm form,
            BindingResult bindingResult,
            MultipartFile coverFile,
            MultipartFile[] galleryFiles,
            List<Long> removeGalleryIds,
            Model model,
            RedirectAttributes redirectAttributes,
            String successMessage
    ) {
        if (bindingResult.hasErrors()) {
            restoreGallery(form);
            model.addAttribute("pageTitle", form.getId() == null ? "New project" : "Edit project");
            return "admin/project-form";
        }
        try {
            projectService.save(form, coverFile, toGalleryList(galleryFiles), removeGalleryIds);
        } catch (IllegalArgumentException ex) {
            restoreGallery(form);
            bindingResult.reject("cover", ex.getMessage());
            model.addAttribute("pageTitle", form.getId() == null ? "New project" : "Edit project");
            return "admin/project-form";
        }
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/admin";
    }

    private static List<MultipartFile> toGalleryList(MultipartFile[] galleryFiles) {
        if (galleryFiles == null || galleryFiles.length == 0) {
            return List.of();
        }
        return Arrays.asList(galleryFiles);
    }

    private void restoreGallery(ProjectForm form) {
        if (form.getId() == null) {
            return;
        }
        form.setGalleryImages(ProjectForm.from(projectService.get(form.getId())).getGalleryImages());
    }
}
