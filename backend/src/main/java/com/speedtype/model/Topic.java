package com.speedtype.model;

import jakarta.persistence.*;

/** Topics used to be a fixed Java enum. They're now a real table so an admin can
 *  add/rename/remove them at runtime — see AdminTopicController. `icon` is one of
 *  a small fixed set of keys the frontend knows how to render (see the admin
 *  service for the allowed list), not free text. */
@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String icon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
