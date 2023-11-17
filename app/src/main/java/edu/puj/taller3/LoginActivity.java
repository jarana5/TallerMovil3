package edu.puj.taller3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import edu.puj.taller3.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Main Page");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#287233")));

        mAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            binding.etCorreo.setText("");
            binding.etPassword.setText("");
        }
    }

    private void attemptSignIn(){
        String user =  binding.etCorreo.getText().toString();
        String password = binding.etPassword.getText().toString();

        if(!user.equalsIgnoreCase("") && !password.equalsIgnoreCase("")){
            mAuth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            } else {
                                Toast.makeText(LoginActivity.this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
        }
    }
}