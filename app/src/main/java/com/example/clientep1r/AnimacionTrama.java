package com.example.clientep1r;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.clientep1r.Classes.Enviar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

public class AnimacionTrama extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animacion_trama);

        //Traer la info
        Bundle bundle = getIntent().getExtras();

        String macPropia = bundle.getString("macPropia");
        String mensaje = bundle.getString("mensaje");
        String macEnvio = bundle.getString("macEnvio");
        String ip = bundle.getString("ip");
        String crc = calcularCRC(mensaje) + "";

        //Insertar info en los textbox

        TextView textviewmacPropia = (TextView) findViewById(R.id.textView9);
        TextView textviewmacEnvio = (TextView) findViewById(R.id.textView8);
        TextView textviewMensaje = (TextView) findViewById(R.id.textView11);
        TextView textviewIp = (TextView) findViewById(R.id.textView14);
        TextView textviewCRC = (TextView) findViewById(R.id.textView23);

        textviewmacPropia.append( "\n" + macPropia);
        textviewmacEnvio.append( "\n" + macEnvio);
        textviewMensaje.append( "\n" + mensaje);
        textviewIp.append( "\n" + ip);
        textviewCRC.append( "\n" + crc);

        textviewmacPropia.setVisibility(View.GONE);
        textviewmacEnvio.setVisibility(View.GONE);
        textviewMensaje.setVisibility(View.GONE);
        textviewIp.setVisibility(View.GONE);
        textviewCRC.setVisibility(View.GONE);

        List<TextView> textViews = new ArrayList<>();
        textViews.add(textviewmacPropia);
        textViews.add(textviewmacEnvio);
        textViews.add(textviewMensaje);
        textViews.add(textviewCRC);
        textViews.add(textviewIp);

        //Realizar la animacion
        animateTextViews(textViews, 0);

        //Terminar y cerrar

        new CountDownTimer(17000, 1000) { // 5000 milisegundos (5 segundos) de duración, 1000 milisegundos (1 segundo) de intervalo entre cada tick
            public void onTick(long millisUntilFinished) {
                System.out.println(millisUntilFinished);
            }

            public void onFinish() {
                System.out.println("Se va a cerrar");
                System.out.println("El mensaje desde animacion es:");
                System.out.println(mensaje);
                Intent intent = new Intent();
                com.mycompany.servidorp1r.Classes.Mensaje mensaje1 = new com.mycompany.servidorp1r.Classes.Mensaje(macPropia,macEnvio,mensaje,ip);
                Enviar envioMensaje = new Enviar(mensaje1);
                envioMensaje.execute();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }.start();

    }

    private long calcularCRC(String msj){
        CRC32 checksum = new CRC32();
        checksum.update(msj.getBytes());

        long val = checksum.getValue();

        return val;
    }

    private void animateTextViews(final List<TextView> textViews, final int index) {
        if (index < textViews.size()) {
            // Obtener el TextView actual
            final TextView textView = textViews.get(index);

            // Crear y configurar la animación
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(2000);
            animation.setStartOffset(1000);

            // Configurar el listener para iniciar la animación del siguiente TextView
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Aplicar la animación en el siguiente TextView
                    animateTextViews(textViews, index + 1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            // Mostrar el TextView actual antes de aplicar la animación
            textView.setVisibility(View.VISIBLE);

            // Iniciar la animación en el TextView actual
            textView.startAnimation(animation);
        }
    }
}