package com.vsii.coursemanagement.dtos.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RolePermissionResponse {

    private String roleName;

    private String permissionName;

    private String endPoint;

    private String httpMethod;
}
