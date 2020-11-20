package com.yashraj.bloodonation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.bloodonation.Model.DonorDetails;
import com.yashraj.bloodonation.R;

public class FindDonorActivity extends AppCompatActivity {

    private static String str_city = "";
    EditText searchEt;
    String str = "";
    DatabaseReference databaseReference;
    RecyclerView findDonorrecyclerView;
    FirebaseRecyclerAdapter<DonorDetails, DonorViewHolder> firebaseRecyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finddonor);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Donor");
        searchEt = findViewById(R.id.searchET);
        findDonorrecyclerView = findViewById(R.id.find_donor_recyclerView);
        findDonorrecyclerView.setHasFixedSize(true);
        findDonorrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


        databaseReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("user_city")) {
                    str_city = snapshot.child("user_city").getValue().toString();
                    Log.i("TAG", "onDataChange: " + str_city);
                    Toast.makeText(FindDonorActivity.this, "Swipe to Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchEt.getText().toString().equals("")) {
                    Toast.makeText(FindDonorActivity.this, "Write City to Search", Toast.LENGTH_SHORT).show();
                } else {
                    str = s.toString();
                    str=str.toUpperCase();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDonor();
            }
        });
        LoadDonor();
    }

    public void LoadDonor(){
        FirebaseRecyclerOptions<DonorDetails> options = null;
        if (str.equals("")) {
            options = new FirebaseRecyclerOptions.Builder<DonorDetails>().setQuery(databaseReference.orderByChild("user_city").equalTo(str_city), DonorDetails.class).build();
        } else {
            options = new FirebaseRecyclerOptions.Builder<DonorDetails>()
                    .setQuery(databaseReference
                                    .orderByChild("user_city")
                                    .startAt(str)
                                    .endAt(str + "\uf8ff"),
                            DonorDetails.class)
                    .build();
        }

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DonorDetails, DonorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DonorViewHolder holder, final int i, @NonNull final DonorDetails donorDetails) {
                holder.bloodgroup.setText(donorDetails.getUser_bloodgroup());
                holder.donoraddress.setText(donorDetails.getUser_address());
                holder.donor_fistname.setText(donorDetails.getUser_firstname());
                holder.donor_lastname.setText(donorDetails.getUser_lastname());
                holder.donorcity.setText(donorDetails.getUser_city());
                holder.donormobile.setText(donorDetails.getUser_mobile());
                holder.donorstate.setText(donorDetails.getUser_state());
                swipeRefreshLayout.setRefreshing(false);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(i).getKey();
                        Intent sendActivityIntent = new Intent(FindDonorActivity.this, DonorProfileActivity.class);
                        sendActivityIntent.putExtra("visit_user_id", visit_user_id);
                        sendActivityIntent.putExtra("user_firstname", donorDetails.getUser_firstname());
                        sendActivityIntent.putExtra("user_lastname", donorDetails.getUser_lastname());
                        sendActivityIntent.putExtra("user_bloodgroup", donorDetails.getUser_bloodgroup());
                        sendActivityIntent.putExtra("user_address", donorDetails.getUser_address());
                        sendActivityIntent.putExtra("user_state", donorDetails.getUser_state());
                        sendActivityIntent.putExtra("user_mobile", donorDetails.getUser_mobile());
                        sendActivityIntent.putExtra("user_city", donorDetails.getUser_city());
                        startActivity(sendActivityIntent);
                        Animatoo.animateSlideLeft(FindDonorActivity.this);
                    }
                });
            }

            @NonNull
            @Override
            public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.donor_layout, viewGroup, false);
                return new DonorViewHolder(view);
            }
        };
        findDonorrecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Animatoo.animateInAndOut(FindDonorActivity.this);
        return super.onKeyDown(keyCode, event);
    }

    public static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView bloodgroup, donor_fistname, donor_lastname, donoraddress, donorcity, donorstate, donormobile;


        public DonorViewHolder(View itemView) {
            super(itemView);

            bloodgroup = itemView.findViewById(R.id.blood_group_donortext);
            donor_fistname = itemView.findViewById(R.id.donor_firstname);
            donor_lastname = itemView.findViewById(R.id.donor_lastname);
            donoraddress = itemView.findViewById(R.id.donor_address);
            donorcity = itemView.findViewById(R.id.donor_city);
            donormobile = itemView.findViewById(R.id.donor_mobile);
            donorstate = itemView.findViewById(R.id.donor_state);

        }
    }
}
