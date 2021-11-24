package com.mcommerce.nhom8.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mcommerce.nhom8.R;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnGui_reset;
    TextInputLayout inpEmail_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        linkview();
        addEvent();


    }

    private void linkview() {
        inpEmail_reset = findViewById(R.id.inpEmail_reset);
        btnGui_reset = findViewById(R.id.btnGui_reset);

    }

    private void addEvent() {
        btnGui_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = inpEmail_reset.getEditText().getText().toString().trim();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this,"Đường dẫn đặt lại mật khẩu đã được gửi, vui lòng kiểm tra hộp thư của bạn",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}