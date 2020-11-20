package com.yashraj.bloodonation.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.bloodonation.R;

import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Spinner sp;
    String[] bloodgroup = {"Choose Bloodgroup", "A+", "O+", "B+", "AB+", "A-", "O-", "B-", "AB-"};
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser mUser;
    private EditText user_firstname, user_lastname, user_city, user_state, user_mobile, user_email, user_country, user_address, user_age;
    private Button sumitButton;
    private String str_firstname = "", str_lastname = "", str_bloodgroup = "", str_city = "", str_state = "", str_mobile = "", str_email = "", str_country = "", str_address = "", str_age = "";
    private ArrayAdapter<String> adp;
    private RelativeLayout no_network_state_layout,network_state_layout;
    private Button retry_button;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Donor");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        user_firstname = findViewById(R.id.user_firstname);
        user_lastname = findViewById(R.id.user_lastname);
        user_email = findViewById(R.id.user_email);
        user_mobile = findViewById(R.id.user_mobile);
        user_city = findViewById(R.id.user_city);
        user_state = findViewById(R.id.user_state);
        user_country = findViewById(R.id.user_country);
        user_address = findViewById(R.id.user_address);
        user_age = findViewById(R.id.user_age);
        sp = findViewById(R.id.user_bloodgroup);
        adp = new ArrayAdapter<String>(DetailsActivity.this, android.R.layout.simple_list_item_1, bloodgroup);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        str_mobile = mUser.getPhoneNumber();
        user_mobile.setText(str_mobile);
        sumitButton = findViewById(R.id.submitbutton);
        no_network_state_layout=findViewById(R.id.detail_no_network_state);
        network_state_layout=findViewById(R.id.detail_network_state);
        retry_button=findViewById(R.id.detail_retry_button);
        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        sumitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_age.getText().toString().isEmpty() && user_address.getText().toString().isEmpty() && user_state.getText().toString().isEmpty()
                        && user_lastname.getText().toString().isEmpty() && user_firstname.getText().toString().isEmpty()
                        && user_email.getText().toString().isEmpty() && user_country.getText().toString().isEmpty() && user_city.getText().toString().isEmpty()) {
                    if(user_state.getText().toString().trim().length()>4){
                        user_firstname.setError("Enter Firstname");
                        user_lastname.setError("Enter Lastname");
                        user_address.setError("Enter Address");
                        user_age.setError("Enter Age");
                        user_city.setError("Enter City");
                        user_state.setError("Enter State");
                        user_country.setError("Enter Country");
                        user_email.setError("Enter Email");
                        Toast.makeText(getApplicationContext(), "Provide Required Details!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DetailsActivity.this, "Enter Full State Name", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    saveDataofDonor();
                }
            }
        });
        retrieveData();
        user_mobile.setEnabled(false);

    }

    public void checkConnection(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconnection=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifi.isConnected()){
            no_network_state_layout.setVisibility(View.GONE);
            network_state_layout.setVisibility(View.VISIBLE);
        }else if(mobileconnection.isConnected()){
            no_network_state_layout.setVisibility(View.GONE);
            network_state_layout.setVisibility(View.VISIBLE);
        }else{
            no_network_state_layout.setVisibility(View.VISIBLE);
            network_state_layout.setVisibility(View.GONE);
        }

    }
    private void saveDataofDonor() {
        progressDialog.setTitle("Account Setting");
        progressDialog.setMessage("Saving account changes...");
        progressDialog.show();


        str_firstname = user_firstname.getText().toString();
        str_lastname = user_lastname.getText().toString();
        str_email = user_email.getText().toString();
        str_city = user_city.getText().toString();
        str_country = user_country.getText().toString();
        str_bloodgroup = sp.getSelectedItem().toString();
        str_state = user_state.getText().toString();
        str_address = user_address.getText().toString();
        str_age = user_age.getText().toString();

        str_state = str_state.toUpperCase();
        str_city = str_city.toUpperCase();
        str_bloodgroup = str_bloodgroup.toUpperCase();
        str_country = str_country.toUpperCase();

        str_mobile = mUser.getPhoneNumber();


        HashMap<String, Object> MapData = new HashMap<>();
        MapData.put("uid", mAuth.getCurrentUser().getUid());
        MapData.put("user_firstname", str_firstname);
        MapData.put("user_lastname", str_lastname);
        MapData.put("user_email", str_email);
        MapData.put("user_bloodgroup", str_bloodgroup);
        MapData.put("user_city", str_city);
        MapData.put("user_state", str_state);
        MapData.put("user_mobile", str_mobile);
        MapData.put("user_address", str_address);
        MapData.put("user_country", str_country);
        MapData.put("user_age", str_age);


        databaseReference.child(mUser.getUid()).updateChildren(MapData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(DetailsActivity.this, DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    Toast.makeText(DetailsActivity.this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), "Error:" + error, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });


    }

    private void retrieveData() {
        databaseReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sendUsertoMainActivity();
                    str_firstname = snapshot.child("user_firstname").getValue().toString();
                    str_lastname = snapshot.child("user_lastname").getValue().toString();
                    str_email = snapshot.child("user_email").getValue().toString();
                    str_bloodgroup = snapshot.child("user_bloodgroup").getValue().toString();
                    str_city = snapshot.child("user_city").getValue().toString();
                    str_state = snapshot.child("user_state").getValue().toString();
                    str_country = snapshot.child("user_country").getValue().toString();
                    str_mobile = snapshot.child("user_mobile").getValue().toString();
                    str_address = snapshot.child("user_address").getValue().toString();
                    str_age = snapshot.child("user_age").getValue().toString();

                    user_firstname.setText(str_firstname);
                    user_lastname.setText(str_lastname);
                    user_email.setText(str_email);
                    sp.setTooltipText(str_bloodgroup);
                    user_city.setText(str_city);
                    user_state.setText(str_state);
                    user_country.setText(str_country);
                    user_address.setText(str_address);
                    user_age.setText(str_age);
                    user_mobile.setEnabled(false);
//                    user_mobile.setText(str_mobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendUsertoMainActivity() {
        Intent intent = new Intent(DetailsActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
