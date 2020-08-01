package com.yashraj.bloodonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.yashraj.bloodonation.Model.DonorDetails;
import com.yashraj.bloodonation.R;

public class DashBoardActivity extends AppCompatActivity {
    private RecyclerView recyclerViewdashboard;
    private CardView cardViewdashboard;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recyclerViewdashboard=findViewById(R.id.recycler_view_dashboard);
        cardViewdashboard=findViewById(R.id.covid_card);
        mAuth=FirebaseAuth.getInstance();
        recyclerViewdashboard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewdashboard.hasFixedSize();
    }

}