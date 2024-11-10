package com.todoapp.business.category.domain;

public class Category {
    private Long id;
    private String label;

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }


}

