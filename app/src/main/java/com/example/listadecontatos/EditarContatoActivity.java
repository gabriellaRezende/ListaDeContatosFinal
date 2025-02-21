package com.example.listadecontatos;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class EditarContatoActivity extends AppCompatActivity {

    private EditText edtNomeEditar, edtTelemovelEditar, edtEmailEditar;
    private Spinner spinnerGrupoEditar;
    private Button btnSalvarEditar, btnVoltarEditar, btnCriarGrupoEditar, btnExcluirEditar;
    private DataBase dbHelper;
    private int contatoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_contato_activity);

        //Inicializar banco de dados
        dbHelper = new DataBase(this);

        //Inicializar os elementos na tela
        edtNomeEditar = findViewById(R.id.edtNomeEditar);
        edtTelemovelEditar = findViewById(R.id.edtTelemovelEditar);
        edtEmailEditar = findViewById(R.id.edtEmailEditar);
        spinnerGrupoEditar = findViewById(R.id.spinnerGrupoEditar);
        btnSalvarEditar = findViewById(R.id.btnSalvarEditar);
        btnVoltarEditar = findViewById(R.id.btnVoltarEditar);
        btnCriarGrupoEditar = findViewById(R.id.btnCriarGrupoEditar);
        btnExcluirEditar = findViewById(R.id.btnExcluirEditar);

        //Receber o contatoId da Intent
        contatoId = getIntent().getIntExtra("contatoId", -1);
        if (contatoId != -1) {
            carregarContato();
        }

        //Carregar Grupos no Spinner
        carregarGrupos();

        //Configurar os botões
        btnSalvarEditar.setOnClickListener(v -> salvarContato());
        btnExcluirEditar.setOnClickListener(v -> confirmarExclusao());
        btnVoltarEditar.setOnClickListener(v -> finish());
        btnCriarGrupoEditar.setOnClickListener(v -> abrirModalCriarGrupo());
    }

    //Carregar os dados do contato no formulário
    private void carregarContato() {
        Contato contato = dbHelper.getContatoById(contatoId);
        if (contato != null) {
            edtNomeEditar.setText(contato.getName());
            edtTelemovelEditar.setText(contato.getTelemovel());
            edtEmailEditar.setText(contato.getEmail());
        } else {
            Toast.makeText(this, "Erro ao carregar o contato.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //Carregar Grupos no Spinner
    private void carregarGrupos() {
        List<Grupo> listaGrupos = dbHelper.getAllGrupos();
        ArrayAdapter<Grupo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaGrupos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupoEditar.setAdapter(adapter);
    }

    //Salvar o contato editado no banco
    private void salvarContato() {
        String nome = edtNomeEditar.getText().toString();
        String telemovel = edtTelemovelEditar.getText().toString();
        String email = edtEmailEditar.getText().toString();
        Grupo grupoSelecionado = (Grupo) spinnerGrupoEditar.getSelectedItem();
        int grupoId = grupoSelecionado != null ? grupoSelecionado.getId() : -1;

        if (!nome.isEmpty() && !telemovel.isEmpty()) {
            int linhasAfetadas = dbHelper.updateContato(contatoId, nome, telemovel, email, grupoId);

            if (linhasAfetadas > 0) {
                Toast.makeText(this, "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show();Intent intent = new Intent(EditarContatoActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); //Fechar a atividade após salvar
            } else {
                Toast.makeText(this, "Erro ao atualizar contato.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Nome e telefone são obrigatórios!", Toast.LENGTH_SHORT).show();
        }
    }

    //Dialog para confirmar a exclusão do contato
    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir este contato?")
                .setPositiveButton("Sim", (dialog, which) -> excluirContato())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    //Excluir o contato do banco
    private void excluirContato() {
        dbHelper.deleteContato(contatoId);
        Toast.makeText(this, "Contato excluído com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditarContatoActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); //Fechar a atividade após excluir
    }


    //Reutilizar modal de criar grupo
    private void abrirModalCriarGrupo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_grupo, null);
        builder.setView(dialogView);
        builder.setTitle("Criar Grupo");

        EditText edtNomeGrupo = dialogView.findViewById(R.id.edtNomeGrupo);
        Button btnCancelarGrupo = dialogView.findViewById(R.id.btnCancelarGrupo);
        Button btnSalvarGrupo = dialogView.findViewById(R.id.btnSalvarGrupo);

        AlertDialog dialog = builder.create();

        btnSalvarGrupo.setOnClickListener(v -> {
            String nomeGrupo = edtNomeGrupo.getText().toString().trim();
            if (!nomeGrupo.isEmpty()) {
                dbHelper.addGrupo(nomeGrupo);
                carregarGrupos();
                ((ArrayAdapter<Grupo>) spinnerGrupoEditar.getAdapter()).notifyDataSetChanged();
                Toast.makeText(this, "Grupo criado com sucesso!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Por favor, insira um nome para o grupo.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelarGrupo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

}