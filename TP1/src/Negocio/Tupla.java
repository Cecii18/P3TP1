package Negocio;

public class Tupla<T1, T2> {

    private T1 elem1; // Primer elemento de la tupla
    private T2 elem2; // Segundo elemento de la tupla
    
    public Tupla(T1 elem1, T2 elem2) {
        this.elem1 = elem1;
        this.elem2 = elem2;
    }

    // Obtener el segundo elemento
    public T2 getElem2() {
        return elem2;
    }

    // Establecer el segundo elemento
    public void setElem2(T2 elem2) {
        this.elem2 = elem2;
    }

    // Obtener el primer elemento
    public T1 getElem1() {
        return elem1;
    }

    // Establecer el primer elemento
    public void setElem1(T1 elem1) {
        this.elem1 = elem1;
    }
}
