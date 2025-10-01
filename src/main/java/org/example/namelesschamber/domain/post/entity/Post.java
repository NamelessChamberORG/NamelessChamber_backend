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

    private String title;

    private String content;

    //멤버 식별용 ID
    @Indexed
    private String userId;

    private PostType type;

    @Builder.Default
    private boolean isDeleted = false;

    @Builder.Default
    private long views = 0L;

    @Builder.Default
    private long likes = 0L;

    @CreatedDate
    private LocalDateTime createdAt;

    public void increaseViews() {
        this.views += 1;
    }

    public void updateByAdmin(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void deleteByAdmin() {
        this.isDeleted = true;
    }
}



