package umn.ac.id.uts_30008;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListLagu extends AppCompatActivity {

    private  static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    SeekBar seekBar;
    RecyclerView recyclerView;
    //ListView listView;
    //ArrayAdapter<String> adapter;
    //DaftarLaguAdapter mAdapter;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lagu);


        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        //popup hanya sekali muncul(pertama kali login saja)
        if(firstStart){
            showStartDialog();
        }
//list view
        if(ContextCompat.checkSelfPermission(ListLagu.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ListLagu.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(ListLagu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(ListLagu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            loadsongs();
        }
        //listview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        seekBar=(SeekBar) findViewById(R.id.seekBar);

        songAdapter = new SongAdapter(this,songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAdapter);


        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Button b, View v, SongInfo obj, int position) {
//                String songName = (String) songAdapter.;
//                if(b.getText().toString().equals("play")){
//                    startActivity(new Intent(getApplicationContext(), Player.class));
//                }
//
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(b.getText().toString().equals("Stop")){
                                b.setText("Play");
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                mediaPlayer=null;
                            }else{

//                                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("songs",songs).putExtra("pos",position)); //player music tersendiri belum jalan
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(obj.getSongUrl());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        seekBar.setProgress(0);
                                        seekBar.setMax(mp.getDuration());
                                        b.setText("Stop");
                                    }
                                });
                            }

                        }catch (IOException e){}
                    }
                };

                handler.postDelayed(r,10);


            }
        });

//        CheckPermission();
        Thread t = new MyThread();
        t.start();

    }

    private class MyThread extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(10);
                if(mediaPlayer !=null)
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
//    private void CheckPermission(){
//        if(Build.VERSION.SDK_INT>=23){
//            if(ActivityCompat.checkSelfPermission(ListLagu.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
//            }else{
//                ActivityCompat.requestPermissions(ListLagu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
//            }
//        }else{
//            loadsongs();
//        }
//    }
    private void loadsongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor =  getContentResolver().query(uri, null,selection,null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    SongInfo s = new SongInfo(name, url);
                    songs.add(s);
                }while(cursor.moveToNext());
            }
            cursor.close();
            songAdapter = new SongAdapter(ListLagu.this, songs);
        }
    }
//    @Override
//    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        switch (requestCode){
//            case 123:
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    loadsongs();
//                }else{
//                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
//                    CheckPermission();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//
//    }

//    public void doStuff(){

        //listView = (ListView) findViewById(R.id.listView);
        //arrayList = new ArrayList<>();
        //getMusic();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        //recyclerView.setAdapter(adapter);
//        mAdapter = new DaftarLaguAdapter(this, daftarLagu);
//        recyclerView.setAdapter(mAdapter);
        //listView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//
//            }
//        });
////        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//
//            }
//        });
//    }

//    public void getMusic(){
//        ContentResolver contentResolver = getContentResolver();
//        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);
//
//        if(songCursor != null && songCursor.moveToFirst()){
//            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//
//            do{
//                String currentTitle = songCursor.getString(songTitle);
//                String currentArtist = songCursor.getString(songArtist);
//                String currentLocation= songCursor.getString(songLocation);
//                arrayList.add("Title:"+currentTitle+ "\n"+"Artist: "+currentArtist+"\n"+"Location: "+currentLocation);
//            } while(songCursor.moveToNext());
//        }
//    }


//    @Override
//    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSION_REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(ListLagu.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "Permissions Granted!", Toast.LENGTH_SHORT).show();
//                        doStuff();
//                    }
//                }else {
//                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                return;
//            }
//        }
//    }


    private void showStartDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Selamat Datang")
                .setMessage("          Velix Halim \n          00000030008")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.profile:
                Toast.makeText(this,"Profile selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ListLagu.this, Profile.class));
                return true;
            case R.id.Logout:
                Toast.makeText(this,"Logout selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ListLagu.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}