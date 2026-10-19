package com.portfolio.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portfolio.domain.InterviewCategory;
import com.portfolio.service.InterviewQuestionService;
import com.portfolio.web.dto.InterviewQuestionForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/interviews")
public class InterviewController {

    private final InterviewQuestionService interviewQuestionService;

    public InterviewController(InterviewQuestionService interviewQuestionService) {
        this.interviewQuestionService = interviewQuestionService;
    }

    @GetMapping
    public String list(
            @RequestParam(value = "category", required = false) InterviewCategory category,
            @RequestParam(value = "q", required = false) String query,
            Model model
    ) {
        model.addAttribute("questions", interviewQuestionService.list(category, query));
        model.addAttribute("selectedCategory", category);
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("categories", InterviewCategory.values());
        return "admin/interviews";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new InterviewQuestionForm());
        model.addAttribute("pageTitle", "New question");
        model.addAttribute("categories", InterviewCategory.values());
        return "admin/interview-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", InterviewQuestionForm.from(interviewQuestionService.get(id)));
        model.addAttribute("pageTitle", "Edit question");
        model.addAttribute("categories", InterviewCategory.values());
        return "admin/interview-form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") InterviewQuestionForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return persist(form, bindingResult, model, redirectAttributes, "Question saved");
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") InterviewQuestionForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        form.setId(id);
        return persist(form, bindingResult, model, redirectAttributes, "Question saved");
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        interviewQuestionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Question deleted");
        return "redirect:/admin/interviews";
    }

    private String persist(
            InterviewQuestionForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            String successMessage
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", form.getId() == null ? "New question" : "Edit question");
            model.addAttribute("categories", InterviewCategory.values());
            return "admin/interview-form";
        }
        interviewQuestionService.save(form);
        redirectAttributes.addFlashAttribute("message", successMessage);
        return "redirect:/admin/interviews";
    }
}
