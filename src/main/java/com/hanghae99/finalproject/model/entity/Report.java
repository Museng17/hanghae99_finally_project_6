package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.model.resultType.TimeStamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report extends TimeStamp {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private Users reporter;

    @ManyToOne
    private Folder badfolder;

    public Report(Users reporter, Folder badfolder){
        this.reporter = reporter;
        this.badfolder = badfolder;
    }
}
