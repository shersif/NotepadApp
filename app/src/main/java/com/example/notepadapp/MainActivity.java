package com.example.notepadapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepadapp.bean.Notepad;
import com.example.notepadapp.db.MyDbHelper;
import com.example.notepadapp.ui.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 日记列表
    private RecyclerView listRV;

    // 数据库帮助类
    private MyDbHelper dbHelper;

    NotepadListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper=new MyDbHelper(this);

        findViewById(R.id.addIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });

        listRV = findViewById(R.id.listRv);
        adapter = new NotepadListAdapter();
        listRV.setAdapter(adapter);
        // 添加横线
        listRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Notepad> notepads = dbHelper.findAll();
        adapter.updateList(notepads);
    }

    class NotepadListAdapter extends RecyclerView.Adapter<NotepadListAdapter.ViewHolder> {

        private final List<Notepad> notepads=new ArrayList<>();


        @SuppressLint("NotifyDataSetChanged")
        public void updateList(List<Notepad> list){
            notepads.clear();
            notepads.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=View.inflate(MainActivity.this,R.layout.item_list,null);
            return new ViewHolder(view);
        }

        // 设置数据
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Notepad notepad = notepads.get(position);
            holder.content.setText(notepad.getContent());
            holder.time.setText(notepad.getTime());

            // 长按删除
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDeleteDialog(notepad);
                    return false;
                }
            });

            // 单击修改记录
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity=(MainActivity)view.getContext();
                    Intent intent = new Intent(activity, EditActivity.class);
                    intent.putExtra("notepadId",notepad.getId());
                    activity.startActivityForResult(intent,100);
                }
            });
        }

        private void showDeleteDialog(Notepad notepad){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("温馨提示")
                    .setMessage("确定要删除记录吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "已取消删除！", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.delete(notepad);
                            Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            updateList(dbHelper.findAll());
                        }
                    })
                    .create();
            alertDialog.show();
        }

        @Override
        public int getItemCount() {
            return notepads.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView content,time;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                content = itemView.findViewById(R.id.contentTv);
                time = itemView.findViewById(R.id.timeTv);
            }
        }
    }


}