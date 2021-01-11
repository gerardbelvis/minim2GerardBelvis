package com.example.minim2_gerardbelvis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.minim2_gerardbelvis.models.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://api.github.com/";
    private static Retrofit retrofit;
    private GitHubAPI gitHubAPIinterface;
    private ProgressBar progressBar;
    private String user;
    private RecyclerView repositoriesRecyclerView;
    private TextView followersTextView;
    private TextView followingTextView;
    private TextView userTextView;
    private android.widget.ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        repositoriesRecyclerView = findViewById(R.id.recyclerView);
        profileImageView = findViewById(R.id.profileImageView);
        followersTextView=findViewById(R.id.followersTextView);
        followingTextView = findViewById(R.id.followingTextView);
        userTextView =findViewById(R.id.userTextView);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");

        startRetrofit();
        gitHubAPIinterface=retrofit.create(GitHubAPI.class);

        repositoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        repositoriesRecyclerView.setHasFixedSize(true);

        loadInfo();


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

    public void loadInfo(){
        Log.d("user", user);
        Call<User> call = gitHubAPIinterface.infoUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User user = response.body();
                Log.d("following i repos ", String.valueOf(user.getFollowing()) + ", " +String.valueOf(user.getPublic_repos()));
                userTextView.setText(String.valueOf(user.getLogin()));
                followingTextView.setText(String.valueOf(user.getFollowing()));
                followersTextView.setText(String.valueOf(user.getFollowers()));
                Picasso.with(getApplicationContext()).load(user.getAvatar_url()).into(profileImageView);
                showProgress(false);

                loadRepositories();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Code: " + t.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });
    }

    public void loadRepositories(){
        showProgress(true);
        Call<List<Repositories>> call = gitHubAPIinterface.reposList(user);
        call.enqueue(new Callback<List<Repositories>>() {
            @Override
            public void onResponse(Call<List<Repositories>> call, Response<List<Repositories>> response) {

                if(response.isSuccessful()){
                    Log.d("onResponse", "list has arrived");
                    List<Repositories> reposList = response.body();
                    repositoriesRecyclerView = findViewById(R.id.recyclerView);
                    // https://developer.android.com/guide/topics/ui/layout/recyclerview#java + video

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), reposList);
                    repositoriesRecyclerView.setAdapter(adapter);
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<List<Repositories>> call, Throwable t) {

            }
        });
    }

    public void showProgress (boolean visible){
        //Sets the visibility/invisibility of progressBar
        if(visible)
            this.progressBar.setVisibility(View.VISIBLE);
        else
            this.progressBar.setVisibility(View.GONE);
    }
}