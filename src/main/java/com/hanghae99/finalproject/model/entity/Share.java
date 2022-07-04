package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.util.TimeStamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Share extends TimeStamp {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private Users users;

    @OneToOne
    private Folder folder;

    @OneToOne
    private Board board;

}
