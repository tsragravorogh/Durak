package com.tsragravorogh;

import java.time.Instant;

public class Article {
    private Long id;
    private String title;
    private String body;
    private final Instant createdDt = Instant.now();

    private Long count = 0L;

    public Article(String title, String body) {
        this.id = this.count++;
        this.title = title;
        this.body = body;
    }

    public Article() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdDt=" + createdDt +
                ", count=" + count +
                '}';
    }
}
