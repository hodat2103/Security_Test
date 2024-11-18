package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.entities.Role;
import com.vsii.coursemanagement.repositories.RoleRepository;
import com.vsii.coursemanagement.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    public Role getRoleWithPermissions(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }
}
