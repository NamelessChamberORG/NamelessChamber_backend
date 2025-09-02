package org.example.namelesschamber.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.feedback.dto.request.FeedbackRequestDto;
import org.example.namelesschamber.domain.feedback.entity.Feedback;
import org.example.namelesschamber.domain.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void createFeedback(FeedbackRequestDto request, String userId) {
        Feedback feedback = Feedback.create(userId, request.content());
        feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}
