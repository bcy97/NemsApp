package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "用户信息列表")
public class TestData {
    @SmartColumn(id = 1, name = "姓名")
    private String name;
    @SmartColumn(id = 2, name = "年龄")
    private int age;

    public String getName() {
        return name;
    }

    public TestData() {
    }

    public TestData(String name, int age) {
        this.name = name;
        this.age = age;
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
}
