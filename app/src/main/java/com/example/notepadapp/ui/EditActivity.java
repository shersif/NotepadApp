package com.example.notepadapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notepadapp.R;
import com.example.notepadapp.bean.Notepad;
import com.example.notepadapp.db.MyDbHelper;
import com.example.notepadapp.utils.TimeUtil;

public class EditActivity extends AppCompatActivity {

    private MyDbHelper dbHelper;

    private EditText noteEt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new MyDbHelper(this);

        noteEt = findViewById(R.id.noteEt);
    }

    public void back(View view) {
        finish();
    }

    public void clearContent(View view) {
        noteEt.setText("");
    }

    public void saveNote(View view) {
        String content = noteEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            Toast.makeText(this,"保存内容不能为空！",Toast.LENGTH_LONG).show();
        }
        Notepad notepad = new Notepad(content, TimeUtil.getTime());
        dbHelper.insertNote(notepad);
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }
}