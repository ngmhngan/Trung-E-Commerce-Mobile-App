package com.mcommerce.model;

public class Ingredient {
    public static final String LABEL_BOT ="Bột";
    public static final String LABEL_SUAKEM ="Sữa-Kem";
    public static final String LABEL_BO ="Bơ";
    public static final String LABEL_KHAC ="Khác";
    private long id;
    private String name, label;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public Ingredient() {
    }

}
