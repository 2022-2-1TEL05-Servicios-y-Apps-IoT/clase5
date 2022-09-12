package com.example.clase5;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContadorViewModel extends ViewModel {

    private MutableLiveData<Integer> contador = new MutableLiveData<>();
    private Thread thread = null;

    public void contar1al10() {
        thread = new Thread(() -> {
            int i = 1;
            while (i < 11) {
                contador.postValue(i);
                Log.d("contador", String.valueOf(i++));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public MutableLiveData<Integer> getContador() {
        return contador;
    }

    public void setContador(MutableLiveData<Integer> contador) {
        this.contador = contador;
    }
}
