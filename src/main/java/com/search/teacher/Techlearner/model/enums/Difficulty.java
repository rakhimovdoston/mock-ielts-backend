package com.search.teacher.Techlearner.model.enums;

public enum Difficulty {
    hard, medium, easy;

    public static Difficulty getValue(String difficulty) {
        for (Difficulty diff: Difficulty.values()) {
            if (diff.name().equals(difficulty))
                return diff;
        }
        return null;
    }
}
