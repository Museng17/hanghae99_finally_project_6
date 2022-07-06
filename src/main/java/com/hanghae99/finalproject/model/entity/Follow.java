package com.hanghae99.finalproject.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Follow {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private Users following;

    @ManyToOne
    private Users follower;

    @Builder
    public Follow(Users following, Users follower) {

        this.following = following;
        this.follower = follower;
    }
}
