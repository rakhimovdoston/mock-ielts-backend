package com.search.teacher.model.enums;

public enum TestTime {
    morning, afternoon, evening;

    public static TestTime getValue(String testTime) {
        for (TestTime time : TestTime.values()) {
            if (time.name().equals(testTime))
                return time;
        }
        return TestTime.morning;
    }
}
