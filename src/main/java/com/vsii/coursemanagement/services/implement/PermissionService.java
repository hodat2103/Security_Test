package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.entities.Permission;
import com.vsii.coursemanagement.repositories.PermissionRepository;
import com.vsii.coursemanagement.services.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
