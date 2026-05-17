package evasion;

import com.google.gson.Gson;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class ApiServer {

    private static ControladorPrediccion controlador;

    public static void main(String[] args) {
        try {
            controlador = new ControladorPrediccion(
                "/datos/modelo_evasion.model",
                "/datos/dataset_pangoa_FINAL.arff"
            );
            System.out.println("Modelo cargado correctamente.");
        } catch (Exception e) {
            System.err.println("Error fatal al cargar el modelo: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Spark.port(port);
        Spark.staticFiles.location("/web");

        Spark.post("/predecir", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            DatosEntrada datos = gson.fromJson(req.body(), DatosEntrada.class);

            if (datos.respondioNotificacion == null || datos.fraccionamiento == null) {
                throw new IllegalArgumentException("Faltan campos booleanos");
            }

            int respondioNum = datos.respondioNotificacion ? 1 : 0;
            int fraccionNum = datos.fraccionamiento ? 1 : 0;

            double[] vector = ControladorPrediccion.construirVector(
                datos.tipoContribuyente,
                datos.montoImpuesto,
                datos.montoPagado,
                datos.deudaAcumulada,
                datos.aniosMoroso,
                datos.frecuenciaPago,
                datos.mesesRetrasoPromedio,
                datos.numNotificaciones,
                respondioNum,
                fraccionNum,
                datos.ultimoPago
            );

            String resultado = controlador.predecir(vector);
            Map<String, String> response = new HashMap<>();
            response.put("prediccion", resultado);
            return gson.toJson(response);
        });

        Spark.exception(Exception.class, (e, req, res) -> {
            res.status(400);
            res.type("application/json");
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            res.body(new Gson().toJson(error));
        });

        System.out.println("Servidor iniciado en puerto " + port);
    }

    static class DatosEntrada {
        int tipoContribuyente;
        double montoImpuesto;
        double montoPagado;
        double deudaAcumulada;
        int aniosMoroso;
        int frecuenciaPago;
        double mesesRetrasoPromedio;
        int numNotificaciones;
        Boolean respondioNotificacion;
        Boolean fraccionamiento;
        int ultimoPago;
    }
}