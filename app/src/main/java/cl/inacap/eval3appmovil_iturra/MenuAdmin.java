package cl.inacap.eval3appmovil_iturra;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MenuAdmin extends AppCompatActivity {

    TextView tvsesionadmin;
    ImageButton btnop1,btnop2,btnop3,salir;


    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        tvsesionadmin = (TextView) findViewById(R.id.tvsesionadmin);
        btnop1 = (ImageButton) findViewById(R.id.btnop1);
        btnop2 = (ImageButton) findViewById(R.id.btnop2);
        salir = (ImageButton) findViewById(R.id.salir);

        Intent i = getIntent();
        u = (Usuario)i.getParcelableExtra("u");
        tvsesionadmin.setText("Bienvenido Usuario : "+ u.getNom().toUpperCase());



        btnop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuAdmin.this, GestionVentas.class);
                //Se envía la lista hacia el formulario
                i.putExtra("u", u);
                startActivity(i);

            }
        });
        btnop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MenuAdmin.this, GestionPerfiles.class);
                i.putExtra("u", u);
                startActivity(i);

            }
        });


        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MenuAdmin.this);

                builder.setIcon(R.mipmap.ic_launcher).
                        setTitle("Confirmación de Salida").
                        setMessage("Esta seguro que quiere salir de la sesion?").
                        setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MenuAdmin.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                                Intent e =new Intent(MenuAdmin.this,Principal.class);
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


                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

    }
}