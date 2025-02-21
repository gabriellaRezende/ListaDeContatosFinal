package com.example.listadecontatos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddContatoActivity extends AppCompatActivity {

    private EditText edtNome, edtTelemovel, edtEmail;
    private Spinner spinnerGrupo;
    private Button btnSalvar, btnVoltar, btnCriarGrupo;
    private DataBase dbHelper;
    private Contato contatoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contato);

        //Inicializar banco e elementos da tela
        dbHelper = new DataBase(this);
        edtNome = findViewById(R.id.edtNome);
        edtTelemovel = findViewById(R.id.edtTelemovel);
        edtEmail = findViewById(R.id.edtEmail);
        spinnerGrupo = findViewById(R.id.spinnerGrupo);
        btnSalvar = findViewById(R.id.btnSalvar);

        if (btnSalvar != null) {
            btnSalvar.setVisibility(View.VISIBLE);
        }else {
            Log.e("DEBUG", "Erro: btnSalvar não encontrado no layout!");
        }

        btnVoltar = findViewById(R.id.btnVoltar);
        btnCriarGrupo = findViewById(R.id.btnCriarGrupo);

        //Carregar Grupos no Spinner
        carregarGrupos();

        //Botão para salvar contato
        btnSalvar.setOnClickListener(v -> salvarContato());

        //Botão para voltar à tela anterior
        btnVoltar.setOnClickListener(v -> finish());

        //Botão para criar grupo
        btnCriarGrupo.setOnClickListener(v -> abrirModalCriarGrupo());

    }

    private void carregarDadosParaEdicao() {
        if (contatoAtual != null) {
            edtNome.setText(contatoAtual.getName());
            edtTelemovel.setText(contatoAtual.getTelemovel());
            edtEmail.setText(contatoAtual.getEmail());

            // Selecionar o grupo no Spinner
            List<Grupo> listaGrupos = dbHelper.getAllGrupos();
            for (int i = 0; i < listaGrupos.size(); i++) {
                if (listaGrupos.get(i).getId() == contatoAtual.getGrupoId()) { // Agora pegamos o ID correto
                    spinnerGrupo.setSelection(i);
                    break;
                }
            }
        }
    }

    //Carregar Grupos no Spinner
    private void carregarGrupos() {
        List<Grupo> listaGrupos = dbHelper.getAllGrupos();
        ArrayAdapter<Grupo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaGrupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);
    }

    //Salvar o contato no banco
    private void salvarContato() {
        String nome = edtNome.getText().toString().trim();
        String telemovel = edtTelemovel.getText().toString().trim();
        String email = edtEmail.getText().toString();

        Grupo grupoSelecionado = (Grupo) spinnerGrupo.getSelectedItem();
        int grupoId = (grupoSelecionado != null) ? grupoSelecionado.getId() : -1;

        if (nome.isEmpty() || telemovel.isEmpty()) {
            Toast.makeText(this, "Nome e telefone são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        long resultado;
        if (contatoAtual == null) {
            //Usar metodo de adicionar contatoda classe DataBase
            resultado = dbHelper.addContato(nome, telemovel, email, grupoId);
            Log.d("DEBUG_DB", "Contato inserido, ID: " + resultado);
        } else {
            // Criar um objeto contato e atualizar com usando um metodo especifico
            contatoAtual.setName(nome);
            contatoAtual.setTelemovel(telemovel);
            contatoAtual.setEmail(email);
            contatoAtual.setGrupoId(grupoId);

            resultado = dbHelper.updateContato(
                    contatoAtual.getId(),
                    contatoAtual.getName(),
                    contatoAtual.getTelemovel(),
                    contatoAtual.getEmail(),
                    contatoAtual.getGrupoId()
            );
            Log.d("DEBUG_DB", "Contato atualizado, linhas afetadas: " + resultado);
        }

        if (resultado == -1) {
            Log.e("DEBUG_DB", "Erro ao salvar contato!");
            Toast.makeText(this, "Erro ao salvar contato", Toast.LENGTH_SHORT).show();
        } else {
            finish(); // Volta para a tela de listagem
        }
        }

    //Abrir modal e Criar Grupo
    private void abrirModalCriarGrupo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_grupo, null);
        builder.setView(dialogView);
        builder.setTitle("Criar Grupo");

        EditText edtNomeGrupo = dialogView.findViewById(R.id.edtNomeGrupo);
        Button btnCancelarGrupo = dialogView.findViewById(R.id.btnCancelarGrupo);
        Button btnSalvarGrupo = dialogView.findViewById(R.id.btnSalvarGrupo);

        AlertDialog dialog = builder.create();

        //Ação de Salvar
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

        //Ação de Cancelar
        btnCancelarGrupo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        }
}


