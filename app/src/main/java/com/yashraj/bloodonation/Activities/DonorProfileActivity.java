package com.yashraj.bloodonation.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yashraj.bloodonation.R;

public class DonorProfileActivity extends AppCompatActivity {

    String str_bloodgroup="",str_donor_firstname="",str_donor_lastname="",str_donorcity="",str_donorstate="",str_donormobile="",str_address="";
    TextView bloodgroup,donor_firstname,donorcity,donorstate,donormobile,donoraddress,donor_lastname;
    FloatingActionButton call_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_profile);
        bloodgroup=findViewById(R.id.donor_profile_blood_group_donortext);
        donor_firstname=findViewById(R.id.donor_profile_donor_firstname);
        donor_lastname=findViewById(R.id.donor_profile_donor_lastname);
        donorcity=findViewById(R.id.donor_profile_donor_city);
        donormobile=findViewById(R.id.donor_profile_donor_mobile);
        donorstate=findViewById(R.id.donor_profile_donor_state);
        donoraddress=findViewById(R.id.donor_profile_donor_address);

        str_bloodgroup=getIntent().getExtras().get("user_bloodgroup").toString();
        str_donorcity=getIntent().getExtras().get("user_city").toString();
        str_donormobile=getIntent().getExtras().get("user_mobile").toString();
        str_donor_firstname=getIntent().getExtras().get("user_firstname").toString();
        str_donor_lastname=getIntent().getExtras().get("user_lastname").toString();
        str_donorstate=getIntent().getExtras().get("user_state").toString();
        str_address=getIntent().getExtras().get("user_address").toString();

        bloodgroup.setText(str_bloodgroup);
        donorstate.setText(str_donorstate);
        donormobile.setText(str_donormobile);
        donorcity.setText(str_donorcity);
        donor_firstname.setText(str_donor_firstname);
        donor_lastname.setText(str_donor_lastname);
        donoraddress.setText(str_address);

        call_button=findViewById(R.id.floatingActionButton_call);
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFunction();
            }
        });

    }

    private void callFunction() {
        Intent callIntent=new Intent(Intent.ACTION_CALL);
        if(str_donormobile.trim().isEmpty()){
            callIntent.setData(Uri.parse("tel:567788"));
        }else{
            callIntent.setData(Uri.parse("tel:"+str_donormobile));
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
            requestPermission();
        }else{
            startActivity(callIntent);
        }
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(DonorProfileActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Animatoo.animateInAndOut(DonorProfileActivity.this);
        return super.onKeyDown(keyCode, event);
    }
}
