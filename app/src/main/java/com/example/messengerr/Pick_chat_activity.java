package com.example.messengerr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Pick_chat_activity extends AppCompatActivity {

    ListView lvTopic;
    ArrayList<String> ListOfDisc = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_chat_activity);
        lvTopic = (ListView) findViewById(R.id.lvTopic);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListOfDisc);
        lvTopic.setAdapter(arrayAdpt);
        getUserName();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        lvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DiscussionActivity.class);
                i.putExtra("selected_topic", ((TextView) view).getText().toString());
                i.putExtra("user_name", UserName);
                startActivity(i);
            }
        });

    }

    private void getUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText userName = new EditText(this);
        builder.setView(userName);
        builder.setTitle("Напишите никнейм!");

        builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserName = userName.getText().toString();
                if (UserName.equals("hello")) {
                    //System.exit(0);
                    userName.setTextColor(getResources().getColor(R.color.yellow));
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getUserName();
            }
        });
        builder.show();
    }
}