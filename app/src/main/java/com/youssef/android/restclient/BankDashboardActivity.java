package com.youssef.android.restclient;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youssef.android.restclient.ui.adapter.AccountAdapter;
import com.youssef.android.restclient.model.Account;
import ma.projet.restclient.repository.AccountRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDashboardActivity extends AppCompatActivity implements AccountAdapter.OnDeleteClickListener, AccountAdapter.OnUpdateClickListener {
    private RecyclerView recyclerView;
    private AccountAdapter adapter;
    private RadioGroup formatGroup;
    private FloatingActionButton addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupFormatSelection();
        setupAddButton();

        loadData("JSON");
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        formatGroup = findViewById(R.id.formatGroup);
        addbtn = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFormatSelection() {
        formatGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String format = checkedId == R.id.radioJson ? "JSON" : "XML";
            loadData(format);
        });
    }

    private void setupAddButton() {
        addbtn.setOnClickListener(v -> showAddAccountDialog());
    }

    private void showAddAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BankDashboardActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_Account, null);

        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);

        builder.setView(dialogView)
                .setTitle("Ajouter un Account")
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String solde = etSolde.getText().toString();
                    String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant
                            ? "COURANT" : "EPARGNE";

                    String formattedDate = getCurrentDateFormatted();
                    Account Account = new Account(null, Double.parseDouble(solde), type, formattedDate);
                    addAccount(Account);
                })
                .setNegativeButton("Annuler", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getCurrentDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(calendar.getTime());
    }

    private void addAccount(Account Account) {
        AccountRepository AccountRepository = new AccountRepository("JSON");
        AccountRepository.addAccount(Account, new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    showToast("Account ajouté");
                    loadData("JSON");
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                showToast("Erreur lors de l'ajout");
            }
        });
    }

    private void loadData(String format) {
        AccountRepository AccountRepository = new AccountRepository(format);
        AccountRepository.getAllAccount(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Account> Accounts = response.body();
                    runOnUiThread(() -> adapter.updateData(Accounts));
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                showToast("Erreur: " + t.getMessage());
            }
        });
    }

    @Override
    public void onUpdateClick(Account Account) {
        showUpdateAccountDialog(Account);
    }

    private void showUpdateAccountDialog(Account Account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BankDashboardActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_Account, null);

        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);
        etSolde.setText(String.valueOf(Account.getSolde()));
        if (Account.getType().equalsIgnoreCase("COURANT")) {
            typeGroup.check(R.id.radioCourant);
        } else if (Account.getType().equalsIgnoreCase("EPARGNE")) {
            typeGroup.check(R.id.radioEpargne);
        }

        builder.setView(dialogView)
                .setTitle("Modifier un Account")
                .setPositiveButton("Modifier", (dialog, which) -> {
                    String solde = etSolde.getText().toString();
                    String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant
                            ? "COURANT" : "EPARGNE";
                    Account.setSolde(Double.parseDouble(solde));
                    Account.setType(type);
                    updateAccount(Account);
                })
                .setNegativeButton("Annuler", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAccount(Account Account) {
        AccountRepository AccountRepository = new AccountRepository("JSON");
        AccountRepository.updateAccount(Account.getId(), Account, new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    showToast("Account modifié");
                    loadData("JSON");
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                showToast("Erreur lors de la modification");
            }
        });
    }

    @Override
    public void onDeleteClick(Account Account) {
        showDeleteConfirmationDialog(Account);
    }

    private void showDeleteConfirmationDialog(Account Account) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous vraiment supprimer ce Account ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteAccount(Account))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteAccount(Account Account) {
        AccountRepository AccountRepository = new AccountRepository("JSON");
        AccountRepository.deleteAccount(Account.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Account supprimé");
                    loadData("JSON");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Erreur lors de la suppression");
            }
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(BankDashboardActivity.this, message, Toast.LENGTH_LONG).show());
    }
}
