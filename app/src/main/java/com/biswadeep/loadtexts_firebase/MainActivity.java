package com.biswadeep.loadtexts_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText name, gender, age;
    Button send;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference reference;

    Upload upload;

    String name1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name= (EditText) findViewById(R.id.name_id);
        gender= (EditText) findViewById(R.id.gender_id);
        age= (EditText) findViewById(R.id.age_id);

        send= (Button) findViewById(R.id.send_id);

        upload= new Upload();

        recyclerView= (RecyclerView) findViewById(R.id.recycler_id);

        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference= database.getInstance().getReference().child("Data");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload.setName(name.getText().toString().trim());
                upload.setAge(age.getText().toString().trim());
                upload.setGender(gender.getText().toString().trim());

                String id= reference.push().getKey();
                reference.child(id).setValue(upload);
                Toast.makeText(MainActivity.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Upload> options= new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(reference, Upload.class)
                .build();

        FirebaseRecyclerAdapter<Upload,ViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Upload, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Upload model) {
                holder.setData(getApplicationContext(), model.getName(), model.getGender(), model.getAge());

                holder.setOnClickListener(new ViewHolder.clickListener() {
                    @Override
                    public void onitemlongclick(View view, int position) {
                        name1= getItem(position).getName();
                        
                        showDeleteDataDialog(name1);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row, parent, false);
                return new ViewHolder(view);
            }
        };
    }

    private void showDeleteDataDialog(String name1) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query= reference.orderByChild("name").equalTo(name1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(MainActivity.this, "Data deleted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog a1= builder.create();
        a1.show();
    }
}