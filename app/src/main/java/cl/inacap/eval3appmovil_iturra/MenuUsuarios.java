package cl.inacap.eval3appmovil_iturra;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuUsuarios extends AppCompatActivity {

    TextView tvsesionusu;
    private ImageButton btnop5,btnop6;
    private ImageView SALIR3;
    private Usuario u;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuarios);


            tvsesionusu = (TextView) findViewById(R.id.tvsesionusu);
            btnop5 = (ImageButton) findViewById(R.id.btnop5);
            btnop6 = (ImageButton) findViewById(R.id.btnop6);
            SALIR3 = (ImageView) findViewById(R.id.SALIR3);

            Intent i = getIntent();
            u = (Usuario) i.getParcelableExtra("u");
            tvsesionusu.setText("Bienvenido Usuario : "+u.getNom().toUpperCase());

            btnop5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(MenuUsuarios.this, Ver_Mis_Tickets.class);
                    //Se envía la lista hacia el formulario
                    i.putExtra("u", u);
                    startActivity(i);

                }
            });
            btnop6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(MenuUsuarios.this, DatosAdmin.class);
                    i.putExtra("u", u);
                    startActivity(i);

                }
            });
            SALIR3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MenuUsuarios.this);

                    builder.setIcon(R.mipmap.ic_launcher).
                            setTitle("Confirmación de Salida").
                            setMessage("Esta seguro que quiere salir de la sesion?").
                            setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MenuUsuarios.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                                    Intent e =new Intent(MenuUsuarios.this,Principal.class);
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