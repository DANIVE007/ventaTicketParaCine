package cl.inacap.eval3appmovil_iturra;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatosAdmin extends AppCompatActivity {
    private TextView sesionadmin3;
    private EditText email, tipo1, passw, regnom;
    private FloatingActionButton save2, delete3;


    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_admin);

        sesionadmin3 = (TextView) findViewById(R.id.sesionadmin3);
        regnom = (EditText)  findViewById(R.id.regnom);
        email = (EditText) findViewById(R.id.email);
        tipo1 = (EditText) findViewById(R.id.tipo1);
        passw = (EditText) findViewById(R.id.passw);
        save2 = (FloatingActionButton) findViewById(R.id.save2);
        delete3 = (FloatingActionButton) findViewById(R.id.delete3);



        Intent i = getIntent();
        u = (Usuario) i.getParcelableExtra("u");

        sesionadmin3.setText("Usuario : " + u.getNom().toUpperCase());
        regnom.setText(u.getNom().toUpperCase());
        email.setText(u.getEma().toLowerCase());
        tipo1.setText(u.getTipo().toUpperCase());
        passw.setText(u.getPassword().toUpperCase());

        botonEliminar();
        botonModificar();


    }

    private void botonModificar(){
        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (regnom.getText().toString().trim().isEmpty()
                        || email.getText().toString().trim().isEmpty()
                        || tipo1.getText().toString().trim().isEmpty()
                        || passw.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DatosAdmin.this, "Complete los Campos Faltantes para actualizar", Toast.LENGTH_LONG).show();


                } else {
                    String nombre = regnom.getText().toString();
                    String e_mail = email.getText().toString();
                    String perfil = tipo1.getText().toString();
                    String password2 = passw.getText().toString();


                    FirebaseDatabase dbss = FirebaseDatabase.getInstance();
                    DatabaseReference dbrefer = dbss.getReference(Perfiles.class.getSimpleName());


                    dbrefer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean haya = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("e_mail").getValue().toString().equalsIgnoreCase(e_mail)){
                                    haya = true;
                                    ocultarTeclado();
                                    x.getRef().child("nombre").setValue(nombre);
                                    x.getRef().child("e_mail").setValue(e_mail);
                                    x.getRef().child("perfil").setValue(perfil);
                                    x.getRef().child("password2").setValue(password2);
                                    Toast.makeText(DatosAdmin.this, "Registro Modificado Exitosamente", Toast.LENGTH_SHORT).show();

                                    regnom.setText("");
                                    email.setText("");
                                    tipo1.setText("");
                                    passw.setText("");
                                    Toast.makeText(DatosAdmin.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                                    Intent e =new Intent(DatosAdmin.this,Principal.class);
                                    startActivity(e);
                                    finish();
                                    break;

                                }
                            }
                            if( haya == false) {

                                ocultarTeclado();
                                Toast.makeText(DatosAdmin.this, "Registro  No Encontrado.\n No se puede Modificar", Toast.LENGTH_SHORT).show();
                                regnom.setText("");
                                email.setText("");
                                tipo1.setText("");
                                passw.setText("");
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }


        });
    }//Cierra el Modificar

    private void botonEliminar(){
        delete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String imail = email.getText().toString();
                    FirebaseDatabase dbss = FirebaseDatabase.getInstance();
                    DatabaseReference dbrefer = dbss.getReference(Perfiles.class.getSimpleName());

                    dbrefer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final boolean[] rep = {false};
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("e_mail").getValue().toString().equalsIgnoreCase(imail)){
                                    AlertDialog.Builder a =new AlertDialog.Builder(DatosAdmin.this);
                                    a.setCancelable(false);
                                    a.setTitle("Pregunta");
                                    a.setMessage("Esta seguro(a) de Eliminar registro con email "+imail+"?");
                                    a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    a.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {

                                            rep[0] =true;
                                            ocultarTeclado();
                                            Toast.makeText(DatosAdmin.this, "Perfil Eliminado con email: "+imail+"  Eliminado Exitosamente", Toast.LENGTH_SHORT).show();
                                            x.getRef().removeValue();
                                            regnom.setText("");
                                            email.setText("");
                                            tipo1.setText("");
                                            passw.setText("");
                                            Toast.makeText(DatosAdmin.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                                            Intent e =new Intent(DatosAdmin.this,Principal.class);
                                            startActivity(e);
                                            finish();




                                        }
                                    });

                                    a.show();
                                    break;

                                }
                            }
                            if (rep[0] == false){
                                ocultarTeclado();;
                                Toast.makeText(DatosAdmin.this, "Registro con email "+imail+" No fue Eliminado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            }
        });

    }//Cierra el Eliminar


    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}//Cierra el Ocultar teclado

