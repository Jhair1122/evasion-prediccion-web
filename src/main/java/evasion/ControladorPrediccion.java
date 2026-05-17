package evasion;

import weka.core.*;
import weka.classifiers.Classifier;
import java.io.ObjectInputStream;

public class ControladorPrediccion {
    private Classifier modelo;
    private Instances estructura;

    public ControladorPrediccion(String rutaModelo, String rutaArffReferencia) throws Exception {
        // Cargar modelo desde classpath
        try (ObjectInputStream ois = new ObjectInputStream(
                getClass().getResourceAsStream(rutaModelo))) {
            modelo = (Classifier) ois.readObject();
        }
        
        // Cargar estructura ARFF desde classpath
        try (java.io.InputStream is = getClass().getResourceAsStream(rutaArffReferencia)) {
            if (is == null) throw new Exception("No se encontró: " + rutaArffReferencia);
            Instances temp = new Instances(new java.io.InputStreamReader(is));
            temp.setClassIndex(temp.numAttributes() - 1);
            estructura = new Instances(temp, 0);
        }
    }
    
    public String predecir(double[] valores) throws Exception {
        Instance inst = new DenseInstance(1.0, valores);
        inst.setDataset(estructura);
        double pred = modelo.classifyInstance(inst);
        return Mapeos.CLASES[(int) pred];
    }
    
    public static double[] construirVector(
        int tipoContribuyente,
        double montoImpuesto,
        double montoPagado,
        double deudaAcumulada,
        int aniosMoroso,
        int frecuenciaPago,
        double mesesRetrasoPromedio,
        int numNotificaciones,
        int respondioNotificacion,
        int fraccionamiento,
        int ultimoPago
    ) {
        return new double[]{
            tipoContribuyente, montoImpuesto, montoPagado, deudaAcumulada,
            aniosMoroso, frecuenciaPago, mesesRetrasoPromedio, numNotificaciones,
            respondioNotificacion, fraccionamiento, ultimoPago
        };
    }
}