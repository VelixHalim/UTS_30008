package umn.ac.id.uts_30008;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private Button btn_Login;
    private EditText etUsername, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.edtUsername);
        etPassword = findViewById(R.id.etPassword);

        btn_Login = findViewById(R.id.btnLogin);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(etUsername.getText().toString().equals("uasmobile") && etPassword.getText().toString().equals("uasmobilegenap")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            Login.this
                    );
                    builder.setIcon(R.drawable.ic_check);
                    builder.setTitle("Login Successfully !!");
                    builder.setMessage("Welcome to Android Coding...");
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    startActivity(new Intent(Login.this, ListLagu.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Username & Password",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}