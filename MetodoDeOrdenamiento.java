package com.example.practica5ordenamiento;

import java.util.Arrays;
import java.util.Comparator;

public class MetodoDeOrdenamiento {

    //6. sort()
    public static void ordenarJava(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        Arrays.sort(arreglo, comparador);
    }

    //7. parallelSort()
    public static void ordenarJavaParalelo(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        Arrays.parallelSort(arreglo, comparador);
    }

}