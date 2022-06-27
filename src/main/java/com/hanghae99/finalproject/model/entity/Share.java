package com.hanghae99.finalproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Share {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
}
