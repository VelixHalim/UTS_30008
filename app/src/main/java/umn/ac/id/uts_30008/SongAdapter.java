package umn.ac.id.uts_30008;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    ArrayList<SongInfo> songs;
    Context context;


    SongAdapter.OnItemClickListener onItemClickListener;

    public SongAdapter(Context context, ArrayList<SongInfo> songs){
        this.context=context;
        this.songs = songs;
    }

    public Object getItemId(long i) {
        return i;
    }

    public interface OnItemClickListener{
        void onItemClick(Button b, View v, SongInfo obj, int position);
    }

    public void setOnItemClickListener(SongAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View myview = LayoutInflater.from(context).inflate(R.layout.row_song, viewGroup, false);
        return new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(SongHolder songHolder, int i) {
        SongInfo c = songs.get(i);
        songHolder.songName.setText(c.songName);
        songHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(songHolder.btnAction,v,c,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{
        TextView songName;
        Button btnAction;
        public SongHolder(View itemView){
            super(itemView);
            songName = (TextView) itemView.findViewById(R.id.tvSongName);
            btnAction = (Button) itemView.findViewById(R.id.btnAct);
        }
    }
}
