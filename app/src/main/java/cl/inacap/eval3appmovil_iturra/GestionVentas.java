package cl.inacap.eval3appmovil_iturra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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

public class GestionVentas extends AppCompatActivity {

    private TextView tvsesiontickets;
    private ListView tickdatos;
    private EditText rutusu, numbol, txtbolbus;
    private Spinner cbopelicula, cbosala;
    private FloatingActionButton guardado, delete, modificar;
    private Button btnbuscar;
    private Resources recurso;


    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_ventas);


        tvsesiontickets = (TextView) findViewById(R.id.tvsesiontickets);
        rutusu = (EditText) findViewById(R.id.rutusu);
        numbol = (EditText) findViewById(R.id.numbol);
        cbopelicula = (Spinner) findViewById(R.id.cbopelicula);
        cbosala = (Spinner) findViewById(R.id.cbosala);
        guardado = (FloatingActionButton) findViewById(R.id.guardado);
        tickdatos = (ListView) findViewById(R.id.tickdatos);
        txtbolbus= (EditText) findViewById(R.id.txtbolbus);
        btnbuscar=(Button) findViewById(R.id.btnbuscar);
        delete = (FloatingActionButton) findViewById(R.id.delete);
        modificar = (FloatingActionButton) findViewById(R.id.modificar);





        Intent i = getIntent();
        u = (Usuario) i.getParcelableExtra("u");
        tvsesiontickets.setText("Usuario : " + u.getNom().toUpperCase());

        botonRegistrar();
        listarTickets();
        buscarTickets();
        botonEliminar();
        botonModificar();

    }

    private void botonRegistrar() {
        guardado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rutusu.getText().toString().trim().isEmpty()
                        || numbol.getText().toString().trim().isEmpty()
                        || cbopelicula.getSelectedItemPosition() == 0 ||
                        cbosala.getSelectedItemPosition() == 0) {
                    Toast.makeText(GestionVentas.this, "Complete los Todos Los Campos", Toast.LENGTH_LONG).show();


                } else {
                    String rut = rutusu.getText().toString();
                    String bol = numbol.getText().toString();
                    String pel = cbopelicula.getSelectedItem().toString();
                    String sal = cbosala.getSelectedItem().toString();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Tickets.class.getSimpleName());
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            boolean hay = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("bol").getValue().toString().equalsIgnoreCase(bol)){
                                    hay = true;
                                    ocultarTeclado();
                                    Toast.makeText(GestionVentas.this, "Error, La Boleta ("+bol+") Ya Existe",Toast.LENGTH_SHORT).show();
                                    break;

                                }
                            }
                            if( hay == false) {

                                Tickets tick = new Tickets(rut, bol, pel, sal);
                                dbref.push().setValue(tick);
                                ocultarTeclado();
                                Toast.makeText(GestionVentas.this, "Ticket Registrado Exitosamente", Toast.LENGTH_SHORT).show();
                                rutusu.setText("");
                                numbol.setText("");
                                cbopelicula.setSelection(0);
                                cbosala.setSelection(0);
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

    private void listarTickets(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref= db.getReference(Tickets.class.getSimpleName());

        ArrayList<Tickets> listick = new ArrayList<Tickets>();
        ArrayAdapter<Tickets> ada = new ArrayAdapter<Tickets>(GestionVentas.this, android.R.layout.simple_list_item_1,listick);
        tickdatos.setAdapter(ada);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tickets tick = snapshot.getValue(Tickets.class);
                listick.add(tick);
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
        tickdatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Tickets tick = listick.get(i);
                AlertDialog.Builder a = new AlertDialog.Builder(GestionVentas.this);
                a.setCancelable(true);
                a.setTitle("Ticket Seleccionado");
                String msg = "PRECIO $:"+tick.getRut() +"\n\n";
                msg +="BOLETA : " + tick.getBol()+"\n\n";
                msg +="PELICULA : " + tick.getPel()+"\n\n";
                msg +="SALA : " + tick.getSal();


                a.setMessage(msg);
                a.show();
            }
        });
    }//cierra el metodo listartickets

    private void buscarTickets(){
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtbolbus.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(GestionVentas.this, "Digite la Boleta a Buscar", Toast.LENGTH_SHORT).show();
                }else{

                    recurso = getResources();
                    String lista1[] = recurso.getStringArray(R.array.peliculas);
                    String lista2[] = recurso.getStringArray(R.array.salas);
                    int boleta = Integer.parseInt(txtbolbus.getText().toString());

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Tickets.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(boleta);

                            boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("bol").getValue().toString().equalsIgnoreCase(aux)){
                                    res =true;
                                    ocultarTeclado();
                                    rutusu.setText(x.child("rut").getValue().toString());
                                    numbol.setText(x.child("bol").getValue().toString());
                                    for (int i=0; i<lista1.length; i++){
                                        if(lista1[i].equalsIgnoreCase(x.child("pel").getValue().toString())){
                                            cbopelicula.setSelection(i);
                                        }
                                    }
                                    for (int i=0; i<lista2.length; i++){
                                        if(lista2[i].equalsIgnoreCase(x.child("sal").getValue().toString())){
                                            cbosala.setSelection(i);
                                        }
                                    }
                                    //
                                    break;

                                }
                            }
                            if (res == false){
                                ocultarTeclado();;
                                Toast.makeText(GestionVentas.this, "Boleta ("+aux+") No Encontrada", Toast.LENGTH_SHORT).show();
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
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtbolbus.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(GestionVentas.this, "Digite la Boleta a Eliminar", Toast.LENGTH_SHORT).show();
                }else{
                    int boleta = Integer.parseInt(txtbolbus.getText().toString());

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Tickets.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(boleta);
                            final boolean[] res = {false};
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("bol").getValue().toString().equalsIgnoreCase(aux)){
                                    AlertDialog.Builder a =new AlertDialog.Builder(GestionVentas.this);
                                    a.setCancelable(false);
                                    a.setTitle("Pregunta");
                                    a.setMessage("Esta seguro de Eliminar El Ticket N°"+aux+"");
                                    a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    a.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {

                                            res[0] =true;
                                            ocultarTeclado();
                                            Toast.makeText(GestionVentas.this, "Boleta ("+aux+")  Eliminada Exitosamente", Toast.LENGTH_SHORT).show();
                                            x.getRef().removeValue();
                                            listarTickets();

                                        }
                                    });

                                    a.show();
                                    break;

                                }
                            }
                            if (res[0] == false){
                                ocultarTeclado();;
                                Toast.makeText(GestionVentas.this, "Boleta ("+aux+") No Eliminada", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }
    private void botonModificar(){
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rutusu.getText().toString().trim().isEmpty()
                        || numbol.getText().toString().trim().isEmpty()
                        || cbopelicula.getSelectedItemPosition() == 0 ||
                        cbosala.getSelectedItemPosition() == 0) {
                    Toast.makeText(GestionVentas.this, "Complete los Campos Faltantes para actualizar", Toast.LENGTH_LONG).show();


                } else {
                    String rut = rutusu.getText().toString();
                    String bol = numbol.getText().toString();
                    String pel = cbopelicula.getSelectedItem().toString();
                    String sal = cbosala.getSelectedItem().toString();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Tickets.class.getSimpleName());


                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            boolean hay = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(x.child("bol").getValue().toString().equalsIgnoreCase(bol)){
                                    hay = true;
                                    ocultarTeclado();
                                    x.getRef().child("rut").setValue(rut);
                                    x.getRef().child("bol").setValue(bol);
                                    x.getRef().child("pel").setValue(pel);
                                    x.getRef().child("sal").setValue(sal);
                                    Toast.makeText(GestionVentas.this, "Ticket  Modificado Exitosamente", Toast.LENGTH_SHORT).show();

                                    rutusu.setText("");
                                    numbol.setText("");
                                    cbopelicula.setSelection(0);
                                    cbopelicula.setSelection(0);
                                    listarTickets();
                                    break;

                                }
                            }
                            if( hay == false) {

                                ocultarTeclado();
                                Toast.makeText(GestionVentas.this, "Ticket  No Encontrado.\n No se puede Modificar", Toast.LENGTH_SHORT).show();
                                rutusu.setText("");
                                numbol.setText("");
                                cbopelicula.setSelection(0);
                                cbosala.setSelection(0);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }


        });
    }







    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}