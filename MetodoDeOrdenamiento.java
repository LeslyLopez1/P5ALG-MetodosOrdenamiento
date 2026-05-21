public class MetodoDeOrdenamiento {

    //1. qicksort
    public static void quickSort(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        quickSortRecursivo(arreglo, 0, arreglo.length - 1, comparador);
    }

    private static void quickSortRecursivo(VideoJuego[] arreglo, int izq, int der,
                                           Comparator<VideoJuego> comparador) {
        if (izq < der) {
            int ip = particionar(arreglo, izq, der, comparador);
            quickSortRecursivo(arreglo, izq, ip - 1, comparador);
            quickSortRecursivo(arreglo, ip + 1, der, comparador);
        }
    }

    private static int particionar(VideoJuego[] arreglo, int izq, int der, Comparator<VideoJuego> comparador) {
        VideoJuego pivote = arreglo[der];
        int i = izq - 1;
        for (int j = izq; j < der; j++) {
            if (comparador.compare(arreglo[j], pivote) <= 0) { i++; intercambiar(arreglo, i, j); }
        }
        intercambiar(arreglo, i + 1, der);
        return i + 1;
    }

    //2.merge sort
    public static void mergeSort(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        if (arreglo.length < 2) return;
        mergeSortRecursivo(arreglo, 0, arreglo.length - 1, comparador);
    }

    private static void mergeSortRecursivo(VideoJuego[] arreglo, int izq, int der,
                                           Comparator<VideoJuego> comparador) {
        if (izq < der) {
            int centro = (izq + der) / 2;
            mergeSortRecursivo(arreglo, izq, centro, comparador);
            mergeSortRecursivo(arreglo, centro + 1, der, comparador);
            mezclar(arreglo, izq, centro, der, comparador);
        }
    }

    private static void mezclar(VideoJuego[] arreglo, int izq, int centro, int der,
                                Comparator<VideoJuego> comparador) {
        VideoJuego[] L = Arrays.copyOfRange(arreglo, izq, centro + 1);
        VideoJuego[] R = Arrays.copyOfRange(arreglo, centro + 1, der + 1);
        int i = 0, j = 0, k = izq;
        while (i < L.length && j < R.length)
            arreglo[k++] = comparador.compare(L[i], R[j]) <= 0 ? L[i++] : R[j++];
        while (i < L.length) arreglo[k++] = L[i++];
        while (j < R.length) arreglo[k++] = R[j++];
    }

    //3. shell sotr
    public static void shellSort(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        int[] brechas = {1750, 701, 301, 132, 57, 23, 10, 4, 1};
        for (int brecha : brechas) {
            for (int i = brecha; i < arreglo.length; i++) {
                VideoJuego temp = arreglo[i];
                int j = i;
                while (j >= brecha && comparador.compare(arreglo[j - brecha], temp) > 0) {
                    arreglo[j] = arreglo[j - brecha]; j -= brecha;
                }
                arreglo[j] = temp;
            }
        }
    }

    //4. selec direc
    public static void seleccionDirecta(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        for (int i = 0; i < arreglo.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arreglo.length; j++)
                if (comparador.compare(arreglo[j], arreglo[min]) < 0) min = j;
            if (min != i) intercambiar(arreglo, min, i);
        }
    }

    //5. radix
    public static void radixSort(VideoJuego[] arreglo,
                                 java.util.function.ToLongFunction<VideoJuego> extractor) {
        if (arreglo.length == 0) return;
        long[] claves = new long[arreglo.length];
        for (int i = 0; i < arreglo.length; i++) claves[i] = extractor.applyAsLong(arreglo[i]);
        long max = claves[0];
        for (long c : claves) if (c > max) max = c;
        for (long exp = 1; max / exp > 0; exp *= 10) conteoDigito(arreglo, claves, exp);
    }

    private static void conteoDigito(VideoJuego[] arreglo, long[] claves, long exp) {
        int n = arreglo.length;
        VideoJuego[] salida = new VideoJuego[n];
        long[] salidaClaves = new long[n];
        int[] conteo = new int[10];
        for (long c : claves) conteo[(int)((c / exp) % 10)]++;
        for (int i = 1; i < 10; i++) conteo[i] += conteo[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            int d = (int)((claves[i] / exp) % 10);
            salida[--conteo[d]] = arreglo[i];
            salidaClaves[conteo[d]] = claves[i];
        }
        System.arraycopy(salida, 0, arreglo, 0, n);
        System.arraycopy(salidaClaves, 0, claves, 0, n);
    }

    //6. sort()
    public static void ordenarJava(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        Arrays.sort(arreglo, comparador);
    }

    //7. parallelSort()
    public static void ordenarJavaParalelo(VideoJuego[] arreglo, Comparator<VideoJuego> comparador) {
        Arrays.parallelSort(arreglo, comparador);
    }

    private static void intercambiar(VideoJuego[] arreglo, int i, int j) {
        VideoJuego temp = arreglo[i]; arreglo[i] = arreglo[j]; arreglo[j] = temp;
    }
}
