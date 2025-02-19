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

    //Utilizar layout do AddContatoActivity para editar
    private EditText edtNome, edtTelemovel, edtEmail;
    private Spinner spinnerGrupo;
    private Button btnSalvar, btnVoltar, btnCriarGrupo, btnExcluir;
    private DataBase dbHelper;
    private int contatoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contato); //Utilizar layout do AddContatoActivity

        //Inicializar banco e elementos da tela
        dbHelper = new DataBase(this);
        edtNome = findViewById(R.id.edtNome);
        edtTelemovel = findViewById(R.id.edtTelemovel);
        edtEmail = findViewById(R.id.edtEmail);
        spinnerGrupo = findViewById(R.id.spinnerGrupo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnCriarGrupo = findViewById(R.id.btnCriarGrupo);
        btnExcluir = findViewById(R.id.btnExcluir); //Botão de Exclusão

        //Receber o contatoId da Intent
        Intent intent = getIntent();
        contatoId = intent.getIntExtra("contatoId", -1);
        String nome = intent.getStringExtra("nome");
        String telemovel = intent.getStringExtra("telemovel");
        String email = intent.getStringExtra("email");
        int grupoId = intent.getIntExtra("grupoId", -1);

        //Preencher os campos com os dados do contato
        edtNome.setText(nome);
        edtTelemovel.setText(telemovel);
        edtEmail.setText(email);

        //Carregar Grupos no Spinner
        carregarGrupos();
        spinnerGrupo.setSelection(grupoId);

        //Botão para salvar contato editado
        btnSalvar.setOnClickListener(v -> salvarContato());

        //Botão para voltar à tela anterior
        btnVoltar.setOnClickListener(v -> finish());

        //Botão para criar grupo
        btnCriarGrupo.setOnClickListener(v -> abrirModalCriarGrupo());

        //Botão para excluir contato
        btnExcluir.setOnClickListener(v -> confirmarExclusao());

    }

    //Carregar Grupos no Spinner
    private void carregarGrupos() {
        List<Grupo> listaGrupos = dbHelper.getAllGrupos();
        ArrayAdapter<Grupo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaGrupos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);
    }

    //Salvar o contato editado no banco
    private void salvarContato() {
        String nome = edtNome.getText().toString();
        String telemovel = edtTelemovel.getText().toString();
        String email = edtEmail.getText().toString();

        Grupo grupoSelecionado = (Grupo) spinnerGrupo.getSelectedItem();
        int grupoId = grupoSelecionado.getId();

        if (!nome.isEmpty() && !telemovel.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DataBase.COLUMN_NAME, nome);
            values.put(DataBase.COLUMN_TELEMOVEL, telemovel);
            values.put(DataBase.COLUMN_EMAIL, email);
            values.put(DataBase.COLUMN_GRUPO_ID, grupoId);

            int linhasAfetadas = db.update(DataBase.TABLE_CONTATOS, values, DataBase.COLUMN_ID + " = ?", new String[]{String.valueOf(contatoId)});
            db.close();

            if (linhasAfetadas > 0) {
                Toast.makeText(this, "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); //Fechar a atividade após salvar
            } else {
                Toast.makeText(this, "Erro ao atualizar contato.", Toast.LENGTH_SHORT).show();
            }
            } else {
                Toast.makeText(this, "Nome e telefone são obrigatórios!", Toast.LENGTH_SHORT).show();
            }
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
                ((ArrayAdapter<Grupo>) spinnerGrupo.getAdapter()).notifyDataSetChanged();
                Toast.makeText(this, "Grupo criado com sucesso!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Por favor, insira um nome para o grupo.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelarGrupo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    //Confirmar a exclusão do contato
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataBase.TABLE_CONTATOS, DataBase.COLUMN_ID + " = ?", new String[]{String.valueOf(contatoId)});
        db.close();
        Toast.makeText(this, "Contato excluído com sucesso!", Toast.LENGTH_SHORT).show();
        finish(); //Fechar a atividade após excluir
    }
}