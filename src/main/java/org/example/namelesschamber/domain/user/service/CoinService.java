package org.example.namelesschamber.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public int getCoin(String userId) {
        return findUserById(userId).getCoin();

    }

    public int rewardForPost(String userId, int amount) {
        Query q = Query.query(Criteria.where("_id").is(userId));
        Update u = new Update().inc("coin", amount);
        FindAndModifyOptions opt = FindAndModifyOptions.options().returnNew(true);
        User after = mongoTemplate.findAndModify(q, u, opt, User.class);
        if (after == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return after.getCoin();
    }

    public boolean chargeIfEnough(String userId, int amount) {
        Query q = Query.query(Criteria.where("_id").is(userId).and("coin").gte(amount));
        Update u = new Update().inc("coin", -amount);
        FindAndModifyOptions opt = FindAndModifyOptions.options().returnNew(true);
        User after = mongoTemplate.findAndModify(q, u, opt, User.class);
        return after != null;
    }

    public void refund(String userId, int amount) {
        Query q = Query.query(Criteria.where("_id").is(userId));
        Update u = new Update().inc("coin", amount);
        FindAndModifyOptions opt = FindAndModifyOptions.options().returnNew(true);
        User after = mongoTemplate.findAndModify(q, u, opt, User.class);
        if (after == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
