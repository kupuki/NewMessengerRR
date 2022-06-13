package com.example.messengerr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {

    Button btnSendMsg;
    EditText etMsg;
    ListView lvDisc;
    ArrayList<String> listCoverstation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    private DatabaseReference dbr;
    String  SelectedTopic, user_msg_key;
    String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        btnSendMsg = (Button) findViewById(R.id.btnsendmsg);
        etMsg = (EditText) findViewById(R.id.etText);
        lvDisc = (ListView) findViewById(R.id.lvConv);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listCoverstation);
        lvDisc.setAdapter(arrayAdpt);
        UserName = getIntent().getExtras().get("user_name").toString();
        SelectedTopic = getIntent().getExtras().get("selected_topic").toString();
        setTitle("Чат : " + SelectedTopic);
        dbr = FirebaseDatabase.getInstance().getReference().child(SelectedTopic);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("msg", etMsg.getText().toString());
                map2.put("user", UserName);
                dbr2.updateChildren(map2);
                etMsg.getText().clear();
            }
        });
        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConverstation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConverstation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateConverstation(DataSnapshot snapshot){
        String msg, user, conversation;
        Iterator i = snapshot.getChildren().iterator();
        while (i.hasNext()){
            msg = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();

            conversation = user + ": " + msg;
            arrayAdpt.insert(conversation,  arrayAdpt.getCount());
            arrayAdpt.notifyDataSetChanged();
        }
    }

}