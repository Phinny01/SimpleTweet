 package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

 public class TimelineActivity extends AppCompatActivity {

     public final int REQUEST_CODE = 20;
     public static final String TAG = "TimelineActivity";
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    Button logoutButton;
    private SwipeRefreshLayout swipeContainer;
     void onLogoutButton() {
         // forget who's logged in
         TwitterApp.getRestClient(this).clearAccessToken();
         // navigate backwards to Login screen
         Intent i = new Intent(this, LoginActivity.class);
         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
         i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
         startActivity(i);}

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
         return super.onCreateOptionsMenu(menu);

     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if (item.getItemId() == R.id.compose) {

             Intent intent = new Intent(this, ComposeActivity.class);
             startActivityForResult(intent, REQUEST_CODE);
             return true;
         }
         return super.onOptionsItemSelected(item);

     }
     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Tweet tweet =  Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, tweet);
            adapter.notifyItemInserted(0 );
            rvTweets.smoothScrollToPosition(0);

         }
         super.onActivityResult(requestCode, resultCode, data);
     }
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


         swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
         // Setup refresh listener which triggers new data loading
         swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 // Your code to refresh the list here.
                 // Make sure you call swipeContainer.setRefreshing(false)
                 // once the network request has completed successfully.
                 fetchTimelineAsync(0);
             }
         });
         // Configure the refreshing colors
         swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                 android.R.color.holo_green_light,
                 android.R.color.holo_orange_light,
                 android.R.color.holo_red_light);

        client = TwitterApp.getRestClient(this);
        populateHomeTimeline();

        logoutButton = findViewById(R.id.LogoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutButton();
            }
        });
        rvTweets =findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);

        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);

    }

     private void fetchTimelineAsync(int i) {
         adapter.clear();
         populateHomeTimeline();
         swipeContainer.setRefreshing(false);
     }

     private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Onsuccess!" + json.toString() );
                JSONArray  jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);

                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "OnFailure! ", throwable);
            }
        });
    }
}