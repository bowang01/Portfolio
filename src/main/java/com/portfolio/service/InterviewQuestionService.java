package com.portfolio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.domain.InterviewCategory;
import com.portfolio.domain.InterviewQuestion;
import com.portfolio.repo.InterviewQuestionRepository;
import com.portfolio.web.dto.InterviewQuestionForm;

@Service
public class InterviewQuestionService {

    private final InterviewQuestionRepository repository;

    public InterviewQuestionService(InterviewQuestionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<InterviewQuestion> list(InterviewCategory category, String query) {
        String q = query == null ? "" : query.trim();
        if (category == null && q.isEmpty()) {
            return repository.findAllByOrderByUpdatedAtDesc();
        }
        if (category == null) {
            return repository.findByQuestionContainingIgnoreCaseOrderByUpdatedAtDesc(q);
        }
        if (q.isEmpty()) {
            return repository.findByCategoryOrderByUpdatedAtDesc(category);
        }
        return repository.findByCategoryAndQuestionContainingIgnoreCaseOrderByUpdatedAtDesc(category, q);
    }

    @Transactional(readOnly = true)
    public InterviewQuestion get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview question not found"));
    }

    @Transactional
    public InterviewQuestion save(InterviewQuestionForm form) {
        InterviewQuestion item = form.getId() == null ? new InterviewQuestion() : get(form.getId());
        form.applyTo(item);
        return repository.save(item);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(get(id));
    }
}
