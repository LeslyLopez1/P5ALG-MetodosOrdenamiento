package com.example.practica5ordenamiento;

public class LectorArchivo {

    private String rutaCSV;
    private ArrayList<VideoJuego> juegos;

    public LectorArchivo() {
        rutaCSV = "games.csv";
        juegos = new ArrayList<>();
        leer();
    }

    public ArrayList<VideoJuego> getJuegos() {
        return juegos;
    }

    private void leer() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(LectorArchivo.class.getClassLoader().getResourceAsStream(rutaCSV)),
                StandardCharsets.UTF_8))) {

            //lectura de las lineas
            ArrayList<String> lineas = new ArrayList<>();
            for (String linea = br.readLine(); linea != null; linea = br.readLine()) {
                lineas.add(linea);
            }

            //recorrer las lineas para procesar
            for (int i = 1; i < lineas.size(); i++) {

                //manejo de comillas, si es impar es ifual a que este abierto
                //mienrtas no este completo avanza al sig renglon de la lista
                StringBuilder registro = new StringBuilder(lineas.get(i));
                while (!estaCompleto(registro.toString()) && i + 1 < lineas.size()) {
                    i++;
                    registro.append('\n').append(lineas.get(i));
                }

                String[] campos = separar(registro.toString());
                if (campos.length < 14) continue;

                try {
                    juegos.add(new VideoJuego(
                            campos[0],
                            campos[1],
                            campos[2],
                            campos[3],
                            Double.parseDouble(campos[4]),
                            campos[5],
                            campos[6],
                            campos[8],
                            campos[9],
                            campos[10],
                            campos[11],
                            campos[12],
                            campos[13]
                    ));
                } catch (NumberFormatException ignored) {}
            }

        } catch (IOException e) {
            throw new RuntimeException("error!!!" + e.getMessage());
        }
    }
    //converti texto a un array de char para contar comillas
    //regress ture si es par
    private boolean estaCompleto(String linea) {
        int cuenta = 0;
        for (char c : linea.toCharArray())
            if (c == '"') cuenta++;
        return cuenta % 2 == 0;
    }

    //separa columnas con ,
    private String[] separar(String linea) {
        ArrayList<String> campos = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean enComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '"') {
                if (enComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    sb.append('"'); i++;
                } else {
                    enComillas = !enComillas;
                }
            } else if (c == ',' && !enComillas) {
                campos.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        campos.add(sb.toString());
        return campos.toArray(new String[0]);
    }
}