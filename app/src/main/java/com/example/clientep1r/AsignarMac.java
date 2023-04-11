package com.example.clientep1r;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AsignarMac extends AppCompatActivity {

    private String macActual;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_mac);
        this.bundle = getIntent().getExtras();

        this.macActual = bundle.getString("macPropia");

        TextView textviewmac = (TextView) findViewById(R.id.textView13);
        textviewmac.setText(this.macActual);
    }


    public void AsignarMacNueva(View view){
        EditText editText = (EditText) findViewById(R.id.editTextMacAddress2);

        this.macActual = editText.getText().toString();

        TextView textviewmac = (TextView) findViewById(R.id.textView13);
        textviewmac.setText(this.macActual);
    }

    public void GoBack(View view){
        System.out.println("Se va a cerrar");
        Intent intent = new Intent();
        intent.putExtra("macPropia", this.macActual);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public String getMacActual() {
        return macActual;
    }


}