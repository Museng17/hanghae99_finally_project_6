package com.hanghae99.finalproject.model.resultType;

public enum DisclosureStatusType {
    PUBLIC("public"),
    PRIVATE("private");

    private String status;

    DisclosureStatusType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public DisclosureStatusType setStatus(String status) {
        this.status = status;
        return this;
    }
}
