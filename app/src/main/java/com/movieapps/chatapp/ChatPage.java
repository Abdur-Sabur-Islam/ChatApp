package com.movieapps.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatPage extends AppCompatActivity {
    private TextView mShowTextTv;
    private EditText mWriteMessageEt;
    private Button mSendMessageBtn;

    private String userName,groupName;

    private DatabaseReference root;
    private String unicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        mShowTextTv = (TextView) findViewById(R.id.showTextTv);
        mWriteMessageEt = (EditText) findViewById(R.id.writeMassageEt);
        mSendMessageBtn = (Button) findViewById(R.id.sendMessageBtn);

        userName = getIntent().getExtras().get("userName").toString();
        groupName = getIntent().getExtras().get("groupName").toString();

        setTitle("GroupName"+groupName);
        // firebase database Reference

        root = FirebaseDatabase.getInstance().getReference().child(groupName);

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                unicKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messageRoot = root.child(unicKey);

                Map<String,Object> mapForMessage = new HashMap<String, Object>();
                mapForMessage.put("Message",mWriteMessageEt.getText().toString());
                mapForMessage.put("userName",userName);

                messageRoot.updateChildren(mapForMessage);

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatDeatailWithShowText(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatDeatailWithShowText(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private  String massage,user;
    private void appendChatDeatailWithShowText(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            massage = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();
            mShowTextTv.append(user+"\n"+" :     "+massage+"\n");
        }
    }
}
