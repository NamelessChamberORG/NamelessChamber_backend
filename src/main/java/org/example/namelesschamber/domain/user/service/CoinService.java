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
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getCoin();
    }

    @Transactional
    public int rewardForPost(String userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.addCoin(amount);
        userRepository.save(user);
        return user.getCoin(); // 최신 잔액 반환
    }

    @Transactional
    public int chargeForRead(String userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.getCoin() < amount) throw new CustomException(ErrorCode.NOT_ENOUGH_COIN);
        user.decreaseCoin(amount);
        userRepository.save(user);
        return user.getCoin();
    }
}
