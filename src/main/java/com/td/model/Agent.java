package com.td.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Random;

@Data
@Entity
@Table(name="t_agent")
public class Agent {

    public Agent(){
        Random random = new Random();
        this.id = random.nextLong();
    }

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="account")
    private String account;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;
}
