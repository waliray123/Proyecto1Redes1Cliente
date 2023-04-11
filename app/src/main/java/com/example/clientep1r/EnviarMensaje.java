package com.example.clientep1r;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clientep1r.Classes.Enviar;
import com.example.clientep1r.Classes.Utils;

public class EnviarMensaje extends AppCompatActivity {

    private String macActual;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);
        this.bundle = getIntent().getExtras();

        this.macActual = bundle.getString("macPropia");

    }

    public void GoBack(View view){
        System.out.println("Se va a cerrar");
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void EnvioDeMensaje(View view){
        Utils util = new Utils();
        String ip = util.getIPAddress(true);
        EditText editTextMac = (EditText) findViewById(R.id.editTextMacAEnviar);
        EditText multi = (EditText) findViewById(R.id.editTextTextMultiLine2);

        String mensaje = multi.getText().toString();
        String macRecibo = editTextMac.getText().toString();

        com.mycompany.servidorp1r.Classes.Mensaje mensaje1 = new com.mycompany.servidorp1r.Classes.Mensaje(this.macActual,macRecibo,mensaje,ip);
        Enviar envioMensaje = new Enviar(mensaje1);
        envioMensaje.execute();
    }

    public void AnimarMensaje(View view){
        Intent i = new Intent(this, AnimacionTrama.class);

        EditText editTextMac = (EditText) findViewById(R.id.editTextMacAEnviar);
        EditText multi = (EditText) findViewById(R.id.editTextTextMultiLine2);

        Utils util = new Utils();
        String ip = util.getIPAddress(true);
        String mensaje = multi.getText().toString();
        String macRecibo = editTextMac.getText().toString();

        i.putExtra("macPropia",this.macActual);
        i.putExtra("mensaje",mensaje);
        i.putExtra("macEnvio",macRecibo);
        i.putExtra("ip",ip);
        animarMensajeLauncher.launch(i);
    }

    ActivityResultLauncher<Intent> animarMensajeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                // Traer informacion si es necesaria
                //Realizar el envio del mensaje ya que termino de mostrar la animacion
                System.out.println("Se envia el mensaje luego de animar");
                Bundle bundle = getIntent().getExtras();

                String macPropia = bundle.getString("macPropia");
                String mensaje = bundle.getString("mensaje");
                String macEnvio = bundle.getString("macEnvio");
                String ip = bundle.getString("ip");
                System.out.println("El mensaje es:");
                System.out.println(mensaje);


            }
        }
    });

}