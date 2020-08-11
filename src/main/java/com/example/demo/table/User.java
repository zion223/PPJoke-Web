package com.example.demo.table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象")
public class User {

    @ApiModelProperty(value = "用户Id", name = "id")
    private int id;

    @ApiModelProperty(value = "用户名", name = "name")
    private String name;

    @ApiModelProperty(value = "用户密码", name = "password")
    private String password;

    @ApiModelProperty(value = "用户手机号", name = "number")
    private String number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", password=" + password + ", number=" + number + "]";
    }
}
