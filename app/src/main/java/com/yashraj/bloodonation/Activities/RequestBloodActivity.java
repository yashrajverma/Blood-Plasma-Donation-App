package com.yashraj.bloodonation.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.bloodonation.R;

import java.util.HashMap;

public class RequestBloodActivity extends AppCompatActivity {
    private DatabaseReference donor_database, request_database;
    private FirebaseAuth mAuth;
    private Spinner sp;
    private ArrayAdapter<String> adp;
    private TextInputEditText dateEditText, mobileEditText, nameEditText, descriptionEditText, cityEditText;
    private String bloodgroup[] = {"Choose Bloodgroup", "A+", "O+", "B+", "AB+", "A-", "O-", "B-", "AB-",};
    private String str_blood, str_name, str_mobile, str_city, str_date, str_description, mUser;
    private ProgressDialog progressDialog;
    private MaterialButton button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        mAuth = FirebaseAuth.getInstance();
        donor_database = FirebaseDatabase.getInstance().getReference().child("Donor");
        request_database = FirebaseDatabase.getInstance().getReference().child("Request_Blood");
        sp = findViewById(R.id.bloodgroup_spinner);
        adp = new ArrayAdapter<String>(RequestBloodActivity.this, android.R.layout.simple_list_item_1, bloodgroup);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        str_blood = sp.getSelectedItem().toString();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        dateEditText = findViewById(R.id.req_date);
        nameEditText = findViewById(R.id.req_username);
        mobileEditText = findViewById(R.id.req_mobile);
        descriptionEditText = findViewById(R.id.req_description);
        cityEditText = findViewById(R.id.req_city);
        button = findViewById(R.id.request_button);
        mUser = mAuth.getCurrentUser().getUid();
        donor_database.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    str_mobile = snapshot.child("user_mobile").getValue().toString();
                    mobileEditText.setText(str_mobile);
                    mobileEditText.setEnabled(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        retrieveData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateEditText.getText().toString().isEmpty() && nameEditText.getText().toString().isEmpty()
                         && descriptionEditText.getText().toString().isEmpty() && cityEditText.getText().toString().isEmpty()) {
                    dateEditText.setError("Fill field");
                    nameEditText.setError("Fill field");
                    mobileEditText.setError("Fill field");
                    descriptionEditText.setError("Fill field");
                    cityEditText.setError("Fill field");

                    if (str_blood.equals("Choose Bloodgroup")) {

                        Toast.makeText(getApplicationContext(), "Provide the Valid Details", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    str_date = dateEditText.getText().toString();
                    str_name = nameEditText.getText().toString();
                    str_description = descriptionEditText.getText().toString();
                    str_city = cityEditText.getText().toString();
                    str_city = str_city.toUpperCase();
                    str_blood=sp.getSelectedItem().toString();
                    postBloodRequest();
                }

            }
        });

    }
    private void callFunction() {
        Intent callIntent=new Intent(Intent.ACTION_CALL);
        if(str_mobile.trim().isEmpty()){
            callIntent.setData(Uri.parse("tel:567788"));
        }else{
            callIntent.setData(Uri.parse("tel:"+str_mobile));
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
            requestPermission();
        }else{
            startActivity(callIntent);
        }
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(RequestBloodActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }
    private void postBloodRequest() {
        progressDialog.setTitle("Account Setting");
        progressDialog.setMessage("Saving account changes...");
        progressDialog.show();

        HashMap<String, Object> mapdata = new HashMap<>();
        mapdata.put("uid", mUser);
        mapdata.put("name", str_name);
        mapdata.put("bloodgroup", str_blood);
        mapdata.put("mobile", str_mobile);
        mapdata.put("description", str_description);
        mapdata.put("date", str_date);
        mapdata.put("city", str_city);

        request_database.child(mUser).updateChildren(mapdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RequestBloodActivity.this, "Request Posted Successfully", Toast.LENGTH_SHORT).show();
                    sendToDashActivity();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(RequestBloodActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

//    void retrieveData() {
//        request_database.child(mUser).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    str_mobile = snapshot.child("mobile").getValue().toString();
//                    str_city = snapshot.child("city").getValue().toString();
//                    str_date = snapshot.child("date").getValue().toString();
//                    str_description = snapshot.child("description").getValue().toString();
//                    str_name = snapshot.child("name").getValue().toString();
//                    str_blood = snapshot.child("bloodgroup").getValue().toString();
//
//
//                    cityEditText.setText(str_city);
//                    descriptionEditText.setText(str_description);
//                    nameEditText.setText(str_name);
//                    mobileEditText.setText(str_mobile);
//                    dateEditText.setText(str_date);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        sp.setTooltipText(str_blood);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    void sendToDashActivity() {
        Intent dashIntent = new Intent(RequestBloodActivity.this, DashBoardActivity.class);
        dashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        dashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashIntent);
        finish();
    }

}
