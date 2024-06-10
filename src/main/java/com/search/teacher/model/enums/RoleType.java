package com.search.teacher.model.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_STUDENT("Student"),
    ROLE_TEACHER("Teacher"),
    ROLE_ADMIN("Admin");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    public static RoleType getRoleByName(String name){
        for (RoleType roleType: RoleType.values()) {
            if (name.equals(roleType.getValue()))
                return roleType;
        }

        return null;
    }

}
