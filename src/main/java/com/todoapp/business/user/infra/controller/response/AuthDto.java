package com.todoapp.business.user.infra.controller.response;

public class AuthDto {

    private String jwt;
    private String userRole;
    private Long userId;
    private boolean isSuccess;

    public AuthDto() {
        super();
    }

    public AuthDto(String jwt, String userRole, Long userId, boolean isSuccess) {
        super();
        this.jwt = jwt;
        this.userRole = userRole;
        this.userId = userId;
        this.isSuccess = isSuccess;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
