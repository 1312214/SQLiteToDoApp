package com.duyhoang.sqlitetodoapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duyhoang.sqlitetodoapp.bean.ToDo;
import com.duyhoang.sqlitetodoapp.db.ToDoDBListDBAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtToDoList;
    Button btnAdd, btnRemove, btnModify;
    EditText etAdd, etRemove, etModify, etPlace;

    ToDoDBListDBAdapter myToDoListDBAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToDoListDBAdapter = ToDoDBListDBAdapter.getToDoListDBAdapterInstance(this);

        txtToDoList = (TextView)findViewById(R.id.text_todo_list);
        btnAdd = (Button)findViewById(R.id.button_add);
        btnModify = (Button)findViewById(R.id.button_modify);
        btnRemove = (Button)findViewById(R.id.button_remove);
        etAdd = (EditText)findViewById(R.id.edit_text_add_todo);
        etRemove = (EditText)findViewById(R.id.edit_text_remove);
        etModify = (EditText)findViewById(R.id.edit_text_modify);
        etPlace = (EditText)findViewById(R.id.edit_text_add_place);

        btnRemove.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        setAllToDoList();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add: addToDo(); break;
            case R.id.button_remove: removeToDo(); break;
            case R.id.button_modify: modifyToDo(); break;
        }
    }

    private void removeToDo() {
        myToDoListDBAdapter.remove(etRemove.getText().toString());
        setAllToDoList();
    }

    private void modifyToDo() {
        myToDoListDBAdapter.modify(etRemove.getText().toString(), etModify.getText().toString());
        setAllToDoList();

    }

    private void addToDo() {
        myToDoListDBAdapter.add(etAdd.getText().toString(), etPlace.getText().toString());
        setAllToDoList();

    }


    private String getAllToDoListString(){
        StringBuilder builder = new StringBuilder();

        List<ToDo> toDoList = myToDoListDBAdapter.getAllToDoList();

        if(toDoList != null && toDoList.size() > 0){
            for(ToDo toDo: toDoList){
                builder.append("" + toDo.getId() + ", " + toDo.getTodoString() + ", " + toDo.getPlace() + "\n");
            }
            return builder.toString();
        } else{
            return "NO HAVING TODO";
        }
    }

    private void setAllToDoList(){
        txtToDoList.setText(getAllToDoListString());
    }



}
