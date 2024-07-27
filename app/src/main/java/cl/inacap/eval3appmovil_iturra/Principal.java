package cl.inacap.eval3appmovil_iturra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private EditText txtusu, txtpas;
    private Button btnini;



    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtusu = (EditText) findViewById(R.id.txtusu);
        txtpas = (EditText) findViewById(R.id.txtpas);
        btnini = (Button) findViewById(R.id.btnini);


        btnini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtusu.getText().toString().trim().isEmpty() || txtpas.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Principal.this, "Complete los Campos para Continuar", Toast.LENGTH_SHORT).show();
                    ocultarTeclado();
                } else {

                    String nom = txtusu.getText().toString();
                    String ema = nom + "@gmail.com";
                    String tipo = "";
                    String password = txtpas.getText().toString();


                    if (nom.equalsIgnoreCase("ADMIN") && password.equalsIgnoreCase("inacap")) {
                        tipo = "Administrador";
                        Usuario u;
                        u = new Usuario(nom, ema, tipo, password);
                        Intent i = new Intent(Principal.this, MenuAdmin.class);
                        i.putExtra("u", u);
                        startActivity(i);
                        finish();
                    }else{
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference dbref = db.getReference(Perfiles.class.getSimpleName());
                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot x : snapshot.getChildren()) {
                                    if(x.child("nombre").getValue().toString().equalsIgnoreCase(nom) && x.child("password2").getValue().toString().equalsIgnoreCase(password)) {

                                        String ema = x.child("e_mail").getValue().toString();
                                        String tipo = x.child("perfil").getValue().toString();

                                        Toast.makeText(Principal.this, "Usuario Encontrado", Toast.LENGTH_SHORT).show();
                                        Usuario u = new Usuario(nom, ema, tipo, password);
                                        Intent i = new Intent(Principal.this, MenuUsuarios.class);
                                        i.putExtra("u", u);
                                        startActivity(i);
                                        return;

                                    }else{

                                        Toast.makeText(Principal.this, "Error de Usuario y/o Contraseña", Toast.LENGTH_SHORT).show();
                                        txtusu.setText("");
                                        txtpas.setText("");
                                        txtusu.requestFocus();
                                        ocultarTeclado();
                                    }
                                    return;


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                }
            }

    });
    }
}