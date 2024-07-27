package cl.inacap.eval3appmovil_iturra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ver_Mis_Tickets extends AppCompatActivity {

    private TextView tvsesionestatickets;
    private TextView sumatickets;
    private TextView promediotickets;
    private TextView totaltickets;
    private ImageButton btnsalir;


    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mis_tickets);


        tvsesionestatickets = (TextView) findViewById(R.id.tvsesionestatickets);
        sumatickets = (TextView) findViewById(R.id.sumatickets);
        promediotickets = (TextView) findViewById(R.id.promediotickets);
        totaltickets = (TextView) findViewById(R.id.totaltickets);
        btnsalir = (ImageButton) findViewById(R.id.btnsalir);


        Intent i = getIntent();
        u = (Usuario) i.getParcelableExtra("u");
        tvsesionestatickets.setText("Usuario : " + u.getNom().toUpperCase());

        btnsalir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(Ver_Mis_Tickets.this);

                builder.setIcon(R.mipmap.ic_launcher).
                        setTitle("Confirmación de Salida").
                        setMessage("Esta seguro que quiere salir de la sesion?").
                        setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(Ver_Mis_Tickets.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                                Intent e = new Intent(Ver_Mis_Tickets.this, Principal.class);
                                startActivity(e);
                                finish();
                            }

                        }).
                        setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });






        CalculoTickets();
        ocultarTeclado();

    }


    private void CalculoTickets() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Tickets.class.getSimpleName());

        ArrayList<Tickets> listick = new ArrayList<Tickets>();
        ArrayAdapter<Tickets> ada = new ArrayAdapter<Tickets>(Ver_Mis_Tickets.this, android.R.layout.simple_list_item_1, listick);


        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int acu = 0;
                int cont = 0;
                float prom = 0F;
                for (DataSnapshot x : snapshot.getChildren()) {
                    int Precios = Integer.parseInt(x.child("rut").getValue().toString());
                    cont++;
                    acu = acu + Precios;

                }
                if (cont > 0) {
                    prom = acu / cont;

                }
                sumatickets.setText("$ " + acu);
                promediotickets.setText("" + cont);
                totaltickets.setText("$ " + prom);
                AlertDialog.Builder a = new AlertDialog.Builder(Ver_Mis_Tickets.this);
                a.setCancelable(true);
                a.setTitle("Datos Estadisticos CineTickets");
                a.setMessage("SUMA DE PRECIOS TICKETS : $ " + acu + "\nCANTIDAD TICKETS VENDIDOS : " + cont + "\nVALOR PROMEDIO X TICKET: $ " + prom);
                a.show();
                return;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }}


}