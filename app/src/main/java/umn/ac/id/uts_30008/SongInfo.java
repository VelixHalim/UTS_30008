package umn.ac.id.uts_30008;

public class SongInfo {
    public String songName, songUrl;
    public SongInfo(){}

    public SongInfo(String songName, String songUrl){
        this.songName = songName;
        this.songUrl = songUrl;
    }

    public String getSongName(){
        return songName;
    }
    public String getSongUrl(){
        return songUrl;
    }
}
