package LectorCSV;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class EjemploLecturaCSV {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cont = 0, contProductos = 0, opcion = 0;

        //Creamos el ArrayList de objeto Producto.
        ArrayList<Producto> productos = new ArrayList<>();
        List<String> contenidoFichero = new ArrayList<>();



        boolean archivoExistente = false, segundaVuelta = false;
        String ruta;

        System.out.println("Hola, introduce la ruta absoluta del fichero: ");

        do {
            if (segundaVuelta)
                System.out.println("Introduce la ruta absoluta del fichero:");

            ruta = sc.nextLine();
            Path rutaFichero = Paths.get(ruta);
            try {
                if (!Files.exists(rutaFichero)) {
                    System.out.println("El fichero no existe.");
                } else if (!Files.isRegularFile(rutaFichero)) {
                    System.out.println("ERROR: el archivo no se puede leer.");
                } else {
                    contenidoFichero = Files.readAllLines(rutaFichero);
                    archivoExistente = true;
                }


                if (!contenidoFichero.isEmpty()) {
                    for (String linea : contenidoFichero) {
                        if (cont > 0) {
                            try {
                                Producto nuevo = convertirLineaAProducto(linea);
                                if (nuevo != null)
                                    productos.add(nuevo);
                                contProductos++;
                            } catch (Exception e) {
                                System.err.println("ERROR: línea " + cont + " no procesada. Asegúrate de que el forma se cumpla.");
                            }
                        }
                        cont++;
                    }

                    do {
                        mostrarMenu();
                        try {
                            opcion = sc.nextInt();
                            sc.nextLine();
                            switch (opcion) {
                                case 1:
                                    mostrarProductos(productos);
                                    break;
                                case 2:
                                    escribirFichero(productos, contProductos, sc);
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Opción no válida.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("ERROR: la opción debe ser un número.");
                            sc.nextLine();
                        }

                    } while (opcion != 3);

                } else {
                    System.out.println("ERROR: el fichero no puede estar vacío.");
                }

            } catch (IOException e) {
                System.out.println("Error al leer el fichero.");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: asegúrate de que los números no contengan letras.");
            }

            segundaVuelta = true;
        } while (!archivoExistente);

        System.out.println("*** Adiós, amigo ***");
        sc.close();
    }

    public static void mostrarMenu() {
        System.out.println("\n*** ELIGE UNA OPCIÓN ***");
        System.out.println("\t1. Ver productos disponibles.");
        System.out.println("\t2. Crear archivo txt con listado de productos.");
        System.out.println("\t3. Salir.");
    }

    public static void mostrarProductos(ArrayList<Producto> productos) {
        System.out.println("*** INFO DE LOS PRODUCTOS ***");
        for (Producto producto: productos) {
            producto.mostrarProducto();
            System.out.println();
        }
    }

    public static void escribirFichero(ArrayList<Producto> productos, int contProductos, Scanner sc) {
        Path directorioNuevo = Paths.get("informes");
        Path rutaFichero = Paths.get("informes/infoProductos.txt");

        try {
            String opcion = "";
            boolean archivoVacio = false;

            if (!Files.exists(directorioNuevo)) {
                System.out.println("La carpeta no existe. Se va a crear.");
                Files.createDirectory(directorioNuevo);
                Files.createFile(rutaFichero);
                System.out.println("¡Archivo creado!");
                archivoVacio = true;
            } else if (Files.exists(directorioNuevo) && !Files.exists(rutaFichero)) {
                System.out.println("Parece ser que el fichero no existe. Se va a crear.");
                Files.createFile(rutaFichero);
                System.out.println("¡Archivo creado!");
            } else {
                System.out.println("Al parecer, el archivo ya existe. Se va a sobreescribir. ¿Estás seguro de que quieres continuar? y/n");
                do {
                    opcion = sc.nextLine();

                    if (!opcion.equalsIgnoreCase("y") && !opcion.equalsIgnoreCase("n"))
                        System.out.println("ERROR: respuesta no registrada en el sistema. Vuelve a introducir una respuesta (y/n): ");
                } while (!opcion.equalsIgnoreCase("y") && !opcion.equalsIgnoreCase("n"));
            }

            if (opcion.equalsIgnoreCase("y") || archivoVacio) {
                double precioTotal = 0;
                Files.writeString(rutaFichero, "*** LISTADO DE PRODUCTOS ***");
                for (Producto producto : productos) {
                    Files.writeString(rutaFichero, "\nProducto con ID " + producto.getId() + ", se llama " + producto.getProducto() + ", cuesta " + producto.getPrecio() + " y tenemos " + producto.getStock() + " en stock.", StandardOpenOption.APPEND);
                    precioTotal += producto.getPrecio();
                }
                Files.writeString(rutaFichero, "\nProductos en total: " + contProductos, StandardOpenOption.APPEND);

                double media;
                if (!productos.isEmpty()) {
                    media = precioTotal / productos.size();
                    Files.writeString(rutaFichero, "\nPrecio medio:  " + media + "€", StandardOpenOption.APPEND);
                } else {
                    Files.writeString(rutaFichero, "\nNo se ha podido calcular la media.", StandardOpenOption.APPEND);
                }


                System.out.println("¡Archivo escrito con éxito!");
            } else {
                System.out.println("El archivo no se ha sobreescrito.");
            }

        } catch (IOException e) {
            System.out.println("ERROR: no se ha podido escribir el archivo.");
        }
    }

    private static Producto convertirLineaAProducto(String linea) {
        String[] lineaSeparada = linea.split(",");
        if (lineaSeparada != null && lineaSeparada.length == 4) {
            int id = Integer.parseInt(lineaSeparada[0].trim());
            String producto = lineaSeparada[1].trim();
            double precio = Double.parseDouble(lineaSeparada[2].trim());
            int stock = Integer.parseInt(lineaSeparada[3].trim());
            return new Producto(id, producto, precio, stock);
        } else {
            return null;
        }
    }
}
