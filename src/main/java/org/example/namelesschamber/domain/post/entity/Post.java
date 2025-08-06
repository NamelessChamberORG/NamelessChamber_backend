package org.example.namelesschamber.domain.post.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "content")
@EqualsAndHashCode(of = "id")
public class Post {

    @Id
    private String id;

    private String content;
    //비회원 사용자 식별용 UUID
    @Indexed
    private String anonymousToken;
    //멤버 식별용 ID
    @Indexed
    private String userId;

    private PostType type;

    private boolean isDeleted;
    @CreatedDate
    private LocalDateTime createdAt;

}
