package com.td.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Random;

@Data
@Entity
@Table(name="t_email")
public class Emailexd {

    public Emailexd(){
        Random random = new Random();
        long abs = Math.abs(random.nextLong());
        this.id = String.valueOf(abs);
    }

    @Id
    @Column(name="id")
    private String id;

    @Column(name="subject")
    private String subject;

    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @Lob
    @Column(name="content",columnDefinition="TEXT")
    private String content;

    @Column(name="t_from")
    private String from;

    @Column(name="t_to")
    private String to;

    @Column(name="t_cc")
    private String cc;

    @Column(name="t_bcc")
    private String bcc;

    @Column(name="box_type")
    private String boxType;

    @Column(name="sent_date")
    private String sentDate;

    @Column(name="message_id")
    private String messageId;
}
