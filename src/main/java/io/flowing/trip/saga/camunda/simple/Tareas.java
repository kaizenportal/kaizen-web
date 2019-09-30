/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.flowing.trip.saga.camunda.simple;

import io.flowing.trip.saga.camunda.adapter.BookFlightAdapter;
import io.flowing.trip.saga.camunda.adapter.BookHotelAdapter;
import io.flowing.trip.saga.camunda.adapter.CancelCarAdapter;
import io.flowing.trip.saga.camunda.adapter.CancelFlightAdapter;
import io.flowing.trip.saga.camunda.adapter.CancelHotelAdapter;
import io.flowing.trip.saga.camunda.adapter.ReserveCarAdapter;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

/**
 *
 * @author edeleon
 */
public class Tareas {

    public void ejecutarTareas(int v) {
        // Configure and startup (in memory) engine
        ProcessEngine camunda
                = new StandaloneInMemProcessEngineConfiguration()
                        .buildProcessEngine();

        // define saga as BPMN process
        org.camunda.bpm.model.bpmn.builder.ProcessBuilder flow = Bpmn.createExecutableProcess("trip");

        // - flow of activities and compensating actions
        flow.startEvent()
                //inicia la transacción(proceos)
                .serviceTask("car").name("Reserve car").camundaClass(ReserveCarAdapter.class)
                .boundaryEvent().compensateEventDefinition().compensateEventDefinitionDone()
                .compensationStart()
                //realiza el rollback
                .serviceTask("CancelCar").camundaClass(CancelCarAdapter.class).compensationDone()
                //Inicia la transaccion(proceos)
                .serviceTask("hotel").name("Book hotel").camundaClass(BookHotelAdapter.class)
                .boundaryEvent().compensateEventDefinition().compensateEventDefinitionDone()
                .compensationStart()
                //realiza el rollback
                .serviceTask("CancelHotel").camundaClass(CancelHotelAdapter.class).compensationDone()
                //inicia la transacción(proceos)
                .serviceTask("flight").name("Book flight").camundaClass(BookFlightAdapter.class)
                .boundaryEvent().compensateEventDefinition().compensateEventDefinitionDone()
                .compensationStart()
                //realiza el rollback
                .serviceTask("CancelFlight").camundaClass(CancelFlightAdapter.class).compensationDone()
                //Finalizamos el evento de los procesos
                .endEvent();

        // - trigger compensation in case of any exception (other triggers are possible)
        flow.eventSubProcess()
                .startEvent().error("java.lang.Throwable")
                .intermediateThrowEvent().compensateEventDefinition().compensateEventDefinitionDone()
                .endEvent();

        // ready
        BpmnModelInstance saga = flow.done();
        // optional: Write to file to be able to open it in Camunda Modeler
        //Bpmn.writeModelToFile(new File("trip.bpmn"), saga);

        // finish Saga and deploy it to Camunda
        camunda.getRepositoryService().createDeployment() //
                .addModelInstance("trip.bpmn", saga) //
                .deploy();

        // now we can start running instances of our saga - its state will be persisted
        for(int i = 0; i<=v; i++){
            camunda.getRuntimeService().startProcessInstanceByKey("trip", Variables.putValue("name", "trip"+i));
        }
        //camunda.getRuntimeService().startProcessInstanceByKey("trip", Variables.putValue("name", "trip2"));
        //camunda.getRuntimeService().startProcessInstanceByKey("trip", Variables.putValue("name", "trip3"));

    }
}
