public class Counter extends Thread {

    private int inicio;
    private int fin;

    public Counter(int inicio, int fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    public void run() {
        for (int i = inicio; i <= fin; i++) {
            System.out.println(i);
        }
    }
}