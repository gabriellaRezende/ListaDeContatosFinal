package com.example.listadecontatos;

public class Grupo {

    private int id;

    private String nomeGrupo;

    public Grupo(){
    }

    public Grupo(int id, String nomeGrupo) {
        this.id = id;
        this.nomeGrupo = nomeGrupo;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNomeGrupo(){
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo){
        this.nomeGrupo = nomeGrupo;
    }

    @Override
    public String toString() {
        return nomeGrupo;
    }

}
