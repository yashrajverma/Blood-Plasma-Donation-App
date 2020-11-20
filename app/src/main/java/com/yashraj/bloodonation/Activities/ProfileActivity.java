package com.yashraj.bloodonation.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yashraj.bloodonation.R;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String bloodgroup[] = {"Choose Bloodgroup", "A+", "O+", "B+", "AB+","A-", "O-", "B-", "AB-", };
    ArrayAdapter<String> adp;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser mUser;
    private EditText profile_firstname, profile_lastname, profile_city, profile_state, profile_mobile, profile_email, profile_country,profile_address,profile_age;
    private Button sumitButton,deleteButton;
    private String str_firstname,str_lastname,str_bloodgroup,str_city,str_state,str_mobile,str_email,str_country,str_address,str_age;
    private Spinner sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Donor");
        storageReference= FirebaseStorage.getInstance().getReference().child("donor_image");
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        profile_firstname =findViewById(R.id.profile_firstname);
        profile_lastname =findViewById(R.id.profile_lastname);
        profile_email =findViewById(R.id.profile_email);
        profile_mobile =findViewById(R.id.profile_mobile);
        profile_city =findViewById(R.id.profile_city);
        profile_state =findViewById(R.id.profile_state);
        profile_country =findViewById(R.id.profile_country);
        profile_address=findViewById(R.id.profile_address);
        profile_age=findViewById(R.id.profile_age);
        sumitButton=findViewById(R.id.submitbutton_profile);
        deleteButton=findViewById(R.id.delete_account_button);
        sp=findViewById(R.id.profile_bloodgroup);
        adp = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, bloodgroup);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);

        sumitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                saveDataOfDonor();
            }
        });
        retrieveData();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    private void deleteAccount() {
        databaseReference.child(mUser.getUid()).removeValue();
        Intent intent=new Intent(ProfileActivity.this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mAuth.signOut();
        startActivity(intent);
        finish();

    }


    private void saveDataOfDonor() {
        progressDialog.setTitle("Account Setting");
        progressDialog.setMessage("Saving account changes...");
        progressDialog.show();

        str_firstname= profile_firstname.getText().toString();
        str_lastname= profile_lastname.getText().toString();
        str_email= profile_email.getText().toString();
        str_city= profile_city.getText().toString();
        str_country= profile_country.getText().toString();
        str_bloodgroup= sp.getSelectedItem().toString();
        str_state= profile_state.getText().toString();
        str_address=profile_address.getText().toString();
        str_age=profile_age.getText().toString();

        str_state=str_state.toUpperCase();
        str_city=str_city.toUpperCase();
        str_bloodgroup=str_bloodgroup.toUpperCase();

        if(TextUtils.isEmpty(str_bloodgroup) && TextUtils.isEmpty(str_city)
                && TextUtils.isEmpty(str_country) && TextUtils.isEmpty(str_email)
                && TextUtils.isEmpty(str_firstname) && TextUtils.isEmpty(str_lastname)
                && TextUtils.isEmpty(str_mobile) && TextUtils.isEmpty(str_state) && TextUtils.isEmpty(str_address) ){
            progressDialog.dismiss();
            Toast.makeText(this, "Provide Required Details!", Toast.LENGTH_SHORT).show();

        }else{
            HashMap<String,Object> MapData=new HashMap<>();
            MapData.put("uid",mAuth.getCurrentUser().getUid());
            MapData.put("user_firstname",str_firstname);
            MapData.put("user_lastname",str_lastname);
            MapData.put("user_email",str_email);
            MapData.put("user_bloodgroup",str_bloodgroup);
            MapData.put("user_city",str_city);
            MapData.put("user_state",str_state);
            MapData.put("user_address",str_address);
            MapData.put("user_country",str_country);
            MapData.put("user_age",str_age);


            databaseReference.child(mUser.getUid()).updateChildren(MapData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent intent=new Intent(ProfileActivity.this,DashBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ProfileActivity.this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else {
                        String error=task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"Error:"+error,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
    private void retrieveData() {
        databaseReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    str_firstname=snapshot.child("user_firstname").getValue().toString();
                    str_lastname=snapshot.child("user_lastname").getValue().toString();
                    str_email=snapshot.child("user_email").getValue().toString();
                    str_bloodgroup=snapshot.child("user_bloodgroup").getValue().toString();
                    str_city=snapshot.child("user_city").getValue().toString();
                    str_state=snapshot.child("user_state").getValue().toString();
                    str_country=snapshot.child("user_country").getValue().toString();
                    str_mobile=snapshot.child("user_mobile").getValue().toString();
                    str_address=snapshot.child("user_address").getValue().toString();
                    str_age=snapshot.child("user_age").getValue().toString();

                     //TODO
                    profile_firstname.setText(str_firstname);
                    profile_lastname.setText(str_lastname);
                    profile_email.setText(str_email);
                    sp.setTooltipText(str_bloodgroup);
                    profile_city.setText(str_city);
                    profile_state.setText(str_state);
                    profile_country.setText(str_country);
                    profile_mobile.setText(str_mobile);
                    profile_address.setText(str_address);
                    profile_age.setText(str_age);
                    profile_mobile.setEnabled(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Animatoo.animateInAndOut(ProfileActivity.this);
        return super.onKeyDown(keyCode, event);
    }
}
