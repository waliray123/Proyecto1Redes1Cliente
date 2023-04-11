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

public class DesempaquetandoAnimacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desempaquetando_animacion);

        //Traer la info
        Bundle bundle = getIntent().getExtras();



        String macEnvio = bundle.getString("macEnvio");
        String mensaje = bundle.getString("mensaje");
        String macRecibo = bundle.getString("macRecibo");
        String ip = bundle.getString("ip");
        String crc = bundle.getString("crc");

        //Insertar info en los textbox

        TextView textviewMacEnvio = (TextView) findViewById(R.id.textView20);
        TextView textviewMacRecibo = (TextView) findViewById(R.id.textView21);
        TextView textviewMensaje = (TextView) findViewById(R.id.textView17);
        TextView textviewIp = (TextView) findViewById(R.id.textView18);
        TextView textviewCRC = (TextView) findViewById(R.id.textView22);

        textviewMacEnvio.append( "\n" + macEnvio);
        textviewMacRecibo.append( "\n" + macRecibo);
        textviewMensaje.append( "\n" + mensaje);
        textviewIp.append( "\n" + ip);
        textviewCRC.append( "\n" + crc);

        textviewMacEnvio.setVisibility(View.GONE);
        textviewMacRecibo.setVisibility(View.GONE);
        textviewMensaje.setVisibility(View.GONE);
        textviewIp.setVisibility(View.GONE);
        textviewCRC.setVisibility(View.GONE);

        List<TextView> textViews = new ArrayList<>();
        textViews.add(textviewMacEnvio);
        textViews.add(textviewMacRecibo);
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
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }.start();
        System.out.println("ESto deberia de estar despues de Se va a cerrar aaa");
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