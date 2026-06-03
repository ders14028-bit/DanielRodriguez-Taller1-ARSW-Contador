package main

import (
	"fmt"
	"sync"
	"time"
)

func counter(inicio, fin int64, wg *sync.WaitGroup) {
	defer wg.Done()

	for i := inicio; i <= fin; i++ {
		fmt.Println(i)
	}
}

func main() {

	var limite int64 = 1000000
	numHilos := 500000

	bloque := limite / int64(numHilos)

	var wg sync.WaitGroup

	tiempoInicio := time.Now()

	var inicio int64 = 1

	for i := 0; i < numHilos; i++ {

		var fin int64

		if i == numHilos-1 {
			fin = limite
		} else {
			fin = inicio + bloque - 1
		}

		wg.Add(1)
		go counter(inicio, fin, &wg)

		inicio = fin + 1
	}

	wg.Wait()

	duracion := time.Since(tiempoInicio)

	fmt.Println("Conteo finalizado")
	fmt.Printf("Tiempo: %v\n", duracion)
	fmt.Printf("Tiempo: %.3f segundos\n", duracion.Seconds())
}
