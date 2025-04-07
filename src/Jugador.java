import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

import javax.swing.JPanel;

public class Jugador {

    private int TOTAL_CARTAS = 10;
    private int MARGEN = 10;
    private int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random(); // la suerte del jugador

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();

        int posicion = MARGEN + (TOTAL_CARTAS - 1) * DISTANCIA;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN);
            posicion -= DISTANCIA;
        }

        pnl.repaint();
    }
    public int[] getContadorCartas(List<Carta> cartas) {
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }
        return contadores;
    }
    
    public int[] getContadorInt(List<Integer> indices) {

        
        int[] contadores = new int[NombreCarta.values().length];
        for (Integer index : indices) {
            index = index % 13;
            if (index == 0) {
                index = 13;
            }
            index = index - 1; // Ajustar el índice para que coincida con el ordinal
            if (index >= 0 && index < NombreCarta.values().length) {
                contadores[index]++;
            }
        }
        return contadores;
    }
    
    public int[] getGrupos() {

        
        int [] contadores = getContadorCartas(Arrays.asList(cartas));

       
        return contadores;

        
    }

    public String mostrarGrupos() {
        String mensaje = "No se encontraron grupos";

        int[] contadores = getGrupos();

        boolean hayGrupos = false;
        for (int contador : contadores) {
            if (contador >= 2) {
                hayGrupos = true;
                break;
            }
        }

        if (hayGrupos) {
            mensaje = "Se encontraron los siguientes grupos:\n";
            int fila = 0;
            for (int contador : contadores) {
                if (contador >= 2) {
                    mensaje += Grupo.values()[contador] + " de " + NombreCarta.values()[fila] + "\n";
                }
                fila++;
            }
        }

        return mensaje;
    }

    public List<List<Integer>> getEscalera() {
        
        // Crear un arreglo de índices
        int[] indices = new int[cartas.length];
        for (int i = 0; i < cartas.length; i++) {
            indices[i] = cartas[i].getIndice() ;
        }


        // Ordenar los índices
        Arrays.sort(indices);

        // Crear un arreglo de cartasModulo     
        int[] cartasModulo = new int[cartas.length];
        
        for (int i = 0; i < indices.length; i++) {
            cartasModulo[i] = indices[i] % 14;
        }
        

        List<List<Integer>> IndicesEscaleraLista = new ArrayList<>(); 
        int indiceEscalera = 0;
        while (indiceEscalera<cartasModulo.length-1) {
            
            int inicio = indiceEscalera;
            int grupoEscalera = 1;

            while ((indiceEscalera < cartasModulo.length -1)  && 
            ( cartasModulo[indiceEscalera] + 1 == cartasModulo[indiceEscalera + 1])) {
                
                indiceEscalera++;
                grupoEscalera++;
                
            }
            
            if (grupoEscalera  > 1) {
                IndicesEscaleraLista.add(Arrays.asList(indices[inicio], grupoEscalera));
                
                
            }

            indiceEscalera++;
            
        } 
            
       
        
       
        return IndicesEscaleraLista;
    }

    public String mostrarEscalera() {
        String mensaje = "No se encontraron escaleras";
        boolean hayEscalera = false;
        List<List<Integer>> IndicesEscaleraLista = getEscalera();
        if (IndicesEscaleraLista.size() > 0) {
            hayEscalera = true;
        }
        if (hayEscalera) {
            mensaje = "Se encontraron las siguientes escaleras:\n";  // Reemplaza el mensaje inicial solo si se encontraron escaleras.
        }
        

        for (int i = 0; i < IndicesEscaleraLista.size(); i++) {
            int indiceCarta = IndicesEscaleraLista.get(i).get(0);
            int tamanoGrupo = IndicesEscaleraLista.get(i).get(1);
        
            // Encontrar la carta correspondiente al índice almacenado.
            Carta carta = null;
            for (Carta c : cartas) {
                if (c.getIndice() == indiceCarta) {
                    carta = c;
                    break;
                }
            }
        
            if (carta != null) {
                mensaje += "Escalera desde: " + carta.getNombre() + " de " + carta.getPinta() + " de " + tamanoGrupo + " cartas\n";
            }
        }
       
        

        

        return mensaje;
    }

    public List<Integer> getListasExpandidaEscalera(List<List<Integer>> IndicesEscaleraLista) {
        
        // Crear un arreglo de índices
        List<Integer> listaExpandida = new ArrayList<>();

        // Expandir cada sublista
        for (List<Integer> par : IndicesEscaleraLista) {
            
            int start = par.get(0);    // Índice inicial de la escalera
            int length = par.get(1);   // Cantidad de cartas en esa escalera
            for (int i = 0; i < length; i++) {
                listaExpandida.add(start + i);
            }
        }

        
       return listaExpandida;
    }

    public String getPuntaje(){
        String mensaje = "No se encontraron puntajes";
        
        int puntaje = 0;

        // Lista para almacenar los enteros expandidos
        List<List<Integer>> IndicesEscaleraLista = getEscalera();
        List<Integer> listaExpandida = getListasExpandidaEscalera(IndicesEscaleraLista);

        // Contadores de cartas grupos y Escalera
        int[] contadorEscalera = getContadorInt(listaExpandida);
        int [] contadores = getContadorCartas(Arrays.asList(cartas));
        
        //System.out.println("Contador de escalera: " + (IndicesEscaleraLista));
        //System.out.println("Contador de escalera expandida: " + Arrays.toString(contadorEscalera));
        //System.out.println("Contador de cartas: " + Arrays.toString(contadores));


        // Suma de Ambos contadores para eliminar los grupos
        int[] sumaContadores = new int[contadores.length];
        for (int i = 0; i < contadores.length; i++) {
            sumaContadores[i] = contadores[i] + contadorEscalera[i]; //suma ambos
            if (sumaContadores[i] >= 2) {
                sumaContadores[i] = 0; // si hay un grupo, se elimina
            }
            if (sumaContadores[i] == 1) {
                if (i >= 10) {
                    puntaje += 10; // si hay un grupo, se elimina
                } else {
                    puntaje += i + 1; // si hay una carta, se suma el puntaje
                }
                
            }
        }
        

        
        if (puntaje > 0) {
            mensaje = "El puntaje es: " + puntaje;
        }
        return mensaje;
    }
}
