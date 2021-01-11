package com.example.minim2_gerardbelvis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.minim2_gerardbelvis.models.*;

public class UserActivity<checkButtonClicked> extends AppCompatActivity {

    private SharedPreferences preferences;
    private static Retrofit retrofit;
    private String user;
    private EditText userName;
    private RecyclerView repositories;
    private Button checkButton;
    private GitHubAPI gitHubAPIinterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userName = findViewById(R.id.userEditText);
        checkButton = findViewById(R.id.checkUserButton);
        loadPreferences();
        user = preferences.getString("user", null);


        startRetrofit();

        checkUser();

    }

    private static void startRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Attaching Interceptor to a client
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }


    private void savePreferences(){
        SharedPreferences preferences= getSharedPreferences("User credentials", Context.MODE_PRIVATE);
        String user = userName.getText().toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", user);
        editor.apply();


    }

    private void checkUser(){
        gitHubAPIinterface= retrofit.create(GitHubAPI.class);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                Log.d("click", "ok");

                Call<User> call = gitHubAPIinterface.infoUser(userName.getText().toString());
                Log.d("User", userName.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("Info", "We have an answer");
                        if (response.isSuccessful()) {
                            Log.d("onResponse", "we have a user");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user", userName.getText().toString());
                            startActivity(intent);
                        } else {
                            Log.d("Info:", "user not found");
                            Toast.makeText(getApplicationContext(), "User not found: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG);
                    }

                });
            }
        });

    }

    private void loadPreferences(){
        preferences = getSharedPreferences("User credentials", Context.MODE_PRIVATE);
    }


}