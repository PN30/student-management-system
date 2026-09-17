package com.studentms.model;

public abstract class Person {

    // Encapsulation: fields are private, accessed only through getters/setters
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Abstract method - forces subclasses to provide their own display logic
    public abstract String getDetails();
}
