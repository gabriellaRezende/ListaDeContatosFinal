package com.example.listadecontatos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

public class DetalhesContatoActivity extends AppCompatActivity {

    private TextView txtNome, txtTelemovel, txtEmail;
    private Chip chipGrupoDetalhes;
    private Button btnVoltarDetalhes, btnEditar;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_contato);

        //Referencias aos elementos da UI
        txtNome = findViewById(R.id.txtNome);
        txtTelemovel = findViewById(R.id.txtTelemovel);
        txtEmail = findViewById(R.id.txtEmail);
        chipGrupoDetalhes = findViewById(R.id.chipGrupoDetalhes);

        btnVoltarDetalhes = findViewById(R.id.btnVoltarDetalhes);
        btnEditar = findViewById(R.id.btnEditar);

        //Obter o objeto Contato da Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("contato")) {
            contato = intent.getParcelableExtra("contato");

            if (contato != null) {
            //Preencher os campos com os dados do Contato
            txtNome.setText(contato.getName());
            txtTelemovel.setText(contato.getTelemovel());
            txtEmail.setText(contato.getEmail());
            chipGrupoDetalhes.setText(contato.getGrupoNome());
        } else {
                Toast.makeText(this, "Erro ao carregar o contato", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
                Toast.makeText(this, "Contato não encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }

        //Ação para ligar quando estiver no numero de telefone
        txtTelemovel.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contato.getTelemovel()));
            startActivity(callIntent);

        });

        //Ação para voltar para a lista de contatos
        btnVoltarDetalhes.setOnClickListener(v -> finish());

        //Ação de Editar
        btnEditar.setOnClickListener(v -> {
            Intent intentEditar = new Intent(DetalhesContatoActivity.this, AddContatoActivity.class);

            //Passa os dados do contato para a tela de edição
            intent.putExtra("id", contato.getId());
            intent.putExtra("nome", contato.getName());
            intent.putExtra("telemovel", contato.getTelemovel());
            intent.putExtra("email", contato.getEmail());
            intent.putExtra("grupo", contato.getGrupoNome());

            startActivity(intentEditar);
        });
    }

}