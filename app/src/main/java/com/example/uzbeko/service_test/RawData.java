package com.example.uzbeko.service_test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ed on 9/4/2015.
 */
public class RawData implements Parcelable {
    int id;
    String name;
    String desc;
    String[] cities = new String[3];

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeStringArray(cities);
    }

    public static final Parcelable.Creator<RawData> CREATOR
            = new Parcelable.Creator<RawData>(){

        @Override
        public RawData createFromParcel(Parcel source) {
            return new RawData(source);
        }

        @Override
        public RawData[] newArray(int size) {
            return new RawData[size];
        }
    };
    //--kuriam konstrukotiu kuriam paduodam creator grazintus parametrus po siuntimo
    public RawData(Parcel source){
        id = source.readInt();
        name = source.readString();
        desc = source.readString();
        source.readStringArray(cities);
    }
    public RawData(){}
//--Geteriai Seteriai---------------------------------------------------------
    public void setId(int value){
        this.id = value;
    }
    public void setName(String value){
        this.name = value;
    }
    public void setDesc(String value){
        this.desc = value;
    }
    public void setCities(String[] value){
        this.cities = value;
    }
    //--------------------------------
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getDesc(){
        return this.desc;
    }
    public String[] getCities(){
        return this.cities;
    }
}
