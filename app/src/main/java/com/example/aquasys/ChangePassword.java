package com.example.aquasys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private TextView editTextCurrentUser;
    private EditText editTextCurrentPassword;
    private EditText editTextNewPassword;
    private Button btnChangePassword;
    private FirebaseAuth firebaseAuth;
    private Toolbar changepass_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextCurrentUser = findViewById(R.id.TextCurrentUser);
        editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnChangePassword.setOnClickListener(view -> {
            String currentPassword = editTextCurrentPassword.getText().toString();
            String newPassword = editTextNewPassword.getText().toString();

            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null && !currentPassword.isEmpty() && !newPassword.isEmpty()) {
                // Thực hiện thay đổi mật khẩu
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                user.reauthenticate(credential)
                        .addOnCompleteListener(reauthTask -> {
                            if (reauthTask.isSuccessful()) {
                                // Re-authentication thành công, thực hiện thay đổi mật khẩu
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(changePasswordTask -> {
                                            if (changePasswordTask.isSuccessful()) {
                                                Toast.makeText(ChangePassword.this, "Change Password Completed", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(ChangePassword.this, "Change Password Failed", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                            } else {
                                Toast.makeText(ChangePassword.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });

        changepass_toolbar =(Toolbar) findViewById(R.id.change_password_toolbar);
        changepass_toolbar.setTitle("Change Password");
        setBackButtonOnToolbar();
    }
    public void setBackButtonOnToolbar() {
        setSupportActionBar(changepass_toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }
        changepass_toolbar.setNavigationOnClickListener(v -> finish());
    }
}