package com.example.listadecontatos;

import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable {

    private int id;

    private String name;

    private String telemovel;

    private String email;

    private int grupoId;

    private String grupoNome;


    public Contato(String name, String telemovel, String email, String grupoNome) {
        this.id = id;
        this.name = name;
        this.telemovel = telemovel;
        this.email = email;
        this.grupoId = grupoId;
        this.grupoNome = grupoNome;
    }

    protected Contato(Parcel in) {
        id = in.readInt();
        name = in.readString();
        telemovel = in.readString();
        email = in.readString();
        grupoId = in.readInt();
        grupoNome = in.readString();
    }

    public static final Creator<Contato> CREATOR = new Creator<Contato>() {
        @Override
        public Contato createFromParcel(Parcel in) {
            return new Contato(in);
        }

        @Override
        public Contato[] newArray(int size) {
            return new Contato[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(telemovel);
        parcel.writeString(email);
        parcel.writeInt(grupoId);
        parcel.writeString(grupoNome);
    }

    public Contato() {

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getTelemovel(){
        return telemovel;
    }

    public void setTelemovel(String telemovel){
        this.telemovel = telemovel;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public int getGrupoId(){
        return grupoId;
    }

    public void setGrupoId(int grupoId){
        this.grupoId = grupoId;
    }

    public String getGrupoNome(){
        return grupoNome;
    }

    public void setGrupoNome(String grupoNome){
        this.grupoNome = grupoNome;
    }
}
