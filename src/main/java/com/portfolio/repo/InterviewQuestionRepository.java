package com.portfolio.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.domain.InterviewCategory;
import com.portfolio.domain.InterviewQuestion;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findAllByOrderByUpdatedAtDesc();

    List<InterviewQuestion> findByCategoryOrderByUpdatedAtDesc(InterviewCategory category);

    List<InterviewQuestion> findByQuestionContainingIgnoreCaseOrderByUpdatedAtDesc(String question);

    List<InterviewQuestion> findByCategoryAndQuestionContainingIgnoreCaseOrderByUpdatedAtDesc(
            InterviewCategory category,
            String question
    );
}
