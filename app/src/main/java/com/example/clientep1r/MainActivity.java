package com.example.clientep1r;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clientep1r.Classes.Enviar;
import com.example.clientep1r.Classes.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Runnable{


    private String estadoCliente;
    private String macPropia;
    private int numactividad;

    private String estadoAnimacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.estadoCliente = "Inactivo";
        this.macPropia = "";
        this.numactividad = 0;
        this.estadoAnimacion = "Animando";

        asignarAnimarLabel();
        asignarMacLabel();
        limpiarBandejaEntrada();
        Thread hilo1 = new Thread(this);
        hilo1.start();
    }

    public void asignarMacLabel(){
        TextView textviewmac = (TextView) findViewById(R.id.textViewMac);
        textviewmac.setText(this.macPropia);
    }

    public void asignarAnimarLabel(){
        TextView textviewAnimacion = (TextView) findViewById(R.id.textView16);
        textviewAnimacion.setText(this.estadoAnimacion);
    }

    public void limpiarBandejaEntrada(){
        EditText bandejaEntrada = (EditText) findViewById(R.id.editTextTextMultiLine);
        bandejaEntrada.setText("");
        bandejaEntrada.setEnabled(false);
    }

    public void GoMac(View view){
        Intent i = new Intent(this, AsignarMac.class);
        i.putExtra("macPropia",this.macPropia);
        macLauncher.launch(i);
    }

    public void GoEnviar(View view){
        Intent i = new Intent(this, EnviarMensaje.class);
        i.putExtra("macPropia",this.macPropia);
        enviarLauncher.launch(i);
    }

    public void Conectar(View view){
        if(this.estadoAnimacion.equals("Animando")){
            animarMensaje("ConectarIPMAC","");
        }else{
            EnviarMensaje(this.macPropia,"","ConectarIPMAC");
            TextView estado = (TextView) findViewById(R.id.textViewEstado);
            this.estadoCliente = "Activo";
            estado.setText(estadoCliente);
        }
    }

    public void Desconectar(View view){
        if(this.estadoAnimacion.equals("Animando")){
            animarMensaje("DesconectarIPMAC","");
        }else {
            EnviarMensaje(this.macPropia, "", "DesconectarIPMAC");
            TextView estado = (TextView) findViewById(R.id.textViewEstado);
            this.estadoCliente = "Inactivo";
            estado.setText(this.estadoCliente);
        }
    }

    public void CambioAnimacion(View view){
        if (this.estadoAnimacion.equals("Animando")){
            this.estadoAnimacion = "No animando";
        }else{
            this.estadoAnimacion = "Animando";
        }
        asignarAnimarLabel();
    }

    private void EnviarMensaje(String macEnvio, String macRecibo, String mensaje){
        Utils util = new Utils();
        String ip = util.getIPAddress(true);

        com.mycompany.servidorp1r.Classes.Mensaje mensaje1 = new com.mycompany.servidorp1r.Classes.Mensaje(macEnvio,macRecibo,mensaje,ip);
        Enviar env = new Enviar(mensaje1);
        env.execute();
    }

    public void animarMensaje(String mensaje, String macEnvio){
        Intent i = new Intent(this, AnimacionTrama.class);

        EditText editTextMac = (EditText) findViewById(R.id.editTextMacAEnviar);
        EditText multi = (EditText) findViewById(R.id.editTextTextMultiLine2);

        Utils util = new Utils();
        String ip = util.getIPAddress(true);

        i.putExtra("macPropia",this.macPropia);
        i.putExtra("mensaje",mensaje);
        i.putExtra("macEnvio",macEnvio);
        i.putExtra("ip",ip);
        animarMensajeLauncher.launch(i);
    }

    private boolean animarReciboPaquete(com.mycompany.servidorp1r.Classes.Mensaje msj){
        boolean termino = false;
        Intent i = new Intent(this, DesempaquetandoAnimacion.class);
        i.putExtra("macEnvio",msj.getMacAddressEnvio());
        i.putExtra("macRecibo",msj.getMacAddressRecibo());
        i.putExtra("mensaje",msj.getMensaje());
        i.putExtra("crc",msj.getCRC32() + "");
        i.putExtra("ip",msj.getIp());
        animarDesempaquetadoLauncher.launch(i);
        return termino;
    }

    ActivityResultLauncher<Intent> animarMensajeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){

            }
        }
    });

    ActivityResultLauncher<Intent> macLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                String macNueva = extras.getString("macPropia");
                asignarMacNuevaAPropia(macNueva);
                asignarMacLabel();
            }
        }
    });

    ActivityResultLauncher<Intent> enviarLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Bundle extras = result.getData().getExtras();
                // Realizar la respuesta de enviar

            }
        }
    });

    ActivityResultLauncher<Intent> animarDesempaquetadoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){

            }
        }
    });

    public void asignarMacNuevaAPropia(String macNueva){
        this.macPropia = macNueva;
    }


    @Override
    public void run() {
        System.out.println("Hola estoy a la escucha desde el cliente");

        try {
            ServerSocket server = new ServerSocket(9090);
            com.mycompany.servidorp1r.Classes.Mensaje paqueteRecibido;


            while(true){
                Socket socket = server.accept();

                ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());

                paqueteRecibido = (com.mycompany.servidorp1r.Classes.Mensaje)stream.readObject();

                //String mensaje = "\n"+paqueteRecibido.getMacAddressEnvio() +": " + paqueteRecibido.getMensaje();
                String mensaje = "";

                if(paqueteRecibido.getMacAddressEnvio().equals("FFFF:FFFF:FFFF:FFFF") && paqueteRecibido.getMensaje().equals("ESTUIPMAC")){
                    //Recibio un mensaje para confirmar la ip y la mac del dispositivo
                    //TODO: Mostrar la animacion del recibo del mensaje
                    //boolean terminoAnimar = animarReciboPaquete(paqueteRecibido);
                    //System.out.println(terminoAnimar);


                    if (paqueteRecibido.getMacAddressRecibo().equals(this.macPropia)){

                        //Si se tiene que animar entonces tiene animar el mensaje
                        if(this.estadoAnimacion.equals("Animando")){
                            animarMensaje("SIESMIIPMAC","FFFF:FFFF:FFFF:FFFF");
                        }else{
                            //Si es la mac del paquete recibido
                            //Reenviar un paquete de confirmacion

                            Utils util = new Utils();
                            String ip = util.getIPAddress(true);

                            String mensaje2 = "SIESMIIPMAC";
                            String macRecibo = "FFFF:FFFF:FFFF:FFFF";

                            com.mycompany.servidorp1r.Classes.Mensaje mensaje1 = new com.mycompany.servidorp1r.Classes.Mensaje(this.macPropia,macRecibo,mensaje2,ip);
                            Enviar envioMensaje = new Enviar(mensaje1);
                            envioMensaje.execute();
                        }


                    }else{
                        //NO es su mac por lo que no regresa nada al servidor switch

                    }
                }else{
                    if(this.estadoAnimacion.equals("Animando")){
                        //TODO: Mostrar la animacion del recibo del mensaje
                        boolean terminoAnimar = animarReciboPaquete(paqueteRecibido);
                        System.out.println(terminoAnimar);
                    }
                    mensaje = "\n"+paqueteRecibido.getMacAddressEnvio() +": " + paqueteRecibido.getMensaje();
                }
                String finalMensaje = mensaje;

                runOnUiThread(new Runnable() {
                    public void run() {
                        EditText multi = (EditText) findViewById(R.id.editTextTextMultiLine);
                        multi.append(finalMensaje);
                    }
                });

                socket.close();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }
}
