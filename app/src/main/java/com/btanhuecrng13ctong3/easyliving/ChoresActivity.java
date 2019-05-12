package com.btanhuecrng13ctong3.easyliving;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChoresActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private DatabaseReference usernameGet;

    ArrayList<CHORES_OBJECT> items;


    FirebaseUser user;
    String[] test = new String[1];
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference("Chores");


        items = new ArrayList<CHORES_OBJECT>();
        // Set up the buttons

        Log.d("testing", "about to do valueeventlistener");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("testing", "doing shit");

                for (DataSnapshot snapShot: dataSnapshot.getChildren()) {
                    //Log.i("testing", zoneShapshot.child("CHORE_NAME").getValue(String.class));
//                    String user = snapShot.child("")
//                    String chore_name = snapShot.child("CHORE_NAME").getValue(String.class);
                    String user = snapShot.child("USER_ID").getValue(String.class);
                    String chore_name = snapShot.child("CHORE_NAME").getValue(String.class);
                    Boolean chore_status = snapShot.child("CHORE_DONE").getValue(Boolean.class);
                    CHORES_OBJECT obj = new CHORES_OBJECT(user, chore_name, chore_status);
                    items.add(obj);

                }
                createList(items);

                for (int i=0; i<items.size(); i++) {
                    Log.d("testing22", items.get(i).CHORE_NAME);
                }
//                LinearLayout layout = findViewById(R.id.choresLayout);
//                layout.removeAllViews();
//                layout.removeAllViewsInLayout();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Log.d("testing", String.valueOf(items.size()));
        for (int i = 0; i < items.size(); i++) {
            Log.d("testing", items.get(i).CHORE_NAME);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();
                Log.d("testing", user.toString());
//                final DatabaseReference data = FirebaseDatabase.getInstance();
//                DatabaseReference mDataBase;
//                mDataBase = FirebaseDatabase.getInstance().getReference();


                Log.d("testing", "about to write value");


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChoresActivity.this);
                mBuilder.setTitle("Add a chore!");
                final EditText input = new EditText(ChoresActivity.this);
                mBuilder.setView(input);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //m_Text = input.getText().toString();
                        //items.add(new CHORES_OBJECT(user.getDisplayName(), input.getText().toString(), false));
                        //myRef.child("Chores").child(input.getText().toString()).setValue(new CHORES_OBJECT(user.getDisplayName(), input.getText().toString(), false));
                        //items.add(new CHORES_OBJECT(user.getEmail(), input.getText().toString(), false));
                        databaseReference.child(input.getText().toString()).setValue(new CHORES_OBJECT(user.getEmail(), input.getText().toString(), false));


                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mBuilder.show();

            }
        });




    }



        public void createList(ArrayList<CHORES_OBJECT> data_list) {

        LinearLayout layout = findViewById(R.id.choresLayout);
        layout.removeAllViews();

        for (int i=0; i<data_list.size(); i++) {
            TextView user =(TextView) new TextView(this);
            TextView chore = (TextView) new TextView(this);
            CheckBox checkBox = (CheckBox) new CheckBox(this);

            CHORES_OBJECT obj = data_list.get(i);


            user.setId(i);
            user.setText(obj.USER_ID);

            chore.setId(i);
            chore.setText(obj.CHORE_NAME);

            checkBox.setId(i);
            checkBox.setChecked(obj.CHORE_DONE);


            layout.addView(user);
            setTextViewAttributes(user);
            layout.addView(chore);
            setTextViewAttributes(chore);
            layout.addView(checkBox);
            setCheckBoxAttributes(checkBox);
            onCheckBoxClicked(checkBox, obj);


        }
        data_list.clear();
    }

    public void onCheckBoxClicked(final CheckBox checkBox, CHORES_OBJECT object) {
        final CHORES_OBJECT object1 = object;
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code to be executed on click :)
                //databaseReference.child(input.getText().toString()).setValue(new CHORES_OBJECT(user.getEmail(), input.getText().toString(), false));
                databaseReference.child(object1.CHORE_NAME).setValue(new CHORES_OBJECT(object1.USER_ID, object1.CHORE_NAME, !object1.CHORE_DONE));
            }
        });
    }


    private void setCheckBoxAttributes(CheckBox checkBox) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        checkBox.setLayoutParams(params);

        //This is used to place the checkbox on the right side of the textview
        //By default, the checkbox is placed at the left side
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple,
                typedValue, true);

        checkBox.setButtonDrawable(null);
        checkBox.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                typedValue.resourceId, 0);
    }

    private void setTextViewAttributes(TextView textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                0, 0
        );

        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(params);
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }




}
