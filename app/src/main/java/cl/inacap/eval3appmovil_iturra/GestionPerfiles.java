package cl.inacap.eval3appmovil_iturra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GestionPerfiles extends AppCompatActivity {

    private TextView sesionadmin2;
    private ListView tickdatos2;
    private EditText rutusua, nomusua, ape1usu, ape2usu, numid;
    private FloatingActionButton guardado2, delete2, modificar2;
    private Button btnbuscar2;

    private Usuario u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_perfiles);


        sesionadmin2 = (TextView) findViewById(R.id.sesionadmin2);
        rutusua = (EditText) findViewById(R.id.rutusua);
        nomusua = (EditText) findViewById(R.id.nomusua);
        ape1usu = (EditText) findViewById(R.id.ape1usu);
        ape2usu = (EditText) findViewById(R.id.ape2usu);
        numid = (EditText) findViewById(R.id.numid);
        tickdatos2 = (ListView) findViewById(R.id.tickdatos2);
        btnbuscar2 = (Button) findViewById(R.id.btnbuscar2);
        delete2 = (FloatingActionButton) findViewById(R.id.delete2);
        modificar2 = (FloatingActionButton) findViewById(R.id.modificar2);
        guardado2 = (FloatingActionButton) findViewById(R.id.guardado2);


        Intent i = getIntent();
        u = (Usuario) i.getParcelableExtra("u");
        sesionadmin2.setText("Usuario : " + u.getNom().toUpperCase());

            botonRegistrar();
            listarPerfiles();
            buscarPerfiles();
            botonEliminar();
            botonModificar();

    }
    private void botonRegistrar() {
        guardado2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rutusua.getText().toString().trim().isEmpty()
                        || nomusua.getText().toString().trim().isEmpty()
                        || ape1usu.getText().toString().trim().isEmpty()
                        || ape2usu.getText().toString().trim().isEmpty()) {
                    Toast.makeText(GestionPerfiles.this, "Complete los Todos Los Campos", Toast.LENGTH_LONG).show();

                } else {

                    String nombre = rutusua.getText().toString();
                    String e_mail = nomusua.getText().toString();
                    String perfil= ape1usu.getText().toString();
                    String password2 = ape2usu.getText().toString();

                    FirebaseDatabase dbss= FirebaseDatabase.getInstance();
                    DatabaseReference dbrefer = dbss.getReference(Perfiles.class.getSimpleName());
                    dbrefer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean rep = false;
                            for (DataSnapshot x : snapshot.getChildren()) {
                                if (x.child("e_mail").getValue().toString().equalsIgnoreCase(e_mail)) {
                                    rep = true;
                                    ocultarTeclado();
                                    Toast.makeText(GestionPerfiles.this, "Error, El e_mail " + e_mail + " Ya Existe", Toast.LENGTH_SHORT).show();
                                    break;

                                }
                            }
                            if (rep == false) {
                                Perfiles per = new Perfiles(nombre, e_mail, perfil, password2);
                                dbrefer.push().setValue(per);
                                ocultarTeclado();
                                Toast.makeText(GestionPerfiles.this, "Perfil Registrado Exitosamente", Toast.LENGTH_SHORT).show();
                                rutusua.setText("");
                                nomusua.setText("");
                                ape1usu.setText("");
                                ape2usu.setText("");
                                return;

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }// cierra el if y else




            }
        });

    }//cierre btn reg

    private void listarPerfiles(){
        FirebaseDatabase dbss = FirebaseDatabase.getInstance();
        DatabaseReference dbrefer= dbss.getReference(Perfiles.class.getSimpleName());

        ArrayList<Perfiles> listper = new ArrayList<Perfiles>();
        ArrayAdapter<Perfiles> ada = new ArrayAdapter<Perfiles>(GestionPerfiles.this, android.R.layout.simple_list_item_1,listper);
        tickdatos2.setAdapter(ada);

        dbrefer.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Perfiles per = snapshot.getValue(Perfiles.class);
                listper.add(per);
                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        tickdatos2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Perfiles per = listper.get(i);
                AlertDialog.Builder a = new AlertDialog.Builder(GestionPerfiles.this);
                a.setCancelable(true);
                a.setTitle("PERFIL SELECCIONADO");
                String msg = "NOMBRE : " + per.getNombre() + "\n\n";
                msg += "EMAIL : " + per.getE_mail() + "\n\n";
                msg += "TIPO : " + per.getPerfil() + "\n\n";
                msg += "PASSWORD : " + per.getPassword2() + "\n\n";

                a.setMessage(msg);
                a.show();
            }
        });
    }//cierra el metodo listartickets
    private void buscarPerfiles(){
        btnbuscar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numid.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(GestionPerfiles.this, "Digite El ID a Buscar", Toast.LENGTH_SHORT).show();
                }else{
                    String Registro = new String(numid.getText().toString());

                    FirebaseDatabase dbs = FirebaseDatabase.getInstance();
                    DatabaseReference dbrefe = dbs.getReference(Perfiles.class.getSimpleName());

                    dbrefe.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = new String(Registro.toString());

                            boolean rep = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("idd").getValue().toString().equalsIgnoreCase(aux)){
                                    rep =true;
                                    ocultarTeclado();
                                    numid.setText(x.child("idd").getValue().toString());
                                    rutusua.setText(x.child("rutper").getValue().toString());
                                    nomusua.setText(x.child("nomper").getValue().toString());
                                    ape1usu.setText(x.child("ape1per").getValue().toString());
                                    ape2usu.setText(x.child("ape2per").getValue().toString());

                                    break;

                                }
                            }
                            if (rep == false){
                                ocultarTeclado();;
                                Toast.makeText(GestionPerfiles.this, "ID ("+aux+") No Encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }//cierra el if/else

            }
        });

    }//cierra buscar
    private void botonEliminar(){
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numid.getText().toString().trim().isEmpty()){

                    ocultarTeclado();
                    Toast.makeText(GestionPerfiles.this, "Digite el ID a Eliminar", Toast.LENGTH_SHORT).show();
                }else{
                    String Registro = new String(numid.getText().toString());

                    FirebaseDatabase dbs = FirebaseDatabase.getInstance();
                    DatabaseReference dbrefe = dbs.getReference(Perfiles.class.getSimpleName());

                    dbrefe.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = new String(Registro.toString());
                            final boolean[] rep = {false};
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("idd").getValue().toString().equalsIgnoreCase(aux)){
                                    AlertDialog.Builder a =new AlertDialog.Builder(GestionPerfiles.this);
                                    a.setCancelable(false);
                                    a.setTitle("Pregunta");
                                    a.setMessage("Esta seguro(a) de Eliminar El ID N°"+aux+"");
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
                                            Toast.makeText(GestionPerfiles.this, "ID "+aux+"  Eliminado Exitosamente", Toast.LENGTH_SHORT).show();
                                            x.getRef().removeValue();
                                            listarPerfiles();

                                        }
                                    });

                                    a.show();
                                    break;

                                }
                            }
                            if (rep[0] == false){
                                ocultarTeclado();;
                                Toast.makeText(GestionPerfiles.this, "ID "+aux+" No fue Eliminado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }//Cierra el Eliminar
    private void botonModificar(){
        modificar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numid.getText().toString().trim().isEmpty()
                        ||rutusua.getText().toString().trim().isEmpty()
                        || nomusua.getText().toString().trim().isEmpty()
                        || ape1usu.getText().toString().trim().isEmpty()
                        || ape2usu.getText().toString().trim().isEmpty()) {
                    Toast.makeText(GestionPerfiles.this, "Complete los Campos Faltantes para actualizar", Toast.LENGTH_LONG).show();


                } else {
                    String idd = numid.getText().toString();
                    String rutper = rutusua.getText().toString();
                    String nomper = nomusua.getText().toString();
                    String ape1per = ape1usu.getText().toString();
                    String ape2per = ape2usu.getText().toString();



                    FirebaseDatabase dbs = FirebaseDatabase.getInstance();
                    DatabaseReference dbrefe = dbs.getReference(Perfiles.class.getSimpleName());


                    dbrefe.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            boolean haya = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("idd").getValue().toString().equalsIgnoreCase(idd)){
                                    haya = true;
                                    ocultarTeclado();
                                    x.getRef().child("idd").setValue(rutper);
                                    x.getRef().child("rutper").setValue(rutper);
                                    x.getRef().child("nomper").setValue(nomper);
                                    x.getRef().child("ape1per").setValue(ape1per);
                                    x.getRef().child("ape2per").setValue(ape2per);

                                    Toast.makeText(GestionPerfiles.this, "Registro Modificado Exitosamente", Toast.LENGTH_SHORT).show();

                                    numid.setText("");
                                    rutusua.setText("");
                                    nomusua.setText("");
                                    ape1usu.setText("");
                                    ape2usu.setText("");


                                    listarPerfiles();
                                    break;

                                }
                            }
                            if( haya == false) {

                                ocultarTeclado();
                                Toast.makeText(GestionPerfiles.this, "Registro  No Encontrado.\n No se puede Modificar", Toast.LENGTH_SHORT).show();
                                numid.setText("");
                                rutusua.setText("");
                                nomusua.setText("");
                                ape1usu.setText("");
                                ape2usu.setText("");

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



    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}