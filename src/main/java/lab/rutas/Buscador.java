package lab.rutas;

import java.io.File;

public class Buscador {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Debes proporcionar el nombre del archivo de entrada como argumento.");
            return;
        }
        String rutaRelativa = args[0];
        File file = new File(rutaRelativa);

        System.out.println("-------- ESTADO DEL SISTEMA --------");
        System.out.println("Archivo de entrada: " + rutaRelativa);
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Ruta buscada: " + file.getPath());
        System.out.println("Ruta absoluta: " + file.getAbsolutePath());

        if(file.exists()) {
            System.out.println("RESULTADO: Archivo encontrado!");
        } else {
            System.out.println("RESULTADO: Archivo NO encontrado.");
        }
    }
}