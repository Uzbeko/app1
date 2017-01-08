package com.example.uzbeko.service_test;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {
    private String title;
    private String pubDate;
    private String thumbnail;
    private String link;

    public Items(Parcel source){ // -- konstruktorius is parcelio istrauks reiksmes ir priskirs objektui
        this.title = source.readString();
        this.pubDate = source.readString();
        this.thumbnail = source.readString();
        this.link = source.readString();
    }

    public Items(){ //--Neutralus konstruktorius
    }

    //----------------------------------------------
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(pubDate);
        dest.writeString(thumbnail);
        dest.writeString(link);
    }

    //--Is parcelio gautu duomenu rekonstravimas i pradini objekta------------------------
    public static  final Parcelable.Creator<Items> CREATOR = new Parcelable.Creator<Items>(){

        @Override
        public Items createFromParcel(Parcel source) {
            return  new Items(source);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
 //-------------------------------------------------------------------------------------------------
    //--geters--------------------------
    public String getTitle(){
        return this.title;
    }
    public String getPubDate(){
        return this.pubDate;
    }
    public String getthumbnail(){
        return this.thumbnail;
    }
    public String getLink(){
        return this.link;
    }

    //--seters-------------------------------------
    public void setTitle(String title){
        this.title = title;
    }
    public void setPubDate(String pubDate){
        this.pubDate = pubDate;
    }
    public void setthumbnail(String thumbnail){
        this.thumbnail = thumbnail;
    }
    public void setLink(String link){
        this.link = link;
    }
}
