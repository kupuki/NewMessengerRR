package com.example.messengerr;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messengerr.databinding.ActivityDiscussionBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {

    List<DiscussionItem> conversation = new ArrayList<>();
    private ActivityDiscussionBinding binding;
    private DatabaseReference dbr;
    private DiscussionAdapter adapter = new DiscussionAdapter(this);
    String SelectedTopic, user_msg_key;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiscussionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.lvConv.setAdapter(adapter);
        UserName = getIntent().getExtras().get("user_name").toString();
        SelectedTopic = getIntent().getExtras().get("selected_topic").toString();
        setTitle("Чат : " + SelectedTopic);
        dbr = FirebaseDatabase.getInstance().getReference().child(SelectedTopic);
        binding.btnsendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("msg", binding.etText.getText().toString());
                map2.put("user", UserName);
                dbr2.updateChildren(map2);
                binding.etText.getText().clear();
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

    public void updateConverstation(DataSnapshot snapshot) {
        String msg, user;
        Iterator<DataSnapshot> i = snapshot.getChildren().iterator();
        while (i.hasNext()) {
            msg = (String) (i.next()).getValue();
            user = (String) (i.next()).getValue();

            conversation.add(new DiscussionItem(user, msg));
        }
        adapter.submitList(conversation);
    }

}