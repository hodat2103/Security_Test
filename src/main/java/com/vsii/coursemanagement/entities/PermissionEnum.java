package com.vsii.coursemanagement.entities;

public enum PermissionEnum {

    ADMIN_RETRIEVE("admin:retrieve"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    USER_RETRIEVE("user:retrieve");

    private final String permissionName;

    PermissionEnum(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
