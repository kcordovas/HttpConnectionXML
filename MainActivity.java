package com.cordova.httpfeed;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvMostrar;

    private String urlConnection = "http://www.sic.gov.co/que-es-la-api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(this);
        tvMostrar = findViewById(R.id.tv_mostrar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                tvMostrar.setText("");
                new ConnectionAsyncTastk().execute();
                break;
        }
    }

    public class ConnectionAsyncTastk extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String salida = "";
            int i = 0;
            int j = 0;
            try {
                // URL DEL SERVIDOR
                URL url = new URL(urlConnection); // java.net

                // Abre la Conexion del URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Añadimos una cabecera HTTP para que identificarnos y evitar obtener un error de aquellos
                //servidores que prohiben la respuesta a aquellos clientes que no se identifican.
                /*connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");*/

                int respuesta = connection.getResponseCode();

                if (respuesta == HttpURLConnection.HTTP_OK) {
                    BufferedReader lector = new BufferedReader
                            (new InputStreamReader(connection.getInputStream()));
                    String linea = lector.readLine();

                    while (linea != null) {
                        //Si encontramos la etiqueta <title> podemos recuperar el titulo de la noticia
                        if (linea.indexOf("<title>") >= 0) {

                            i = linea.indexOf("<title>") + 16;
                            j = linea.indexOf("</title>") - 3;

                            salida += linea.substring(i, j);
                            salida += "\n-----------------\n";
                        }
                        // Se lee la siguiente linea
                        linea = lector.readLine();
                    }
                    lector.close();
                } else {
                    salida = "No encontrado";
                }

                connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return salida;
        }

        @Override
        protected void onPostExecute(String salida) {
            tvMostrar.append(salida);
            // super.onPostExecute(s);
        }
    }
}

