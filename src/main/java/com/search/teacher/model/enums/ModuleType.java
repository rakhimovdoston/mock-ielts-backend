package com.search.teacher.model.enums;

public enum ModuleType {
    READING, LISTENING, WRITING, SPEAKING;

    public static ModuleType getModuleType(String type) {
        for (ModuleType moduleType : ModuleType.values()) {
            if (moduleType.toString().equals(type.toUpperCase())) {
                return moduleType;
            }
        }
        return ModuleType.READING;
    }
}
