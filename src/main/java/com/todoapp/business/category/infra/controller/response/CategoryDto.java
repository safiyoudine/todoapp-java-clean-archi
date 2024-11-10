package com.todoapp.business.category.infra.controller.response;

public class CategoryDto {
    private Long id;
    private String label;

    public CategoryDto() {
    }

    public CategoryDto(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
