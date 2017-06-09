package com.example.user.mediaplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.mediaplayer.R;
import com.example.user.mediaplayer.model.Audio;

import java.util.List;

/**
 * Created by User on 6/6/2017.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder>  {
    String TAG = MediaAdapter.class.getName();
    private Context context ;
    private IOnClickListener onClickListener;
    private List<Audio> songList;

    public MediaAdapter(Context context, List<Audio> songList) {
        this.context = context;
        this.onClickListener = (IOnClickListener)context;
        this.songList = songList;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView musicName ,musicSize,musicPath;
        ImageButton playMusic  ;
        public MyViewHolder(View itemView) {
            super(itemView);
            musicName = (TextView)itemView.findViewById(R.id.music_name_id);
            musicSize = (TextView)itemView.findViewById(R.id.music_size_id);
            playMusic = (ImageButton) itemView.findViewById(R.id.start_music);
            musicPath = (TextView) itemView.findViewById(R.id.music_data_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Audio audio = getItem(getAdapterPosition());
                    if(onClickListener != null){
                        onClickListener.onClickItem(audio,playMusic);
                    }else {
                        Log.e(TAG,"No data in interface");
                    }
                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context)
               .inflate(R.layout.music_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Audio audio = getItem(position);
        holder.musicName.setText(audio.getTitle());
        holder.musicSize.setText(String.valueOf(audio.getSize()).concat("MB"));
        holder.musicPath.setText(audio.getData());

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    public Audio getItem(int position){
        return  songList.get(position);
    }

  public interface  IOnClickListener{
        void onClickItem(Audio audio,ImageButton playMusic);
    }

}
