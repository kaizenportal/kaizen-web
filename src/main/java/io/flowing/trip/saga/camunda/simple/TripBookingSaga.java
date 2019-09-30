package io.flowing.trip.saga.camunda.simple;

  
/**
 * One main class containing everything to run a Saga using Camunda and BPMN.
 * The class runs an in memory engine, defines the Saga and run a couple of
 * instances.
 */
public class TripBookingSaga {

    public static void main(String[] args) {
       Tareas t = new Tareas();
       t.ejecutarTareas(4);
    }

}
