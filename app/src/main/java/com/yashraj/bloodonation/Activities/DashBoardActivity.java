package com.yashraj.bloodonation.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.bloodonation.Model.RequestDetails;
import com.yashraj.bloodonation.R;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.Utils;

import java.util.ArrayList;
import java.util.List;


public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final String TAG = DashBoardActivity.class.getName().toString();
    FirebaseUser mUser;
    ImageView recyclerImage;
    FirebaseRecyclerAdapter<RequestDetails, RequestViewHolder> firebaseRecyclerAdapter;
    String str_mobile="";
    RelativeLayout no_network_state_layout, network_state_layout;
    private Button retry_button;
    String str_city = "";
    private CardView cardViewdashboard;
    private DatabaseReference databaseReference, request_database;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private ImageView menu_title, profile_imageview;
    private NavigationView navigationView;
    private BottomNavigationView navView;
    private TextView header_user, header_address;
    private ImageCarousel imageCarousel;
    private List<CarouselItem> imglist;
    private FloatingActionButton post_request_button;
    private RecyclerView recyclerView;
    private AdView adView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent homeintent = new Intent(DashBoardActivity.this, DashBoardActivity.class);
                    homeintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeintent);
                    return true;
                case R.id.map:
                    Intent mapintent = new Intent(DashBoardActivity.this, MapActivity.class);
                    mapintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mapintent);
                    Animatoo.animateSlideLeft(DashBoardActivity.this);
                    return true;
                case R.id.find_donor:
                    Intent findintent = new Intent(DashBoardActivity.this, FindDonorActivity.class);
                    findintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(findintent);
                    Animatoo.animateSlideLeft(DashBoardActivity.this);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        cardViewdashboard = findViewById(R.id.covid_card);
        adView = new AdView(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        menu_title = findViewById(R.id.menu_title);
        navigationView = (NavigationView) findViewById(R.id.navigationID);
        View hView=navigationView.getHeaderView(0);
        header_user = hView.findViewById(R.id.header_username);
        header_address = hView.findViewById(R.id.header_address);
        navView = findViewById(R.id.bottomnav_dashboard);
        profile_imageview = findViewById(R.id.profile_user_contact_imageView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Donor");
        request_database = FirebaseDatabase.getInstance().getReference().child("Request_Blood");

        post_request_button = findViewById(R.id.post_request_fab);
        retry_button = findViewById(R.id.retry_button);
        no_network_state_layout = findViewById(R.id.no_network_state);
        network_state_layout = findViewById(R.id.network_state);

        request_database.keepSynced(true);

        recyclerImage = findViewById(R.id.recycler_image);
        recyclerView = findViewById(R.id.request_recyacler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestCard();
            }
        }, 1000);


        ///////////////////////////////////////////////////////////////////////////////// Google Ads View /////////////////////////////////////////////////////////////////////

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); //TODO to implement the original addunit.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ///////////////////////////////////////////////////////////////////////////////// Google Ads View /////////////////////////////////////////////////////////////////////

        post_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, RequestBloodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        imageCarousel = findViewById(R.id.carousel);
        imglist = new ArrayList<CarouselItem>();
        imglist.add(new CarouselItem(R.drawable.group_1_9, ""));
        imglist.add(new CarouselItem(R.drawable.rectangle_3_9, ""));
        imglist.add(new CarouselItem(R.drawable.rectangle_4_9, ""));
        imglist.add(new CarouselItem(R.drawable.rectangle_6_9, ""));

        imageCarousel.addData(imglist);
        imageCarousel.setCaptionTextSize(Utils.spToPx(20, this));
        imageCarousel.setAutoPlay(true);
        imageCarousel.setAutoPlayDelay(3000);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        menu_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "", address = "";
                        if (snapshot.exists()) {
                            username = snapshot.child("user_firstname").getValue().toString();
                            address = snapshot.child("user_address").getValue().toString();
                            header_user.setText(username);
                            header_address.setText(address);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        profile_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Animatoo.animateZoom(DashBoardActivity.this);
            }
        });
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) DashBoardActivity.this);


        cardViewdashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashBoardActivity.this, CovidCardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        databaseReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("user_city")) {
                    str_city = snapshot.child("user_city").getValue().toString();
                    Log.i("TAG", "onDataChange: " + str_city);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        request_database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    if(snapshot.hasChild(mUser.getUid())){
//                        if(snapshot.child(mUser.getUid()).child("mobile").getValue().toString()==str_city){
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigationID) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawerheader, menu);
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(DashBoardActivity.this, "About", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                Toast.makeText(DashBoardActivity.this, "Logged Out!", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                startActivity(new Intent(DashBoardActivity.this, RegistrationActivity.class));
                finish();
                return true;
            case R.id.settings:
                Toast.makeText(DashBoardActivity.this, "More is Clicked", Toast.LENGTH_LONG).show();
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    private void callFunction() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        if (str_mobile.trim().isEmpty()) {
            callIntent.setData(Uri.parse("tel:567788"));
        } else {
            callIntent.setData(Uri.parse("tel:" + str_mobile));
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
            requestPermission();
        } else {
            startActivity(callIntent);
        }
    }

    private void requestCard() {
        FirebaseRecyclerOptions<RequestDetails> options =
                new FirebaseRecyclerOptions.Builder<RequestDetails>().setQuery(request_database.orderByChild("city").equalTo(str_city), RequestDetails.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RequestDetails, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int i, @NonNull RequestDetails requestDetails) {
                holder.description.setText(requestDetails.getDescription());
                holder.mobile.setText(requestDetails.getMobile());
                str_mobile=requestDetails.getMobile();
                holder.name.setText(requestDetails.getName());
                holder.bloodgroup.setText(requestDetails.getBloodgroup());
                holder.city.setText(requestDetails.getCity());
                holder.date.setText(requestDetails.getDate());
                recyclerImage.setVisibility(View.GONE);
                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callFunction();
                    }
                });
                request_database.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("uid")) {

                            holder.delete_button.setVisibility(View.VISIBLE);
                            holder.delete_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    request_database.child(mUser.getUid()).removeValue();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_layout, viewGroup, false);
                return new RequestViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileconnection = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            no_network_state_layout.setVisibility(View.GONE);
            network_state_layout.setVisibility(View.VISIBLE);
        } else if (mobileconnection.isConnected()) {
            no_network_state_layout.setVisibility(View.GONE);
            network_state_layout.setVisibility(View.VISIBLE);
        } else {
            no_network_state_layout.setVisibility(View.VISIBLE);
            network_state_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Animatoo.animateInAndOut(DashBoardActivity.this);
        return super.onKeyDown(keyCode, event);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView bloodgroup, name, mobile, date, description, city;
        FloatingActionButton call;
        Button delete_button;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodgroup = itemView.findViewById(R.id.blood_group_request_text);
            name = itemView.findViewById(R.id.request_text_firstname);
            mobile = itemView.findViewById(R.id.request_text_mobile);
            date = itemView.findViewById(R.id.request_text_date);
            description = itemView.findViewById(R.id.request_text_description);
            city = itemView.findViewById(R.id.request_text_city);
            call = itemView.findViewById(R.id.req_call_fab);
            delete_button = itemView.findViewById(R.id.delete_button);


        }
    }
}