# Juego Simpsons
Actividad de aprendizaje de 1º de DAM de la 1ªEvaluacion
Realizada por Javier Vizcaino Olivares

## Requisitos obligatorios:

- [x] Representa dos tableros de juego (2 jugadores) mediante una matriz de caracteres de 6x6.

- [x] Sitúa al jugador de cada tablero de forma aleatoria y represéntalos con el carácter ‘B’ (Bart) y ‘H’ (Homer), respectivamente. Rellena aleatoriamente con 8 “Krusty” letra “K” sobre el tablero de “Bart” y 8 “Flanders” letra “F” sobre el tablero de “Homer”. Las celdas que no estén ocupadas por los personajes de “Simpson” de cada tablero y por el jugador correspondiente, quedarán representadas con la letra ‘L’.

- [x] Cada jugador tiene 3 vidas, si cae en una casilla donde se encuentra un “personaje malo (Krusty o Flanders)”, perderá una vida. Pierde el juego el jugador que antes pierda las 3 vidas.

- [x] El jugador podrá realizar movimientos indicando dos valores:
Número de casillas: 1, 2, 3 como máximo
Desplazamiento horizontal/vertical: derecha, izquierda, arriba y abajo ('D', 'A','W' y 'S').
Cada combinación de número y letra le proporcionará un movimiento

- [x] Al comienzo del juego y para cada turno, se le mostrará a cada jugador (se juega por turnos) el estado de su tablero y la opción de moverse. Se volverá a mostrar el estado del tablero tras su movimiento antes de cambiar al otro jugador

## Requisitos opcionales:

- [x] (1,5 ptos) Prepara el juego para que no exista límite de tablero.
   - Cuando un jugador, en cualquier dirección, se vaya a salir del tablero, programaremos el juego para que sea capaz de aparecer por el lado contrario, como si rodeara el tablero.
   - Para que el usuario no haga trampas, vamos a evitar que aparezca por el otro lado del tablero en dos ocasiones:
     - Cuando esté en la casilla de la esquina inferior izquierda (6, 0) y se mueva en horizontal hacia la izquierda.
     - Cuando esté en la casilla de la esquina superior derecha (0, 6) y se mueva en vertical hacia arriba.

- [x] (1,5 ptos) Reparte 2 vidas extra en el tablero. Si el jugador cae en una casilla de tipo vida extra ‘V’, suma una vida a su marcador y desaparece la pócima.

- [x] (2 ptos) Cada vez que el jugador realiza un movimiento, las vidas saltan. Cada vez que el jugador realice un desplazamiento, actualizo el tablero si es “L” o ha caído en una casilla que le quita vida, pero después añado la funcionalidad de que las “VIDAS EXTRA SALTAN”. Buscamos una casilla aleatoria vacía para cada vida y las desplazo. 

- [x] Funcionalidad añadida por mi: Ocultar las fichas para no saber donde caes