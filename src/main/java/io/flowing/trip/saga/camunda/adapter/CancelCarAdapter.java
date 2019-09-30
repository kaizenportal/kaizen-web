package io.flowing.trip.saga.camunda.adapter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CancelCarAdapter implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) throws Exception {

     System.out.println("cancel car getActivityInstanceId: "+execution.getActivityInstanceId());
     System.out.println("cancel car getCurrentActivityId: "+execution.getCurrentActivityId());
     System.out.println("cancel car getCurrentActivityName: "+execution.getCurrentActivityName());
     System.out.println("cancel car getCurrentTransitionId: "+execution.getCurrentTransitionId());
     System.out.println("cancel car getParentActivityInstanceId: "+execution.getParentActivityInstanceId());
     System.out.println("cancel car getParentId: "+execution.getParentId());
     System.out.println("cancel car getProcessBusinessKey: "+execution.getProcessBusinessKey());
     System.out.println("cancel car getProcessDefinitionId: "+execution.getProcessDefinitionId());
     System.out.println("cancel car getProcessInstanceId: "+execution.getProcessInstanceId());
     System.out.println("cancel car getTenantId: "+execution.getTenantId());
     System.out.println("cancel car getBusinessKey: "+execution.getBusinessKey());
     System.out.println("cancel car getEventName: "+execution.getEventName());
     System.out.println("cancel car getId: "+execution.getId());
     System.out.println("cancel car getVariableScopeKey: "+execution.getVariableScopeKey());
    
  }

}
