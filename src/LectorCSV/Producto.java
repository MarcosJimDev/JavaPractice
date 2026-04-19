package LectorCSV;

public class Producto {
    private int id;
    private String producto;
    private double precio;
    private int stock;

    public Producto(int id, String producto, double precio, int stock) {
        this.id = id;
        this.producto = producto;
        this.precio = precio;
        this.stock = stock;
    }

    public void mostrarProducto() {
        System.out.println("\tID: " + id);
        System.out.println("\tProducto: " + producto);
        System.out.println("\tPrecio: " + precio);
        System.out.println("\tStock: " + stock);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }

        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        if (producto == null || producto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        this.producto = producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser un número negativo.");
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser un número negativo.");
        }
        this.stock = stock;
    }
}
