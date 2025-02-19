package com.example.listadecontatos;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContatoAdapter adapter;
    private List<Contato> listaContatos;
    private FloatingActionButton fabAddContato;
    private DataBase dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewContatos);
        fabAddContato = findViewById(R.id.fabAddContato);
        dbHelper = new DataBase(this); //Inicializa os dados

        // Configuração do RecyclerView
        listaContatos = new ArrayList<>();
        adapter = new ContatoAdapter(this, listaContatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Carregar os contatos salvos no banco
        carregarContatos();

        //Evento de clique no botão de adicionar contato
        fabAddContato.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContatoActivity.class);
            startActivity(intent);
        });

        // Simular dados (deve comentar esse trecho quando ja tiver dados)
        //listaContatos.add(new Contato("Gabriella", "345678567", "Trabalho@email.com", "Familia"));
        //listaContatos.add(new Contato("Carlos", "987654321", "Trabalho@testemail.com", "Trabalho"));
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarContatos(); //Atualiza a lista sempre que a tela é reaberta
    }

    private void carregarContatos() {
        listaContatos.clear(); //Limpa a lista para evitar duplicidade
        listaContatos.addAll(dbHelper.getAllContatos()); //Busca os contatos do banco
        adapter.notifyDataSetChanged(); //Atualiza a reciclerView

    }
}