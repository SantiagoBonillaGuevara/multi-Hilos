package com.example.multi_threading;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Random;

public class OrderProcessingExample {
    
    public static void main(String[] args) {
        // Lista de pedidos con diferentes montos
        List<Integer> orderAmounts = List.of(150, 300, 450, 600, 750);
        
        // Crea un ExecutorService con un pool de 3 hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Lista para almacenar las tareas
        List<Callable<Integer>> tasks = new ArrayList<>();
        
        // Añadimos una tarea por cada pedido
        for (int amount : orderAmounts) {
            tasks.add(() -> processOrder(amount));
        }
        
        try {
            // Ejecuta todas las tareas y obtiene una lista de futuros
            List<Future<Integer>> results = executor.invokeAll(tasks);
            
            int totalRevenue = 0;
            // Itera sobre los futuros y suma los resultados
            for (Future<Integer> result : results) {
                totalRevenue += result.get(); // get() bloquea hasta obtener el resultado
            }
            
            System.out.println("Total revenue from all orders: $" + totalRevenue);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // Cierra el pool de hilos
            executor.shutdown();
        }
    }
    
    // Método para procesar un pedido con un monto específico
    public static int processOrder(int amount) {
        Random random = new Random();
        int processingTime = random.nextInt(2000) + 1000; // Tiempo de procesamiento aleatorio entre 1 y 3 segundos
        
        try {
            System.out.println("Processing order of $" + amount + "...");
            Thread.sleep(processingTime); // Simula el tiempo de procesamiento del pedido
            System.out.println("Order of $" + amount + " processed in " + processingTime + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return amount; // Retorna el monto del pedido procesado
    }
}
