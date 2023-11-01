package com.search.teacher.Techlearner.model.enums;

public enum RoleType {
    ROLE_STUDENT,
    ROLE_TEACHER,
    ROLE_ADMIN;


    public static RoleType getRoleByName(String name){
        for (RoleType roleType: RoleType.values()) {
            if (name.equals(roleType.name()))
                return roleType;
        }

        return null;
    }
}
