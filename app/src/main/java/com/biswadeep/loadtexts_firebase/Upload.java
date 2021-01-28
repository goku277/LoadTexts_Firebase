package com.biswadeep.loadtexts_firebase;

public class Upload {
    private String name, gender, age;

    public Upload(String name, String gender, String age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public Upload() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}