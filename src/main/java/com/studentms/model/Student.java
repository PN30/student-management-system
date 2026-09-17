package com.studentms.model;


public class Student extends Person {

    private int studentId;
    private String course;
    private double grade;

    public Student(int studentId, String name, int age, String course, double grade) {
        super(name, age);
        this.studentId = studentId;
        this.course = course;
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String getDetails() {
        return String.format(
            "ID: %-5d | Name: %-15s | Age: %-3d | Course: %-10s | Grade: %.2f",
            studentId, getName(), getAge(), course, grade
        );
    }
}
