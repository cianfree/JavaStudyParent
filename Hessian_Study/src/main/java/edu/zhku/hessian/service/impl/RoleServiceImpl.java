package edu.zhku.hessian.service.impl;

import edu.zhku.hessian.domain.Role;
import edu.zhku.hessian.domain.User;
import edu.zhku.hessian.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arvin on 2016/6/5.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final Map<Integer, Role> roleMap = new ConcurrentHashMap<>();

    public RoleServiceImpl() {
        roleMap.put(1, new Role(1, "超级管理员"));
        roleMap.put(2, new Role(2, "系统管理员"));
        roleMap.put(3, new Role(3, "档案管理员"));
        roleMap.put(4, new Role(4, "部门经理"));
        roleMap.put(5, new Role(5, "部门主管"));
    }

    @Override
    public Role getRole(Integer id) {
        return roleMap.get(id);
    }
}
