package com.example.zivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Elderly_person_activities extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private myAdapterPostaroLice mAdapter;

    private List<Baranje> list = new ArrayList<>();

    private TextView dodadi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_person_activities);

        CreateList();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myAdapterPostaroLice(list, R.layout.baranje, this);
        mRecyclerView.setAdapter(mAdapter);

        dodadi = (Button) findViewById(R.id.dodadi);
    }
    private void CreateList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Baranja");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String key = user.getUid();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Baranje baranje = dataSnapshot.getValue(Baranje.class);
                    baranje.setAktivnostId(dataSnapshot.getKey());
                    if(baranje.getStatus().equals("На чекање")) {
                        baranje.setStatusId(1);
                        if(baranje.getUserId().equals(key))
                            list.add(baranje);
                    } else {
                        if(baranje.getStatus().equals("Активно")) {
                            baranje.setStatusId(2);
                        } else if(baranje.getStatus().equals("Закажано")) {
                            baranje.setStatusId(3);
                        } else if(baranje.getStatus().equals("Завршено") && baranje.getOcenaVolonter() == 0) {
                            baranje.setStatusId(4);
                        } else {
                            baranje.setStatusId(5);
                        }
                        if(baranje.getUserId().equals(key))
                            list.add(baranje);
                    }
                }
                Collections.sort(list, new Comparator<Baranje>() {
                    @Override
                    public int compare(Baranje baranje1, Baranje baranje2) {
                        return Integer.valueOf(baranje1.getStatusId()).compareTo(baranje2.getStatusId());
                    }
                });

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Elderly_person_activities.this, "Настана грешка.Обидете се повторно!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void dodadi(View view) {
        startActivity(new Intent(Elderly_person_activities.this, Elderly_person.class));
    }
}