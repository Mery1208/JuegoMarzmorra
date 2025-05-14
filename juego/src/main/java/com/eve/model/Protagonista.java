package com.eve.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Protagonista extends Personaje {
    private int nivel;
    private int muertes;
    private int xp;
    private String protaChico = "/com/eve/images/protaChico.png";
    private String protaChica = "/com/eve/images/protaChica.png";
    private String tipoJugador;

    public Protagonista() {
        super();
        this.muertes = 0;
        this.nivel = 1;
        this.xp = 0;
        this.tipoJugador = "AVENTURERO";
    }

    /**
     * Contructor de la clase Protagonista
     * 
     * @param nombre            del personaje protagonista
     * @param puntosvida        del personaje protagonista
     * @param porcentajeCritico del personaje protagonista
     * @param fuerza            del personaje protagonista
     * @param defensa           del personaje protagonista
     * @param velocidad         de movimiento, establece el orden de peferencia de
     *                          movimiento
     * @param tipoJugador       Tipo del jugador, según este se determinan sus
     *                          estadísticas.
     * @param nivel             nivel del prota, 0 al iniciarse el juego
     * @param xp                experiencia para subir de nivel
     */
    public Protagonista(String imagen, String nombre, int puntosvida, int porcentajeCritico, int fuerza, int defensa,
            int velocidad, String tipoJugador, int id) {
        super(imagen, nombre, puntosvida, porcentajeCritico, fuerza, defensa, velocidad, id);
        this.muertes = 0;
        this.nivel = 1;
        this.xp = 0;
        this.posicion = new int[] { 0, 0 };
        this.tipoJugador = tipoJugador;
    }

    /**
     * Nivel de experiencia de los protagonistas.
     * 
     * @return nivel
     */
    public int getNivel() {
        return this.nivel;
    }

    /**
     * Método para establecer el nivel del prota
     * 
     * @param nivel del prota
     */

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    /**
     * Muerte de los enemigos que matan los personajes
     * 
     * @return muertes
     */
    public int getMuertes() {
        return this.muertes;
    }

    /**
     * Método para establecer el número de enemigos a los que mata el prota
     * (muertes).
     * 
     * @param muertes número de muertes del prota de los enemigos
     */

    public void setMuertes(int muertes) {
        this.muertes = muertes;
    }

    /**
     * Expericencia de los personajes, aumenta cuando matan a los enemigos para que
     * estos puedan subir de nivel
     * 
     * @return xp
     */

    public int getXp() {
        return this.xp;
    }

    /**
     * Método para establecer la cantidad de experiencia del prota
     * 
     * @param xp del prota.
     */
    public void setXp(int xp) {
        this.xp = xp;
    }

    /**
     * Conseguir la imagen del personaje chico
     * 
     * @return protaChico
     */

    public String getProtaChico() {
        return this.protaChico;
    }

    /**
     * Método para establecer la imagen del prota cuando se selecciona un chico
     * 
     * @param protaChico imagen del prota chico.
     */
    public void setProtaChico(String protaChico) {
        this.protaChico = protaChico;
    }

    /**
     * Conseguir la imagen del personaje chica
     * 
     * @return protaChica
     */

    public String getProtaChica() {
        return this.protaChica;
    }

    /**
     * Método para establecer la imagen del prota cuando se selecciona un chico
     * 
     * @param protaChica imagen de la prota chica.
     */
    public void setProtaChica(String protaChica) {
        this.protaChica = protaChica;
    }

    /**
     * Tipo de jugador del prota, según este, las estadísticas del prota varían
     * 
     * @return tipoJugador
     */
    public String getTipoJugador() {
        return this.tipoJugador;
    }

    /**
     * Método para establecer el tipo de jugador que es el prota, en base a un enum,
     * para obtener las estadistcias, las cuales difieren entre roles.
     * 
     * @param tipoJugador rol del jugador. Según el rol, el prota tendrá unas u
     *                    otras estadísticas.
     */
    public void setTipoJugador(String tipoJugador) {
        this.tipoJugador = tipoJugador;
    }

    @Override
    public void atacarPersonaje(int nuevaFila, int nuevaCol, String[][] escenario) {
        Random r = new Random();
        ArrayList<Personaje> personajes = Proveedor.getInstance().getGestorJuego().getPersonajes();
        GestorJuego gestor = Proveedor.getInstance().getGestorJuego();

        // Datos del enemigo
        int id = Integer.parseInt(escenario[nuevaFila][nuevaCol]);
        Enemigo enemigo = gestor.buscarEnemigo(id);
        int vidaEnemigo = enemigo.getPuntosvida();
        int defensaEnemigo = enemigo.getDefensa();
        int xpDa = enemigo.getXpDan();
        int vidaDa = enemigo.getVidaDan();
        int defensaDan = enemigo.getDefensaDan();
        int danioAenemigo = this.fuerza - defensaEnemigo;
        if (r.nextInt(100) < this.porcentajeCritico)
            danioAenemigo *= 2;

        if (danioAenemigo > 0) {
            vidaEnemigo -= danioAenemigo;
            enemigo.setPuntosvida(vidaEnemigo);
            gestor.notifyObservers();
        }

        if (vidaEnemigo <= 0) {
            this.puntosvida += vidaDa;
            this.xp += xpDa;
            this.defensa += defensaDan;
            this.muertes++;
            this.fuerza = this.fuerza + enemigo.getFuerzaDan();
            escenario[nuevaFila][nuevaCol] = "s";
            gestor.setEvento("Enemigo Asesinado con el ID: " + enemigo.getId() + ". Recibes " + enemigo.getVidaDan()
                    + " de vida, " + enemigo.getXpDan() + " de experiencia, " + enemigo.getFuerzaDan() + " de fuerza y "
                    + enemigo.getDefensaDan() + " de defensa.");
            for (Iterator<Personaje> iterator = personajes.iterator(); iterator.hasNext();) {
                Personaje p = iterator.next();
                if (p.getId() == enemigo.getId()) {
                    iterator.remove();
                    break;
                }
            }
            this.subirNivel();
            gestor.notifyObservers();
        }
    }

    /**
     * Método para que el prota realice su acción (mover o atacar) correspondiente
     * en base a la tecla presionada.
     * Con esta tecla se decide si el prota se mueve (porque la casilla nueva es un
     * suelo vacío) o
     * ataca.
     * 
     * @param teclaPresionada dirección que debe tomar el prota.
     */

    public void realizarAccionProta(String teclaPresionada) {
        GestorJuego gestorJuego = Proveedor.getInstance().getGestorJuego();
        Escenario escenarioObj = gestorJuego.getEscenario();
        int[] posicion = this.getPosicion();
        String accion = comprobarAccion(this.getPosicion(), teclaPresionada);
        switch (teclaPresionada) {
            case "W":
                switch (accion) {
                    case "mover":
                        escenarioObj.moverPersonaje(this, posicion[0] - 1, posicion[1]);
                        break;
                    case "atacar":
                        this.atacarPersonaje(posicion[0] - 1, posicion[1], escenarioObj.getEscenario());
                        break;
                    default:
                        break;
                }
                break;
            case "A":
                switch (accion) {
                    case "mover":
                        escenarioObj.moverPersonaje(this, posicion[0], posicion[1] - 1);
                        break;
                    case "atacar":
                        this.atacarPersonaje(posicion[0], posicion[1] - 1, escenarioObj.getEscenario());
                        break;
                    default:
                        break;
                }
                break;
            case "S":
                switch (accion) {
                    case "mover":
                        escenarioObj.moverPersonaje(this, posicion[0] + 1, posicion[1]);
                        break;
                    case "atacar":
                        this.atacarPersonaje(posicion[0] + 1, posicion[1], escenarioObj.getEscenario());
                        break;
                    default:
                        break;
                }
                break;
            case "D":
                switch (accion) {
                    case "mover":
                        escenarioObj.moverPersonaje(this, posicion[0], posicion[1] + 1);
                        break;
                    case "atacar":
                        this.atacarPersonaje(posicion[0], posicion[1] + 1, escenarioObj.getEscenario());
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    public void subirNivel() {
        GestorJuego gestorJuego = Proveedor.getInstance().getGestorJuego();
        int xp = this.getXp();
        int nivelActual = this.getNivel();
        int nuevoNivel = calcularNivelPorXP(xp);
        boolean nivelCambio = false;
        if (nuevoNivel > nivelActual) {
            this.setNivel(nuevoNivel);
            gestorJuego.notifyObservers();
            nivelCambio = true;
        }
        if (nivelCambio) {
            String nivelS = "";
            switch (nuevoNivel) {
                case 2:
                    nivelS = gestorJuego.getEnemigos().get("nivel2");
                    break;
                case 3:
                    nivelS = gestorJuego.getEnemigos().get("nivel3");
                    break;
                case 4:
                    nivelS = gestorJuego.getEnemigos().get("nivel4");
                    break;
                case 5:
                    nivelS = gestorJuego.getEnemigos().get("nivel5");
                    break;
            }
            nuevosEnemigos(nivelS);
        }
    }

    /**
     * Método para cambiar a los enemigos cuando el prota sube de nivel
     * Si tuvieramos más escenarios, habría que pasarle la ruta del escenario para
     * que se lea en setEscenario
     *
     * @param nivel del prota
     * 
     */
    public void nuevosEnemigos(String nivel) {
        GestorJuego gestorJuego = Proveedor.getInstance().getGestorJuego();
        ArrayList<Personaje> personajesNuevos = new ArrayList<>();
        this.setPosicion(new int[] { 0, 0 });
        try {
            personajesNuevos.add(this);
            personajesNuevos.addAll(gestorJuego.getLectorEnemigo().leerCSV(new File(nivel)));
            personajesNuevos.sort(null);
            gestorJuego.setPersonajes(personajesNuevos);
            gestorJuego.getEscenario().setEscenario("");
            gestorJuego.getEscenario().generarPosiciones();
            gestorJuego.notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para saber si el prota tiene suficiente experiecnia para subir o no
     * de nivel.
     * 
     * @param xp del prota
     * @return el número del nivel de que corresponde en base a su experiencia
     */
    public int calcularNivelPorXP(int xp) {
        if (xp >= 4000)
            return 5;
        if (xp >= 1700 && xp < 4000)
            return 4;
        if (xp >= 1200 && xp < 1700)
            return 3;
        if (xp >= 500 && xp < 1200)
            return 2;
        return 1;
    }

    @Override
    public String toString() {
        return super.toString() + " Muertes: " + this.muertes + " Nvl:  " + this.nivel + " XP: " + this.xp
                + " Tipo de jugador: " + this.tipoJugador;
    }
}
