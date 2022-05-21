package com.svalero.juego;


import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Tablero {

    //Variables del juego
    public char[][] tableroBart;

    public char[][] tableroHomer;

    private Integer[] posicionBart = new Integer[2];

    private Integer[] posicionHomer = new Integer[2];

    public Integer vidasBart;

    public Integer vidasHomer;

    public Integer numVidasExtra;

    public Integer numEnemigos;

    private static final int vidaInicial = 3;


    //Modos opcionales
    private boolean tableroInfinito = false;
    public boolean vidasAdicionales = false;
    private boolean vidasSaltan = false;
    private boolean casillasOcultas = false;

    //Constructor
    public Tablero() {
        tableroBart = new char[6][6];
        tableroHomer = new char[6][6];
        vidasBart = vidasHomer = vidaInicial;
        numVidasExtra = 2;
        numEnemigos = 8;
    }


    //Devuelve el estado del tablero del jugador seleccionado

    private void mostrarTablero(boolean bart, boolean ocultar) {
        for (int i = 0; i < tableroBart.length; i++) {
            for (int j = 0; j < tableroBart[i].length; j++) {
                if (bart) {
                    char casilla = tableroBart[i][j];
                    if ((casilla == 'K' || casilla == 'V') & ocultar) {
                        System.out.print("L");
                    } else {
                        System.out.print(tableroBart[i][j]);
                    }
                } else {
                    char casilla = tableroHomer[i][j];
                    if ((casilla == 'F' || casilla == 'V') & ocultar) {
                        System.out.print("L");
                    } else {
                        System.out.print(tableroHomer[i][j]);
                    }
                }
            }
            System.out.println();
        }
    }

    //Muestra el tablero de juego de Bart si esta el modo casillasOcultas se ocultaran todas las casillas
    public void mostrarTableroBart() {
        if (casillasOcultas) {
            mostrarTablero(true, true);
        } else {
            mostrarTablero(true, false);
        }
    }

    //Muestra el tablero de juego de Homer si esta el modo casillasOcultas se ocultaran todas las casillas
    public void mostrarTableroHomer() {
        if (casillasOcultas) {
            mostrarTablero(false, true);
        } else {
            mostrarTablero(false, false);
        }
    }

    //Rellena el tablero de juego con posiciones aleatorias para el jugador (B o H) y los enemigos (K o F). Las casillas libres se rellenan con el caracter "L"
    private void rellenaTablero(boolean bart) {
        Integer min = 0;
        Integer max = (tableroBart.length * tableroBart[0].length) - 1;
        Integer posicionInicial;
        Set<Integer> posicionVidas = new HashSet<>();
        Set<Integer> posicionesUsadas = new HashSet<>();
        Random r1 = new Random(System.currentTimeMillis());

        //Posicion del jugador
        posicionInicial = r1.nextInt(max - min + 1) + min;
        posicionesUsadas.add(posicionInicial);

        //Genera aleatoriamente las vidas
        if (vidasAdicionales) {
            for (int i = 0; i < numVidasExtra; i++) {
                Integer rand = -1;
                boolean repetido = false;
                while (!repetido) {
                    rand = r1.nextInt(max - min + 1) + min;
                    if (!posicionesUsadas.contains(rand)) {
                        posicionesUsadas.add(rand);
                        posicionVidas.add(rand);
                        repetido = true;
                    }
                }
            }
        }

        //Genera aleatoriamente los enemigos
        for (int i = 0; i < numEnemigos; i++) {
            Integer rand = -1;
            boolean repetido = false;
            while (!repetido) {
                rand = r1.nextInt(max - min + 1) + min;
                if (!posicionesUsadas.contains(rand)) {
                    posicionesUsadas.add(rand);
                    repetido = true;
                }
            }
        }

        //Rellena el tablero con el jugador, las vidas, los enemigos y las casillas libres.
        Integer acum = 0;
        for (int i = 0; i < tableroBart.length; i++) {
            for (int j = 0; j < tableroBart[i].length; j++) {
                //Si es la posicion del jugador, añade el jugador al tablero
                if (posicionInicial.equals(acum)) {
                    if (bart) {
                        tableroBart[i][j] = 'B';
                        posicionBart[0] = i;
                        posicionBart[1] = j;

                    } else {
                        tableroHomer[i][j] = 'H';
                        posicionHomer[0] = i;
                        posicionHomer[1] = j;
                    }
                    //Añade las vidas al tablero
                } else if (posicionVidas.contains(acum)) {
                    if (bart) {
                        tableroBart[i][j] = 'V';
                    } else {
                        tableroHomer[i][j] = 'V';
                    }
                    //Añade los enemigos al tablero
                } else if (posicionesUsadas.contains(acum)) {
                    if (bart) {
                        tableroBart[i][j] = 'K';
                    } else {
                        tableroHomer[i][j] = 'F';
                    }
                    //Añade las casillas libres
                } else {
                    if (bart) {
                        tableroBart[i][j] = 'L';
                    } else {
                        tableroHomer[i][j] = 'L';
                    }
                }
                acum++;
            }
        }
    }

    //Mueve el jugador en la direccion y casillas pasadas por parametros
    private void moverJugador(char direccion, Integer casillas, boolean bart) {
        //Tablero [FILA][COLMUNA]
        //S     W      A         D
        //Abajo,Arriba,Izquierda,Derecha
        char[][] copiaTablero;
        Integer[] posicionJugador = new Integer[2];

        //Hago una copia del tablero y de la posicion del jugador para trabajar sobre ellos
        if (bart) {
            copiaTablero = tableroBart.clone();
            posicionJugador = posicionBart.clone();
        } else {
            copiaTablero = tableroHomer.clone();
            posicionJugador = posicionHomer.clone();
        }

        //Libero la casilla en la que estaba antes el jugador.
        copiaTablero[posicionJugador[0]][posicionJugador[1]] = 'L';
        //Compruebo que a la casilla a la que se va a mover no sobrepasa el tablero
        //Si el tablero es infinito sale por el otro lado
        switch (direccion) {
            case 'S':
                //Abajo
                if (posicionJugador[0] + casillas > copiaTablero.length - 1) {
                    if (tableroInfinito) {
                        posicionJugador[0] = posicionJugador[0] + casillas - copiaTablero.length;
                    } else {
                        posicionJugador[0] = copiaTablero.length - 1;
                    }
                } else {
                    posicionJugador[0] = posicionJugador[0] + casillas;
                }
                break;
            case 'W':
                //Arriba
                if (posicionJugador[0] - casillas < copiaTablero.length - 1) {
                    if (tableroInfinito) {
                        posicionJugador[0] = posicionJugador[0] - casillas + (copiaTablero.length);
                    } else {
                        posicionJugador[0] = 0;
                    }
                } else {
                    posicionJugador[0] = posicionJugador[0] - casillas;
                }
                break;
            case 'A':
                //Izquierda
                if (posicionJugador[1] - casillas < 0) {
                    if (tableroInfinito) {
                        posicionJugador[1] = posicionJugador[1] - casillas + (copiaTablero[0].length);
                    } else {
                        posicionJugador[1] = 0;
                    }
                } else {
                    posicionJugador[1] = posicionJugador[1] - casillas;
                }
                break;
            case 'D':
                //Derecha
                if (posicionJugador[1] + casillas > copiaTablero[0].length - 1) {
                    if (tableroInfinito) {
                        posicionJugador[1] = posicionJugador[1] + casillas - (copiaTablero[0].length);
                    } else {
                        posicionJugador[1] = copiaTablero[0].length - 1;
                    }
                } else {
                    posicionJugador[1] = posicionJugador[1] + casillas;
                }
            default:
        }
        //Compruebo si a la casilla a la que se mueve tiene un enemigo o una vida.
        char casilla = copiaTablero[posicionJugador[0]][posicionJugador[1]];
        if (casilla == 'K' || casilla == 'F') {
            System.out.println("Caiste en un enemigo, una vida menos!");
            if (bart) {
                vidasBart--;
            } else {
                vidasHomer--;
            }
        } else if (casilla == 'V') {
            System.out.println("Consigues una vida extra!");
            if (bart) {
                vidasBart++;
            } else {
                vidasHomer++;
            }
        }

        //Muevo el jugador a la casilla correspondiente y actualizo los tableros y la posicion del jugador
        if (bart) {
            copiaTablero[posicionJugador[0]][posicionJugador[1]] = 'B';
            tableroBart = copiaTablero.clone();
            posicionBart = posicionJugador.clone();

        } else {
            copiaTablero[posicionJugador[0]][posicionJugador[1]] = 'H';
            tableroHomer = copiaTablero.clone();
            posicionHomer = posicionJugador.clone();
        }
    }

    //Inicia el juego, se van alternando los turnos hasta que algun jugador pierda todas sus vidas.
    public void gameStart() {
        //Relleno los tableros
        rellenaTablero(true);
        //Esperamos para que la semilla de la generacion aleatoria sea distinta
        Util.wait(200);
        rellenaTablero(false);
        System.out.println("El juego comienza en:");
        for (int i = 0; i < 3; i++) {
            System.out.println(3 - i);
            Util.wait(1000);
        }
        do {
            //Turno Bart
            System.out.println("-----------TE TOCA BART-----------");

            System.out.print("Vidas restantes: ");
            System.out.println(vidasBart);

            System.out.println("Estado de tu tablero:");
            mostrarTableroBart();
            System.out.println();

            //Numero de casillas que te quieres mover
            Integer casilla = validaCasilla();

            //Direccion a la que quieres moverte
            char movimiento = validaMovimiento(true);

            moverJugador(movimiento, casilla, true);
            mostrarTableroBart();

            if (vidasSaltan) {
                mueveVidas(true);
            }

            System.out.println("-----------FIN TURNO DE BART-----------");
            System.out.print("Vidas restantes: ");
            System.out.println(vidasBart);

            //Espera cinco segundos para dar tiempo a que se vea como queda el tablero
            Util.wait(5000);

            //Si las vidas de Bart estan agotadas, se acaba el juego.
            if (vidasBart == 0) {
                break;
            }

            System.out.println("-----------INICIO TURNO HOMER-----------");

            System.out.print("Vidas restantes: ");
            System.out.println(vidasHomer);

            System.out.println("Estado de tu tablero: ");
            mostrarTableroHomer();
            System.out.println();

            //Numero de casillas que te quieres mover
            casilla = validaCasilla();

            //Direccion a la que quieres moverte
            movimiento = validaMovimiento(false);

            moverJugador(movimiento, casilla, false);
            mostrarTableroHomer();

            if (vidasSaltan) {
                mueveVidas(false);
            }

            System.out.println("-----------FIN TURNO HOMER-----------");
            System.out.print("Vidas restantes: ");
            System.out.println(vidasHomer);

            Util.wait(5000);

            //Si las vidas de Homer son 0, se acaba el juego
        } while (vidasHomer != 0);

        if (vidasBart == 0) {
            System.out.println("HOMER HA GANADO");
        } else {
            System.out.println("BART HA GANADO");
        }
    }

    //Valida el numero de casillas que te quieres mover (Entre 1 y 3).
    private Integer validaCasilla() {
        boolean aux = false;
        Scanner sc = new Scanner(System.in);
        Integer numCasillas;
        do {
            if (aux) {
                System.out.println("Solo te puedes mover de 1 a 3 casillas, introduce de nuevo el numero: ");
            } else {
                System.out.println(("Introduce el numero de casillas que te quieres mover: "));
                aux = true;
            }
            numCasillas = sc.nextInt();
        } while (!(numCasillas <= 3 & numCasillas > 0));

        return numCasillas;
    }

    //Valida la direccion de movimiento introducida por teclado.
    private char validaMovimiento(boolean bart) {
        //D     A      W         S
        //Abajo,Arriba,Izquierda,Derecha
        Integer[] posicion;
        boolean aux = false;
        boolean condicion;
        if (bart) {
            posicion = posicionBart.clone();
        } else {
            posicion = posicionBart.clone();
        }
        Scanner sc = new Scanner(System.in);
        char movimiento;

        //Comprobamos que la direccion de movimiento es la correcta
        do {
            condicion = true;
            if (aux) {
                System.out.println("Solo te puedes mover en estas direcciones: ");
                System.out.println("S     W      A         D");
                System.out.println("Abajo,Arriba,Izquierda,Derecha");
                System.out.println("Introduce de nuevo la direccion: ");
            } else {
                System.out.println(("Introduce la direccion en la que te quieres mover: "));
                aux = true;
            }

            movimiento = sc.nextLine().charAt(0);

            //Comprobamos si el movimiento esta prohibido.
            if (tableroInfinito) {
                if (posicion[0] == (0) && posicion[1] == (tableroBart[0].length - 1) && movimiento == 'W') {
                    System.out.println("Este movimiento esta prohibido, muevete hacia otra direccion.");
                    condicion = false;
                    aux = false;
                }

                if (posicion[0] == (tableroBart.length - 1) && posicion[1] == 0 && movimiento == 'A') {
                    System.out.println("Este movimiento esta prohibido, muevete hacia otra direccion.");
                    condicion = false;
                    aux = false;
                }
            }

        } while ((!(movimiento == 'D' || movimiento == 'A' || movimiento == 'W' || movimiento == 'S')) || !condicion);

        return movimiento;
    }



    // Mueve las vidas restantes del tablero a otra posicion aleatoria.
    private void mueveVidas(boolean jugador1) {
        char[][] copiaTablero;
        Integer vidasTablero = 0;
        Integer[] posicionAux = new Integer[2];
        Integer acum = 0;
        Random r1 = new Random(System.currentTimeMillis());
        Integer min = 0;
        Integer max = 35;
        if (jugador1) {
            copiaTablero = tableroBart.clone();
        } else {
            copiaTablero = tableroHomer.clone();
        }
        //Se guardan cuantas vidas quedan y se liberan las casillas.
        for (int i = 0; i < copiaTablero.length; i++) {
            for (int j = 0; j < copiaTablero[i].length; j++) {
                if (copiaTablero[i][j] == 'V') {
                    vidasTablero++;
                    copiaTablero[i][j] = 'L';
                }
                acum++;
            }
        }
        //Si quedan vidas, se generan nuevas posiciones y se añaden al tablero.
        if (acum != 0) {
            for (int i = 0; i < vidasTablero; i++) {
                Integer rand = -1;
                boolean repetido = false;
                while (!repetido) {
                    rand = r1.nextInt(max - min + 1) + min;
                    posicionAux = getContenidoCasilla(rand);
                    char casilla = copiaTablero[posicionAux[0]][posicionAux[1]];
                    if (casilla == 'L') {
                        copiaTablero[posicionAux[0]][posicionAux[1]] = 'V';
                        repetido = true;
                    }
                }
            }
        }

        if (jugador1) {
            tableroBart = copiaTablero.clone();
        } else {
            tableroHomer = copiaTablero.clone();
        }
    }

    // Devuelve la posicion en cordenadas de la casilla pasada por numero
    private Integer[] getContenidoCasilla(Integer numCasilla) {
        Integer[] res = new Integer[2];
        Integer acum = 0;
        for (int i = 0; i < tableroBart.length; i++) {
            for (int j = 0; j < tableroBart[i].length; j++) {
                if (acum.equals(numCasilla)) {
                    res[0] = i;
                    res[1] = j;
                }
                acum++;
            }
        }
        return res;
    }

    // Configura funcionalidades opcionales de la partida.
    public void configuraPartida() {
        Scanner sc = new Scanner(System.in);
        boolean salir = true;
        do {
            System.out.println("Configuracion de las funcionalidades opcionales:");
            if (tableroInfinito) {
                System.out.println("1.- Desactiva que no exista limite en el tablero.");
            } else {
                System.out.println("1.- Activa que no exista limite en el tablero.");
            }
            if (vidasAdicionales) {
                System.out.println("2.- Desactiva las vidas extra.");
            } else {
                System.out.println("2.- Activa las vidas extra.");
            }
            if (vidasSaltan && vidasAdicionales) {
                System.out.println("3.- Desactiva que las vidas salten a otra posicion en cada turno.");
            } else {
                System.out.println("3.- Activa que las vidas salten a otra posicion en cada turno (Debes activar las vidas extra).");
            }
            System.out.println("4.- Para entrar en la configuracion de los parametros del juego.");
            if (casillasOcultas) {
                System.out.println("5.- Desactiva que se muestren todas las casillas excepto la del jugador.");
            } else {
                System.out.println("5.- Activa que se oculten todas las casillas excepto la del jugador.");
            }
            System.out.println("6.- Para salir de la configuracion y comenzar a jugar");

            Integer opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    if (tableroInfinito) {
                        tableroInfinito = false;
                    } else {
                        tableroInfinito = true;
                    }
                    break;

                case 2:
                    if (vidasAdicionales) {
                        vidasAdicionales = false;
                        vidasSaltan = false;
                    } else {
                        vidasAdicionales = true;
                    }
                    break;

                case 3:
                    if (vidasSaltan) {
                        vidasSaltan = false;
                    } else if (vidasAdicionales) {
                        vidasSaltan = true;
                    }
                    break;

                case 4:
                    modificaParametrosJuego(sc);
                    break;

                case 5:
                    if (casillasOcultas) {
                        casillasOcultas = false;
                    } else {
                        casillasOcultas = true;
                    }
                    break;

                case 6:
                    salir = false;
                    break;
                default:
                    System.out.println("Opcion introducida incorrecta.");
            }
        } while (salir);
    }
    /**
     * Configurador para los modificar los parametros del juego
     * El configurador esta en fase beta, funciona correctamente, pero hay algun que otro bug...
     *
     * @param sc Scanner Objeto scanner.
     */
    private void modificaParametrosJuego(Scanner sc) {
        boolean salir = true;
        System.out.println("------------Configurador de los parametros del juego------------");
        System.out.println("La mitad de las casillas del tablero deben de estar libres,");
        System.out.println("por lo que la suma de las vidas y el numero de enemigos no");
        System.out.println("debe superar la mitad del total de casillas del tablero.");
        System.out.println("El numero de vidas tiene que ser siempre tres unidades");
        System.out.println("inferior al numero de enemigos, para poder morir.");
        System.out.println("Siempre que se cambie el tamaño del tablero, los valores de");
        System.out.println("enemigos y vidas volveran a su valor por defecto.");
        System.out.println("----------------------------------------------------------------");
        do {
            Integer totalUsado = numEnemigos + numVidasExtra;
            Integer mitadTablero = (tableroBart.length * tableroBart[0].length) / 2;
            Integer casillasLibres = mitadTablero - totalUsado;
            Integer max;
            Integer min;
            Integer condicion;

            System.out.println("----Configura los parametros del juego:");
            System.out.println("Numero de casillas libres que pueden ser ocupadas: " + (mitadTablero - totalUsado));

            System.out.println("1.-Cambia el tamaño del tablero.");
            System.out.println("2.-Cambia el numero de enemigos.");
            System.out.println("3.-Cambia el numero de vidas extra.");
            System.out.println("4.-Cambia el numero de vidas.");
            System.out.println("5.- Para salir de la configuracion");

            Integer opcion = sc.nextInt();

            switch (opcion) {
                case 4:
                    //Vidas
                    max = numEnemigos - numVidasExtra;
                    System.out.println("Introduce el numero de vidas extra (Actualmente son: " + vidasBart + ")");
                    System.out.println("Con la configuracion actual puedes poner " + max + " vidas.");
                    Integer vidas = vidasBart;
                    do {
                        vidas = sc.nextInt();
                        if (vidas > max || vidas < 1) {
                            System.out.println("Vuelve a introducir un numero:");
                        }
                    } while (vidas > max || vidas < 1);
                    vidasBart = vidas;
                    vidasHomer = vidas;
                    break;

                case 3:
                    //Vidas extra
                    if (vidasAdicionales) {
                        max = casillasLibres - (numVidasExtra + vidasBart);
                        Integer mx2 = max - numEnemigos;
                        if (Integer.signum(mx2) == -1) {
                            if (numVidasExtra == 0) {
                                mx2 = casillasLibres - (numVidasExtra + vidasBart);
                                max = vidasBart - mx2;
                                mx2 = max;
                            } else if (numVidasExtra == 1) {
                                mx2 = casillasLibres - (numVidasExtra + vidasBart);
                                mx2 = max;
                            } else {
                                mx2 = numVidasExtra;
                                max = numVidasExtra;
                            }
                        }
                        System.out.println("Introduce el numero de vidas extra (Actualmente son: " + numVidasExtra + ")");
                        System.out.println("Con la configuracion actual puedes poner " + mx2 + " vidas.");
                        Integer vidasNuevo = vidasBart;
                        do {
                            vidasNuevo = sc.nextInt();
                            condicion = vidasNuevo + vidasBart;
                            if (condicion > numEnemigos || vidasNuevo > max || vidasNuevo < 0) {
                                System.out.println("Vuelve a introducir un numero:");
                            }
                        } while (condicion > numEnemigos || vidasNuevo > max || vidasNuevo < 0);
                        numVidasExtra = vidasNuevo;
                    } else {
                        System.out.println("Antes tienes que activar las vidas extra.");
                    }
                    break;

                case 2:
                    //Enemigos
                    max = casillasLibres + numEnemigos;
                    min = numVidasExtra + vidasBart;
                    System.out.println("Introduce el numero de enemigos (Actualmente son: " + numEnemigos + ")");
                    System.out.println("Con la configuracion actual puedes desde " + min + " hasta " + max + " enemigos.");
                    Integer enemigosNuevo;
                    do {
                        enemigosNuevo = sc.nextInt();
                        if (enemigosNuevo > max || enemigosNuevo < min) {
                            System.out.println("Vuelve a introducir un numero");
                        }
                    } while (enemigosNuevo > max || enemigosNuevo < min);

                    numEnemigos = enemigosNuevo;
                    break;

                case 1:
                    //Tamaño
                    System.out.println("El tamaño por minimo y por defecto del tablero es de 6x6");
                    System.out.println("ATENCION: Los enemigos y vidas se restableceran!!");
                    System.out.print("Introduce el nuevo numero de filas: (Actualmente son: ");
                    System.out.print(tableroBart.length);
                    System.out.println("):");
                    Integer filasNuevo = 6;
                    do {
                        filasNuevo = sc.nextInt();
                        if (filasNuevo < 6) {
                            System.out.println("El dato introducido es incorrecto. El tamaño minimo es 6.");
                        }
                    } while (filasNuevo < 6);

                    System.out.print("Introduce el nuevo numero de columnas: (Actualmente son: ");
                    System.out.print(tableroBart[0].length);
                    System.out.println("):");
                    Integer columnasNuevo = 6;
                    do {
                        columnasNuevo = sc.nextInt();
                        if (columnasNuevo < 6) {
                            System.out.println("El dato introducido es incorrecto. El tamaño minimo es 6.");
                        }
                    } while (columnasNuevo < 6);

                    tableroBart = new char[filasNuevo][columnasNuevo];
                    tableroHomer = new char[filasNuevo][columnasNuevo];
                    numVidasExtra = 2;
                    vidasBart = vidasHomer = 3;
                    numEnemigos = 8;
                    break;


                case 5:
                    salir = false;
                    if (numVidasExtra == 0) {
                        vidasAdicionales = false;
                    }
                    break;
                default:
                    System.out.println("Opcion introducida incorrecta.");
            }
        } while (salir);
    }

}

