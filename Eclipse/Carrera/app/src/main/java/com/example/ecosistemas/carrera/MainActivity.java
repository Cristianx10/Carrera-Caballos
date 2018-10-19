package com.example.ecosistemas.carrera;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnClickListener, ClienteUnicast.GetData{

    Button btn_start;
    Button btn_stop;
    TextView tv_player_1;
    TextView tv_player_2;
    TextView tv_player_3;

    ClienteUnicast conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        tv_player_1 = findViewById(R.id.tv_player_1);
        tv_player_2 = findViewById(R.id.tv_player_2);
        tv_player_3 = findViewById(R.id.tv_player_3);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);


        conexion= new ClienteUnicast( this);
        conexion.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:

                conexion.enviar("comenzar");

                break;

            case R.id.btn_stop:

                conexion.enviar("parar");

                break;
        }
    }



    @Override
    public void getRecibido(String mensaje) {

        this.mensaje = mensaje;
       // tv_player_1.setText(mensaje);
        System.out.println("El dato contiene");
        System.out.println("Recibi un mensaje de: " + mensaje + "..................................................");

        new posiciones().execute();
    }

    public String mensaje;

    public class posiciones extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(mensaje.contains("Puesto")){
                String[] separar = mensaje.split(":");
                if(separar[0].equals("Puesto-1-")){
                    tv_player_1.setText("Gano: " + separar[1]);
                }
                if(separar[0].equals("Puesto-2-")){
                    tv_player_2.setText("Gano: " + separar[1]);
                }
                if(separar[0].equals("Puesto-3-")){
                    tv_player_3.setText("Gano: " + separar[1]);
                }
            }
        }
    }
}
