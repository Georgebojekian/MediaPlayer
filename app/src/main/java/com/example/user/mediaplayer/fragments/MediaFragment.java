package com.example.user.mediaplayer.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.mediaplayer.R;
import com.example.user.mediaplayer.adapters.MediaAdapter;
import com.example.user.mediaplayer.model.Audio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/6/2017.
 */

public class MediaFragment extends Fragment  {
    String TAG = MediaFragment.class.getName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view   = inflater.inflate(R.layout.media_fragment,container,false);
        RecyclerView recyclerView  = (RecyclerView)view.findViewById(R.id.music_list);

        //load musics from device
        MediaAdapter mediaAdapter = new MediaAdapter(getActivity(), loadAudio());

        recyclerView.setAdapter(mediaAdapter);
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
    private ArrayList<Audio> loadAudio() {
        ArrayList<Audio> fetchAudioList = new ArrayList<>();
        ContentResolver contentResolver  = getActivity().getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String data  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                double size = cursor.getDouble(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("0.00");
                double resultofsize = Double.parseDouble(decimalFormat.format((size / (1024 * 1024))));
                fetchAudioList.add(new Audio(title,resultofsize,data));
            }
            cursor.close();
        }
        else if (cursor == null) {
            Log.e(TAG,"ERROR");
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(getActivity(), "No Media", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"No Media");
        }
        return fetchAudioList;

    }




}
