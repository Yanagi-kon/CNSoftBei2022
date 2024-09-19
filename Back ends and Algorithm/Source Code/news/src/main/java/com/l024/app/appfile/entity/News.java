package com.l024.app.appfile.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid")
    int id;

    @Column(name = "title")
    String title;

    @Column(name = "nclass")
    String nclass;

    @Column(name = "image")
    String image;

    @Column(name = "media")
    String media;

    @Column(name = "time")
    String time;

    @Lob
    @Column(name = "text",columnDefinition = "TEXT")
    String text;

    @Column(name = "sums")
    Integer sums;

    @Column(name = "theme")
    String theme;
    public News(String title, String nclass, String media, String time, String text) {
        this.title = title;
        this.nclass = nclass;
        this.media = media;
        this.time = time;
        this.text = text;
    }

    public News() {

    }
}
