package com.td.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Random;

@Data
@Entity
@Table(name="t_email")
public class Emailexd {

    public Emailexd(){
        Random random = new Random();
        this.id = Math.abs(random.nextLong());
    }

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="subject")
    private String subject;

    @Column(name="content")
    private String content;

    @Column(name="t_from")
    private String from;

    @Column(name="t_to")
    private String to;

    @Column(name="box_type")
    private String boxType;

}
