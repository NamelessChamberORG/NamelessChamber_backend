package org.example.namelesschamber.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public int getCoin(String userId) {
        return findUserById(userId).getCoin();

    }

    @Transactional
    public int rewardForPost(String userId, int amount) {
        User user = findUserById(userId);
        user.addCoin(amount);
        userRepository.save(user);
        return user.getCoin();
    }

    @Transactional
    public int chargeForRead(String userId, int amount) {
        User user = findUserById(userId);
        user.decreaseCoin(amount);
        userRepository.save(user);
        return user.getCoin();
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
