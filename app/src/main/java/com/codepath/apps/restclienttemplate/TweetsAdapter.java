package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetImage;
        TextView tvTimeStamp;
         public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        public void bind(Tweet tweet) {
             tvBody.setText(tweet.body);
             tvScreenName.setText(tweet.user.screenName);
             tvTimeStamp.setText(tweet.timeStamp);
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(60)).into(ivProfileImage);

            if (tweet.imageUrl != "") {
                ivTweetImage.setVisibility(View.VISIBLE);
                Log.d("Helloworld",tweet.imageUrl);
                Glide.with(context).
                        load(tweet.imageUrl)

                        .into(ivTweetImage);

            } else {
                ivTweetImage.setVisibility(View.GONE);
            }
            //Glide.with(context).load(tweet.imageUrl).into(ivTweetImage);
        }


    }
}
