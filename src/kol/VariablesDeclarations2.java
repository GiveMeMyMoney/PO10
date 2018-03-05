/*
package kol;

import javax.naming.OperationNotSupportedException;
import java.util.*;

class VariablesDeclarations2 implements VariablesDeclarationsInterface {

    private HashMap<String, DataType> declaredVariables = new HashMap<>();
    private ArrayList<DeclarationObserverInterface> observers = new ArrayList<>();
    private boolean isLocked = false;

    public VariablesDeclarations() {
    }

    @Override
    public void link(String variableName, DataType type, boolean force) throws VariableAlreadyExistsException, OperationNotSupportedException, InvalidCastException {

        if (declaredVariables.containsKey(variableName)) {
            if (force) {
                if(declaredVariables.get(variableName) != type) {
                    for (DeclarationObserverInterface object : observers) {
                        object.typeChange(variableName, declaredVariables.get(variableName), type);
                    }
                } else {
                    for (DeclarationObserverInterface object : observers) {
                        object.removeVariable(variableName, declaredVariables.get(variableName));
                    }
                }
                declaredVariables.put(variableName, type);
            } else {
                throw new VariableAlreadyExistsException();
            }
        } else if (this.isLocked) {
            if (force) {
                declaredVariables.put(variableName, type);
                for (DeclarationObserverInterface object : observers) {
                    object.newVariable(variableName, type);
                }
            } else
                throw new OperationNotSupportedException();
        } else {
            for (DeclarationObserverInterface object : observers) {
                object.newVariable(variableName, type);
            }
            declaredVariables.put(variableName, type);
        }
    }

    @Override
    public void removeVariable(String variableName) {
        for (DeclarationObserverInterface object : observers) {
            object.removeVariable(variableName, declaredVariables.get(variableName));
        }
        declaredVariables.remove(variableName);
    }

    @Override
    public void lockTypes() {
        for (DeclarationObserverInterface object : observers) {
            object.declarationsLocked();
        }
        this.isLocked = true;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public Optional<DataType> getType(String variableName) {
        Optional<DataType> dataType = Optional.ofNullable(declaredVariables.get(variableName));
        return dataType;
    }

    @Override
    public Map<String, DataType> getDeclaredVariables() {
        return new HashMap<>(this.declaredVariables);
    }

    @Override
    public void addObserver(DeclarationObserverInterface observer) {
        observers.add(observer);
    }

    @Override
    public void addObserverAndSendInfo(DeclarationObserverInterface observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DeclarationObserverInterface observer) {
        observers.remove(observer);
    }
}*/
