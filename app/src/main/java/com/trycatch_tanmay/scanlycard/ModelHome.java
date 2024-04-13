package com.trycatch_tanmay.scanlycard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class ModelHome implements Parcelable {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    public long id; // Or replace with your unique identifier

    public String name;
    public String contact;
    public String address;
    public String position;
    public String imagePath; // Field to store the path to the image

    public ModelHome(String name, String contact, String address, String position, String imagePath) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.position = position;
        this.imagePath = imagePath;
    }

    protected ModelHome(Parcel in) {
        id = in.readLong();
        name = in.readString();
        contact = in.readString();
        address = in.readString();
        position = in.readString();
        imagePath = in.readString();
    }

    public static final Parcelable.Creator<ModelHome> CREATOR = new Parcelable.Creator<ModelHome>() {
        @Override
        public ModelHome createFromParcel(Parcel in) {
            return new ModelHome(in);
        }

        @Override
        public ModelHome[] newArray(int size) {
            return new ModelHome[size];
        }
    };

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(contact);
        dest.writeString(address);
        dest.writeString(position);
        dest.writeString(imagePath);
    }
}
