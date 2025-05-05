package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.PessoaAdapter;
import com.example.myapplication.model.Pessoa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PessoaAdapter.OnPessoaClickListener {

    private RecyclerView recyclerViewPessoas;
    private PessoaAdapter adapter;
    private List<Pessoa> pessoaList;
    private FloatingActionButton fabAdicionarPessoa;

    private CollectionReference pessoasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Inicializar Firebase de forma explícita
            FirebaseApp.initializeApp(this);
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            // Definir a referência para a coleção "pessoas"
            pessoasRef = firestore.collection("pessoas");

            // Inicializar Views
            recyclerViewPessoas = findViewById(R.id.recyclerViewPessoas);
            fabAdicionarPessoa = findViewById(R.id.fabAdicionarPessoa);

            // Configurar RecyclerView
            recyclerViewPessoas.setLayoutManager(new LinearLayoutManager(this));
            pessoaList = new ArrayList<>();
            adapter = new PessoaAdapter(pessoaList, this);
            recyclerViewPessoas.setAdapter(adapter);

            // Carregar pessoas do Firestore
            carregarPessoas();

            // Configurar FAB
            fabAdicionarPessoa.setOnClickListener(v -> mostrarDialogoAdicionarPessoa());

        } catch (Exception e) {
            Log.e("FIREBASE", "Erro ao inicializar Firebase: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao inicializar Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void carregarPessoas() {
        pessoasRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(MainActivity.this, "Erro ao carregar pessoas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            pessoaList.clear();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Pessoa pessoa = doc.toObject(Pessoa.class);
                    pessoaList.add(pessoa);
                }
            }
            adapter.setPessoas(pessoaList);
        });
    }

    private void mostrarDialogoAdicionarPessoa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Pessoa");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pessoa, null);
        EditText editTextNome = view.findViewById(R.id.editTextNome);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextTelefone = view.findViewById(R.id.editTextTelefone);
        EditText editTextEndereco = view.findViewById(R.id.editTextEndereco);
        EditText editTextIdade = view.findViewById(R.id.editTextIdade);

        builder.setView(view);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String nome = editTextNome.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String telefone = editTextTelefone.getText().toString().trim();
            String endereco = editTextEndereco.getText().toString().trim();

            int idade = 0;
            try {
                idade = Integer.parseInt(editTextIdade.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Idade inválida. Defina um número.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nome.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira um nome", Toast.LENGTH_SHORT).show();
                return;
            }

            adicionarPessoa(nome, email, telefone, endereco, idade);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void mostrarDialogoEditarPessoa(Pessoa pessoa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Pessoa");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pessoa, null);
        EditText editTextNome = view.findViewById(R.id.editTextNome);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextTelefone = view.findViewById(R.id.editTextTelefone);
        EditText editTextEndereco = view.findViewById(R.id.editTextEndereco);
        EditText editTextIdade = view.findViewById(R.id.editTextIdade);

        // Preencher com dados existentes
        editTextNome.setText(pessoa.getNome());
        editTextEmail.setText(pessoa.getEmail());
        editTextTelefone.setText(pessoa.getTelefone());
        editTextEndereco.setText(pessoa.getEndereco());
        editTextIdade.setText(String.valueOf(pessoa.getIdade()));

        builder.setView(view);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String nome = editTextNome.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String telefone = editTextTelefone.getText().toString().trim();
            String endereco = editTextEndereco.getText().toString().trim();

            int idade = 0;
            try {
                idade = Integer.parseInt(editTextIdade.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Idade inválida. Defina um número.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nome.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira um nome", Toast.LENGTH_SHORT).show();
                return;
            }

            atualizarPessoa(pessoa.getId(), nome, email, telefone, endereco, idade);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void adicionarPessoa(String nome, String email, String telefone, String endereco, int idade) {
        String id = pessoasRef.document().getId();
        Pessoa pessoa = new Pessoa(id, nome, email, telefone, endereco, idade);
        pessoasRef.document(id).set(pessoa)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Pessoa adicionada com sucesso", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Erro ao adicionar pessoa: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void atualizarPessoa(String id, String nome, String email, String telefone, String endereco, int idade) {
        Pessoa pessoa = new Pessoa(id, nome, email, telefone, endereco, idade);
        pessoasRef.document(id).set(pessoa)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Pessoa atualizada com sucesso", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Erro ao atualizar pessoa: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void excluirPessoa(String id) {
        pessoasRef.document(id).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Pessoa excluída com sucesso", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Erro ao excluir pessoa: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onEditClick(Pessoa pessoa) {
        mostrarDialogoEditarPessoa(pessoa);
    }

    @Override
    public void onDeleteClick(Pessoa pessoa) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Pessoa")
                .setMessage("Tem certeza que deseja excluir esta pessoa do cadastro?")
                .setPositiveButton("Sim", (dialog, which) -> excluirPessoa(pessoa.getId()))
                .setNegativeButton("Não", null)
                .show();
    }
}