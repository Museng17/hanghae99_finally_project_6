package com.hanghae99.finalproject.util;

public enum DisclosureStatus {
    PUBLIC("public"),
    PRIVATE("private");

    private String status;

    DisclosureStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public DisclosureStatus setStatus(String status) {
        this.status = status;
        return this;
    }
}
