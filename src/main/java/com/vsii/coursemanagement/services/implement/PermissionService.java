package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.entities.Permission;
import com.vsii.coursemanagement.repositories.PermissionRepository;
import com.vsii.coursemanagement.services.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {
    private final PermissionRepository permissionRepository;


    @Override
    public Map<String, Map<String, Set<String>>> loadPermissions() {
        List<Permission> permissions = permissionRepository.findAll();

        Map<String, Map<String, Set<String>>> permissionMap = new HashMap<>();

        for (Permission permission : permissions) {

            permissionMap.computeIfAbsent(permission.getEndPoint(), k -> new HashMap<>()) // create endpoint if not have in the map
                    .computeIfAbsent(permission.getHttpMethod().name(), k -> new HashSet<>())// create http if not have
                    .add(permission.getName()); // add author to Set (auto access if distinct author)
        }

        return permissionMap;
    }
}

