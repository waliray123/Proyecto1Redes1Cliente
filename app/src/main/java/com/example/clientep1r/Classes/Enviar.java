package com.example.clientep1r.Classes;

import android.os.AsyncTask;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.zip.CRC32;

public class Enviar extends AsyncTask<String,Void,Void> {

    private com.mycompany.servidorp1r.Classes.Mensaje mensaje;
    public Enviar(com.mycompany.servidorp1r.Classes.Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    private long calcularCRC(String msj){
        CRC32 checksum = new CRC32();
        checksum.update(msj.getBytes());

        long val = checksum.getValue();

        return val;
    }

    @Override
    protected Void doInBackground(String... strings) {

        try {
            this.mensaje.setCRC32(calcularCRC(mensaje.getMensaje()));

            Socket s = new Socket("10.10.20.1",9999);
            ObjectOutputStream stream = new ObjectOutputStream(s.getOutputStream());
            stream.writeObject(this.mensaje);
            stream.flush();
            stream.close();
            s.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
