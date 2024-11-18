package com.vsii.coursemanagement.services;

import com.vsii.coursemanagement.entities.Role;
import org.springframework.stereotype.Service;

@Service
public interface IRoleService {
    public Role getRoleWithPermissions(String roleName);
}
