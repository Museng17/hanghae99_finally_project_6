package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.model.resultType.TimeStamp;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Share extends TimeStamp {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private Users users;

    @OneToOne
    private Folder folder;

    public Share(Folder folder, Users users) {
        this.folder = folder;
        this.users = users;
    }

}
