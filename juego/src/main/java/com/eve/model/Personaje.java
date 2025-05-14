package com.eve.model;

public abstract class Personaje implements Comparable {
    protected int puntosvida;
    protected int porcentajeCritico;
    protected int fuerza;
    protected int defensa;
    protected int velocidad;
    protected String nombre;
    protected String imagen;
    protected int[] posicion;
    protected int id;

    public Personaje() {
        this.puntosvida = 0;
        this.porcentajeCritico = 0;
        this.fuerza = 0;
        this.defensa = 0;
        this.velocidad = 0;
        this.nombre = "eve";
        this.posicion = new int[2];
        this.id = 0;
    }

    /**
     * Constructor parametrizado de los personajes
     * 
     * @param imagen            del personaje, enemigo o prota
     * @param nombre            del personaje, enemigo o prota
     * @param puntosvida        del personaje, enemigo o prota
     * @param porcentajeCritico del personaje, enemigo o prota
     * @param fuerza            del personaje, enemigo o prota
     * @param defensa           del personaje, enemigo o prota
     * @param velocidad         del personaje, enemigo o prota
     * @param id                del personaje, enemigo o prota
     */
    public Personaje(String imagen, String nombre, int puntosvida, int porcentajeCritico, int fuerza, int defensa,
            int velocidad, int id) {
        this.puntosvida = puntosvida;
        this.porcentajeCritico = porcentajeCritico;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.nombre = nombre;
        this.imagen = imagen;
        this.posicion = new int[2];
        this.id = id;
    }

    /**
     * Puntos de vida de los personajes
     * 
     * @return puntosVida
     */

    public int getPuntosvida() {
        return this.puntosvida;
    }

    /**
     * Método para establecer los puntos de vida del personaje
     * 
     * @param puntosvida del personaje.
     */
    public void setPuntosvida(int puntosvida) {
        this.puntosvida = puntosvida;
    }

    /**
     * Porcentaje Critico de los personajes
     * 
     * @return porcentajeCritico
     */

    public int getPorcentajeCritico() {
        return this.porcentajeCritico;
    }

    /**
     * Método para establecer el porcentaje crítico de los personajes, según este
     * porcentaje, harán más o menos daño si el resultado obtenido a partir de un
     * random cumple con la probabilidad establecida en el porcentaje critico de que
     * su fuerza aumente.
     * 
     * @param danio del personaje, establece el porcentaje de probabilidad de hacer
     *              más daño.
     */
    public void setPorcentajeCritico(int danio) {
        this.porcentajeCritico = danio;
    }

    /**
     * Fuerza de los personajes
     * 
     * @return fuerza
     */
    public int getFuerza() {
        return this.fuerza;
    }

    /**
     * Método para establecer la fuerza de los personajes
     * 
     * @param fuerza del personjae para hacer daño.
     */
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    /**
     * Defensa de los personajes, la defensa del prota
     * aumenta cuando matan a los enemigos
     * 
     * @return defensa
     */
    public int getDefensa() {
        return this.defensa;
    }

    /**
     * Método para establecer la defensa de los personajes
     * 
     * @param defensa del personaje para protegerse del daño.
     */
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    /**
     * Velocidad de los personajes, según esta, los personajes se ordenan y se
     * establecen los turnos de movimiento.
     * 
     * @return velocidad
     */
    public int getVelocidad() {
        return this.velocidad;
    }

    /**
     * Método para establecer la velocidad de los personajes
     * 
     * @param velocidad del personaje, para establecer los turnos.
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Nombre de los personajes
     * 
     * @return nombre
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método para darle nombre a los personajes
     * 
     * @param nombre del personaje.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Imagen de los personajes
     * 
     * @return imagen
     */

    public String getImagen() {
        return this.imagen;
    }

    /**
     * Método para darle una imagen a los personajes
     * 
     * @param imagen del personaje.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * 
     * Posicion de los personajes
     * 
     * @return posicion
     */
    public int[] getPosicion() {
        return this.posicion;
    }

    /**
     * Método para darle posicion a los personajes
     * 
     * @param posicion del personaje, en el eje X e Y (fila, columna).
     */
    public void setPosicion(int[] posicion) {
        this.posicion = posicion;
    }

    /**
     * Id unico de los personajes
     * 
     * @return id
     */

    public int getId() {
        return this.id;
    }

    /**
     * Método para darle un id a los personajes
     * 
     * @param id del personaje.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método para mover a los personajes. Reciben la nueva posición (fila, columna)
     * y el escenario, para moverlos a esa nueva posición en el escenario
     * 
     * @param nuevaFila número de fila a la que se debe mover el personaje.
     * @param nuevaCol  número columna a la que se debe mover el personaje.
     * @param escenario del juego.
     */
    public void moverPersonaje(int nuevaFila, int nuevaCol, String[][] escenario) {
        GestorJuego gestor = Proveedor.getInstance().getGestorJuego();
        int[] pos = this.getPosicion();
        escenario[pos[0]][pos[1]] = "s";
        this.setPosicion(new int[] { nuevaFila, nuevaCol });
        escenario[nuevaFila][nuevaCol] = "" + this.id;
        gestor.setEvento("");
        gestor.notifyObservers();
    }

    /**
     * Método para atacar a los personajes. Reciben la nueva posición (fila,
     * columna)
     * y el escenario, para atacar al personaje de esa nueva posición en el
     * escenario
     * 
     * @param nuevaFila número de fila con un personaje a la que debe atacar otro.
     * @param nuevaCol  número de columna con un personaje a la que debe atacar
     *                  otro.
     * @param escenario del juego.
     */
    public void atacarPersonaje(int nuevaFila, int nuevaCol, String[][] escenario) {
    }

    /**
     * Método para comprobar si la acción (mover o atacar) se puede llevar a cabo.
     * En el caso de los enemigos, si pueden moverse o atacar, ya sea a partir de la
     * percecpión (se mueve hacia el prota) o de forma aleatoria.
     * En el caso del prota, si en base a la tecla presionada se pueden mover o no,
     * así como atacar o no (según si hay un enemigo en la "nueva posición").
     * 
     * @param posiciones en las que esta el protagonista
     * @param movimiento dirección a la que moverse
     * @return la acción a realizar.
     *         Este método es reutilizable en ambos personajes (enemigos y prota).
     */
    public String comprobarAccion(int[] posiciones, String movimiento) {
        GestorJuego gestorJuego = Proveedor.getInstance().getGestorJuego();
        String[][] escenario = gestorJuego.getEscenario().getEscenario();
        String accion = "";
        switch (movimiento) {
            case "W":
                if (posiciones[0] - 1 >= 0) {
                    if (escenario[posiciones[0] - 1][posiciones[1]].equals("s")
                            || escenario[posiciones[0] - 1][posiciones[1]].equals("t"))
                        return "mover";
                    else if (!escenario[posiciones[0] - 1][posiciones[1]].equals("p"))
                        return "atacar";
                    else
                        return "nada";
                }
                break;
            case "A":
                if (posiciones[1] - 1 >= 0) {
                    if (escenario[posiciones[0]][posiciones[1] - 1].equals("s")
                            || escenario[posiciones[0]][posiciones[1] - 1].equals("t"))
                        return "mover";
                    else if (!escenario[posiciones[0]][posiciones[1] - 1].equals("p"))
                        return "atacar";
                    else
                        return "nada";
                }
                break;
            case "S":
                if (posiciones[0] + 1 < escenario.length) {
                    if (escenario[posiciones[0] + 1][posiciones[1]].equals("s")
                            || escenario[posiciones[0] + 1][posiciones[1]].equals("t"))
                        return "mover";
                    else if (!escenario[posiciones[0] + 1][posiciones[1]].equals("p"))
                        return "atacar";
                    else
                        return "nada";
                }
                break;
            case "D":
                if (posiciones[1] + 1 < escenario[0].length) {
                    if (escenario[posiciones[0]][posiciones[1] + 1].equals("s")
                            || escenario[posiciones[0]][posiciones[1] + 1].equals("t"))
                        return "mover";
                    else if (!escenario[posiciones[0]][posiciones[1] + 1].equals("p"))
                        return "atacar";
                    else
                        return "nada";
                }
                break;
            default:
                break;
        }
        return accion;
    }

    public void recibirDanio(int cantidad) {
        setPuntosvida(getPuntosvida() - cantidad);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Personaje)) {
            throw new IllegalArgumentException("Objeto no es de tipo Personaje");
        }
        Personaje otro = (Personaje) o;
        return Integer.compare(otro.velocidad, this.velocidad);
    }

    @Override
    public String toString() {
        return "Personaje: HP: " + this.puntosvida + " def " + this.defensa + "fr  " + this.fuerza + " img "
                + this.imagen + " name "
                + this.nombre
                + " speed " + this.velocidad + " % " + this.porcentajeCritico + " id " + this.id;
    }

}
