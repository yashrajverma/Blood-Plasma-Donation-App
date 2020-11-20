package com.yashraj.bloodonation.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.bloodonation.Model.DonorDetails;
import com.yashraj.bloodonation.R;

import java.util.HashMap;

public class CovidCardActivity extends AppCompatActivity {
    Button submit_button;
    RadioButton q1_yes,q1_no,q2_yes, q2_no, q3_yes, q3_no, q4_yes, q4_no;
    private Toolbar toolbar;
    private DatabaseReference databaseReference,reference;
    private FirebaseAuth mAuth;
    private String str1="",str2="",str3="",str4="",user_firstname="",user_lastname="",useraddress="",usercity="",usermobile="",que1,que2,que3,que4;
    private RelativeLayout form_relative_layout,covid_relative_layout;
    private RecyclerView recyclerView;
    private ProgressDialog progressBar;
    private FirebaseRecyclerAdapter<DonorDetails,DonorViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_card);
        toolbar = findViewById(R.id.toolbar_covid);
        setActionBar(toolbar);
        submit_button = findViewById(R.id.covid_submit_button);
        form_relative_layout=findViewById(R.id.question_realtive_layout);
        covid_relative_layout=findViewById(R.id.covid_relative_layout);
        recyclerView=findViewById(R.id.covid_activity_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar=new ProgressDialog(this);

        q1_yes = findViewById(R.id.q1_yes_radio_button);
        q1_no = findViewById(R.id.q1_no_radio_button);
        q2_yes = findViewById(R.id.q2_yes_radio_button);
        q2_no = findViewById(R.id.q2_no_radio_button);
        q3_yes = findViewById(R.id.q3_yes_radio_button);
        q3_no = findViewById(R.id.q3_no_radio_button);
        q4_yes = findViewById(R.id.q4_yes_radio_button);
        q4_no = findViewById(R.id.q4_no_radio_button);
        que1= getResources().getString(R.string.que1);
        que2= getResources().getString(R.string.que2);
        que3= getResources().getString(R.string.que3);
        que4= getResources().getString(R.string.que4);
        Log.i("TAG", "onCreate: "+que1+"\n"+que2+" "+que3);


        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Covid_database");
        reference=FirebaseDatabase.getInstance().getReference().child("Donor");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CovidCardActivity.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataofUser();
                if(q1_yes.isChecked()){
                    str1=q1_yes.getText().toString();
                }if(q1_no.isChecked()){
                    str1=q1_no.getText().toString();
                }if(q2_yes.isChecked()){
                    str2=q2_yes.getText().toString();
                }if(q2_no.isChecked()){
                    str2=q2_no.getText().toString();
                }if(q3_yes.isChecked()){
                    str3=q3_yes.getText().toString();
                }if(q3_no.isChecked()){
                    str3=q3_no.getText().toString();
                }if(q4_yes.isChecked()){
                    str4=q4_yes.getText().toString();
                }if(q4_no.isChecked()){
                    str4=q4_no.getText().toString();
                }
                Log.i("TAG", "Radiooooooo Buttoonnnnnnnnn"+str1+"\n"+str2+"\n"+str3+"\n"+str4+"\n");
            }
        });

        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user_firstname=snapshot.child("user_firstname").getValue().toString();
                    user_lastname=snapshot.child("user_lastname").getValue().toString();
                    useraddress=snapshot.child("user_address").getValue().toString();
                    usercity=snapshot.child("user_city").getValue().toString();
                    usermobile=snapshot.child("user_mobile").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    form_relative_layout.setVisibility(View.GONE);
                    covid_relative_layout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void exitUser() {

    }

    private void saveDataofUser() {
        progressBar.setMessage("Saving Info...");
        progressBar.setCancelable(false);
        progressBar.show();
        HashMap<String,Object> mapdata=new HashMap<>();
        mapdata.put("uid",mAuth.getCurrentUser().getUid());
        mapdata.put("user_firstname",user_firstname);
        mapdata.put("user_lastname",user_lastname);
        mapdata.put("user_address",useraddress);
        mapdata.put("user_city",usercity);
        mapdata.put("user_mobile",usermobile);
        mapdata.put("que1",que1);
        mapdata.put("que2",que2);
        mapdata.put("que3",que3);
        mapdata.put("que4",que4);

        mapdata.put("que1_response",str1);
        mapdata.put("que2_response",str2);
        mapdata.put("que3_response",str3);
        mapdata.put("que4_response",str4);


        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(mapdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CovidCardActivity.this, "You will be Informed,When we need you.", Toast.LENGTH_SHORT).show();
                    form_relative_layout.setVisibility(View.GONE);
                    progressBar.dismiss();
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(CovidCardActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<DonorDetails> options=new FirebaseRecyclerOptions.Builder<DonorDetails>().setQuery(databaseReference,DonorDetails.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<DonorDetails, DonorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DonorViewHolder holder, final int i, @NonNull final DonorDetails donorDetails) {

                holder.donor_fistname.setText(donorDetails.getUser_firstname());
                holder.donor_lastname.setText(donorDetails.getUser_lastname());
                holder.donorcity.setText(donorDetails.getUser_city());
                holder.donormobile.setText(donorDetails.getUser_mobile());
                holder.donorstate.setText(donorDetails.getUser_state());
                holder.donoraddress.setText(donorDetails.getUser_address());
            }

            @NonNull
            @Override
            public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plasma_donor, viewGroup ,false);
                return new DonorViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
    public static class DonorViewHolder extends RecyclerView.ViewHolder{
        TextView donor_fistname,donor_lastname,donoraddress,donorcity,donorstate,donormobile;


        public DonorViewHolder(View itemView) {
            super(itemView);

            donor_fistname =itemView.findViewById(R.id.plasms_donor_firstname);
            donor_lastname=itemView.findViewById(R.id.plasms_donor_lastname);
            donoraddress=itemView.findViewById(R.id.plasms_donor_address);
            donorcity=itemView.findViewById(R.id.plasms_donor_city);
            donormobile=itemView.findViewById(R.id.plasms_donor_mobile);
            donorstate=itemView.findViewById(R.id.plasms_donor_state);

        }
    }
}
