package com.example.clase5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        ContadorViewModel contadorViewModel =
                new ViewModelProvider(this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this,
                contador -> textView.setText(String.valueOf(contador)));
        contadorViewModel.getContador().observe(this,
                contador -> textView.setText(String.valueOf(contador)));
        contadorViewModel.getContador().observe(this,
                contador -> textView.setText(String.valueOf(contador)));

    }

    public boolean verificarEstadoInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        boolean wifi = false;
        boolean celular = false;

        Network[] allNetworks = connectivityManager.getAllNetworks();
        for (Network network : allNetworks) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                wifi = true;
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                celular = true;
        }
        Log.d("internet", "wifi: " + wifi);
        Log.d("internet", "4G: " + celular);

        return isConnected;
    }

    public void verificarInternetBtn(View view) {
        Log.d("internet", String.valueOf(verificarEstadoInternet()));
    }


    public void iniciarContador(View view) {
        ContadorViewModel contadorViewModel =
                new ViewModelProvider(this).get(ContadorViewModel.class);
        contadorViewModel.contar1al10();
    }

    public void DescargarWs1(View view) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://my-json-server.typicode.com/typicode/demo/profile";

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    Log.d("data", response);
                    Gson gson = new Gson();
                    Profile profile = gson.fromJson(response, Profile.class);
                    Log.d("data", String.valueOf(profile.getLastName()));
                    TextView textView = findViewById(R.id.textViewTypeCode);
                    textView.setText(profile.getName());
                },
                error -> Log.e("data", error.getMessage()));
        requestQueue.add(stringRequest);
    }

    public void obtenerWs2(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://my-json-server.typicode.com/typicode/demo/comments",
                response -> {
                    Log.d("data", response);
                    Gson gson = new Gson();
                    Comment[] comments = gson.fromJson(response, Comment[].class);
                    List<Comment> commentList = Arrays.asList(comments);
                    for (Comment c : commentList) {
                        Log.d("comment id", String.valueOf(c.getId()));
                        Log.d("comment body", c.getBody());
                    }
                },
                error -> Log.e("data", error.getMessage()));
        requestQueue.add(stringRequest);
    }

    public void descargarConPost(View view) {
        EditText editText = findViewById(R.id.editTextUsuarioBuscar);
        String usuarioBuscar = editText.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://1a8jit3xd4.execute-api.us-east-1.amazonaws.com/prod?accion=validar",
                response -> {
                    Log.d("data", response);
                    if (response.equals("\"existe\"")) {
                        Toast.makeText(this, "Usuario existe", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.e("data", error.getMessage())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", usuarioBuscar);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}