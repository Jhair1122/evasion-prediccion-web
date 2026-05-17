package evasion;

import java.util.HashMap;
import java.util.Map;

public class Mapeos {
    public static final Map<String, Integer> TIPO_MAP = new HashMap<>();
    public static final Map<String, Integer> FRECUENCIA_MAP = new HashMap<>();
    public static final Map<String, Integer> RESPONDIO_MAP = new HashMap<>();
    public static final Map<String, Integer> FRACCION_MAP = new HashMap<>();
    
    static {
        TIPO_MAP.put("Persona natural", 0);
        TIPO_MAP.put("Persona jurídica", 1);
        
        FRECUENCIA_MAP.put("Puntual", 0);
        FRECUENCIA_MAP.put("Con retraso", 1);
        FRECUENCIA_MAP.put("Nunca paga", 2);
        
        RESPONDIO_MAP.put("Sí", 1);
        RESPONDIO_MAP.put("No", 0);
        
        FRACCION_MAP.put("Sí", 1);
        FRACCION_MAP.put("No", 0);
    }
    
    public static final String[] CLASES = {"Bajo", "Medio", "Alto"};
}