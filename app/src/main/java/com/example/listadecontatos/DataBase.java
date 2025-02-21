package com.example.listadecontatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DataBase extends SQLiteOpenHelper {

    public static final String Tag = "xpto";
    private static final String DATABASE_NAME = "contatos.db";
    private static final int DATABASE_VERSION = 1;

    //Tabela de contatos
    public static final String TABLE_CONTATOS = "contatos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "nome";
    public static final String COLUMN_TELEMOVEL = "telemovel";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_GRUPO_ID = "grupo_id";

    //Tabela de grupos
    public static final String TABLE_GRUPOS = "grupos";
    public static final String COLUMN_GRUPO_ID2 = "id";
    public static final String COLUMN_GRUPO_NOME = "nome_grupo";

    //Comando para criar a tabela de grupos
    private static final String CREATE_TABLE_GRUPOS = "CREATE TABLE " + TABLE_GRUPOS + "(" +
            COLUMN_GRUPO_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_GRUPO_NOME + " TEXT NOT NULL);";

    //Comando para criar a tabela de contatos
    private static final String CREATE_TABLE_CONTATOS = "CREATE TABLE " + TABLE_CONTATOS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_TELEMOVEL + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_GRUPO_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_GRUPO_ID + ") REFERENCES " + TABLE_GRUPOS + "(" + COLUMN_GRUPO_ID2 + "));";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL(CREATE_TABLE_GRUPOS);
        bd.execSQL(CREATE_TABLE_CONTATOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTATOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRUPOS);
        onCreate(db);
    }

    // CRUD grupos

    // Adicionar grupo
    public long addGrupo(String nomeGrupo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GRUPO_NOME, nomeGrupo);

        long id = db.insert(TABLE_GRUPOS, null, values);
        db.close();  // Fecha a conexão com o banco de dados
        return id;
    }

    // Listar grupos
    public List<Grupo> getAllGrupos() {
        List<Grupo> listaGrupos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GRUPOS, null);

        if (cursor.moveToFirst()){
            do {
                Grupo grupo = new Grupo();
                grupo.setId(cursor.getInt(0));
                grupo.setNomeGrupo(cursor.getString(1));
                listaGrupos.add(grupo);
        } while (cursor.moveToNext());
    }
        cursor.close();
        return listaGrupos;
    }

    //Atualizar grupo
    public int updateGrupo(int id, String novoNomeGrupo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GRUPO_NOME, novoNomeGrupo);
        return db.update(TABLE_GRUPOS, values, COLUMN_GRUPO_ID2 + " = ?", new String[]{String.valueOf(id)});
    }

    //Deletar grupo
    public void deleteGrupo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GRUPOS, COLUMN_GRUPO_ID2 + " = ?", new String[]{String.valueOf(id)});
    }

    // CRUD contatos

    // Adicionar contato
    public long addContato(String nome, String telemovel, String email, int grupoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nome);
        values.put(COLUMN_TELEMOVEL, telemovel);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_GRUPO_ID, grupoId);
        return db.insert(TABLE_CONTATOS, null, values);
    }

    // Listar contatos
    public List<Contato> getAllContatos() {
        List<Contato> listaContatos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.id, c.nome, c.telemovel, c.email, c.grupo_id, g.nome_grupo " +
                        "FROM " + TABLE_CONTATOS + " c " +
                        "LEFT JOIN " + TABLE_GRUPOS + " g ON c.grupo_id = g.id",
                null);

        if (cursor.moveToFirst()){
            do {
                Contato contato = new Contato();
                contato.setId(cursor.getInt(0));
                contato.setName(cursor.getString(1));
                contato.setTelemovel(cursor.getString(2));
                contato.setEmail(cursor.getString(3));
                contato.setGrupoId(cursor.getInt(4));
                contato.setGrupoNome(cursor.getString(5));
                listaContatos.add(contato);
                Log.i(Tag, contato.toString());


            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaContatos;
    }

    // Atualizar contato
    public int updateContato(int id, String nome, String telemovel, String email, int grupoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, nome);
        values.put(COLUMN_TELEMOVEL, telemovel);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_GRUPO_ID, grupoId);
        return db.update(TABLE_CONTATOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //Excluir Contato
    public void deleteContato(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTATOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //Buscar contato por ID
    public Contato getContatoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.id, c.nome, c.telemovel, c.email, c.grupo_id, g.nome_grupo " +
                        "FROM " + TABLE_CONTATOS + " c " +
                        "LEFT JOIN " + TABLE_GRUPOS + " g ON c.grupo_id = g.id " +
                        "WHERE c.id = ?", new String[]{String.valueOf(id)});


        if (cursor.moveToFirst()) { // Se encontrou o contato
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0)); // Pega o ID
            contato.setName(cursor.getString(1)); // Pega o nome
            contato.setTelemovel(cursor.getString(2)); // Pega o telemóvel
            contato.setEmail(cursor.getString(3)); // Pega o email
            contato.setGrupoId(cursor.getInt(4)); // Pega o grupo ID
            contato.setGrupoNome(cursor.getString(5)); // Pega o nome do grupo
            cursor.close();
            return contato; // Retorna o contato preenchido
        }

        cursor.close();
        return null; // Retorna null se não encontrar o contato
    }


}
