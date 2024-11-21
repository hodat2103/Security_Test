package com.vsii.coursemanagement.services;

import java.util.Map;
import java.util.Set;

public interface IPermissionService {
//    public List<Permission> getAllPermissions();
    public Map<String, Map<String, Set<String>>> loadPermissions();
}
