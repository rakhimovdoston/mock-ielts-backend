package com.search.teacher.model.enums;

public enum Degree {
    BACHELOR, MASTER, SCHOOL;

    public static Degree getDegree(String degree) {
        if (degree == null)
            return Degree.BACHELOR;
        for (Degree deg: Degree.values()) {
            if (degree.equals(deg.name()))
                return deg;
        }
        return Degree.BACHELOR;
    }
}
