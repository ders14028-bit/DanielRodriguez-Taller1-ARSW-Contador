public class Main {

    public static void main(String[] args) throws InterruptedException {

        int limite = 1000000;
        int numHilos = 100;

        int bloque = limite / numHilos;

        Counter[] hilos = new Counter[numHilos];

        long tiempoInicio = System.nanoTime();

        int inicio = 1;

        for (int i = 0; i < numHilos; i++) {

            int fin;

            if (i == numHilos - 1) {
                fin = limite;
            } else {
                fin = inicio + bloque - 1;
            }

            hilos[i] = new Counter(inicio, fin);
            hilos[i].start();

            inicio = fin + 1;
        }

        for (Counter hilo : hilos) {
            hilo.join();
        }

        long tiempoFin = System.nanoTime();

        long duracionNs = tiempoFin - tiempoInicio;
        double duracionMs = duracionNs / 1_000_000.0;
        double duracionSeg = duracionNs / 1_000_000_000.0;

        System.out.println("Conteo finalizado");
        System.out.printf("Tiempo: %,d ns%n", duracionNs);
        System.out.printf("Tiempo: %.3f ms%n", duracionMs);
        System.out.printf("Tiempo: %.3f s%n", duracionSeg);
    }
}
