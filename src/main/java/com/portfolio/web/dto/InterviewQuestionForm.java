package com.portfolio.web.dto;

import com.portfolio.domain.InterviewCategory;
import com.portfolio.domain.InterviewQuestion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InterviewQuestionForm {

    private Long id;

    @NotBlank(message = "Please enter a question")
    private String question;

    @NotNull(message = "Please choose a category")
    private InterviewCategory category = InterviewCategory.TECHNICAL;

    private String answer;

    public static InterviewQuestionForm from(InterviewQuestion item) {
        InterviewQuestionForm form = new InterviewQuestionForm();
        form.id = item.getId();
        form.question = item.getQuestion();
        form.category = item.getCategory();
        form.answer = item.getAnswer();
        return form;
    }

    public void applyTo(InterviewQuestion item) {
        item.setQuestion(question.trim());
        item.setCategory(category == null ? InterviewCategory.TECHNICAL : category);
        item.setAnswer(blankToNull(answer));
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public InterviewCategory getCategory() {
        return category;
    }

    public void setCategory(InterviewCategory category) {
        this.category = category;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
