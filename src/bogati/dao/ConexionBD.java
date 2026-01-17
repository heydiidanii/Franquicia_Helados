package bogati.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    private static Connection conexion = null;
    
    public static Connection obtenerConexion() {
        if (conexion == null) {
            try {
                Properties props = new Properties();
                InputStream input = ConexionBD.class.getClassLoader()
                        .getResourceAsStream("config/database.properties");
                
                if (input == null) {
                    throw new RuntimeException("No se encontró database.properties");
                }
                
                props.load(input);
                
                String url = String.format("jdbc:mariadb://%s:%s/%s",
                        props.getProperty("db.host"),
                        props.getProperty("db.port"),
                        props.getProperty("db.name"));
                
                conexion = DriverManager.getConnection(url,
                        props.getProperty("db.user"),
                        props.getProperty("db.password"));
                
                System.out.println("✅ Conexión exitosa a la base de datos");
                
            } catch (IOException | SQLException e) {
                System.err.println("❌ Error de conexión: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return conexion;
    }
    
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("🔌 Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}