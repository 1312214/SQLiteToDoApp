package com.duyhoang.sqlitetodoapp.bean;

/**
 * Created by rogerh on 5/12/2018.
 */

public class ToDo {

    private int id;
    private String todoString;
    private String place;;

    public ToDo(int id, String todoString, String place){
        this.id = id;
        this.todoString = todoString;
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public String getTodoString() {
        return todoString;
    }

    public String getPlace() {
        return place;
    }
}
