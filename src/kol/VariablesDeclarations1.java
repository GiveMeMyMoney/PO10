/*
package kol;

import javax.naming.OperationNotSupportedException;
import java.util.*;

class VariablesDeclarations1 implements VariablesDeclarationsInterface {
    List<DeclarationObserverInterface> observers = new ArrayList<>();
    Map<String, DataType> dataTypeMap = new HashMap<>();
    boolean isLocked = false;
    boolean flag = false;

    @Override
    public void link(String variableName, DataType type, boolean force) throws VariableAlreadyExistsException, OperationNotSupportedException, InvalidCastException {
        if(!isLocked && type !=null) {
            if(!dataTypeMap.containsKey(variableName)) {
                //deklaracja nowej zmiennej
                dataTypeMap.put(variableName,type);
                for(DeclarationObserverInterface observer: observers) {
                    observer.newVariable(variableName,type);
                }
            } else if(!force) {
                throw new VariableAlreadyExistsException();
            } else if(force) {
                if(dataTypeMap.get(variableName).toString().equals(type.toString())) {
                    //typ jest taki sam jak uzyto wczezsniej

                    DataType oldType = dataTypeMap.get(variableName);
                    dataTypeMap.remove(variableName);
                    dataTypeMap.put(variableName,type);

                    for(DeclarationObserverInterface observer: observers) {
                        observer.removeVariable(variableName,dataTypeMap.get(variableName));
                    }

                    for(DeclarationObserverInterface observer: observers) {
                        observer.newVariable(variableName,type);
                    }
                } else {
                    //typ jest inny niz poprzednio
                    DataType oldType = dataTypeMap.get(variableName);
                    dataTypeMap.remove(variableName);
                    dataTypeMap.put(variableName,type);

                    for(DeclarationObserverInterface observer: observers) {
                        try {
                            observer.typeChange(variableName, oldType, type);
                        } catch(InvalidCastException ex) {
                            flag = true;
                            continue;
                        }
                    }
                    if(flag) {
                        flag = false;
                        throw new InvalidCastException();
                    }
                }
            }
        } else {
            throw new OperationNotSupportedException();
        }
    }

    @Override
    public void removeVariable(String variableName) {
        if(!isLocked) {
            if (dataTypeMap.containsKey(variableName)) {
                DataType dataToRemove = dataTypeMap.get(variableName);
                dataTypeMap.remove(variableName);
                for (DeclarationObserverInterface observer : observers) {
                    observer.removeVariable(variableName, dataToRemove);
                }
            }
        }
    }

    @Override
    public void lockTypes() {
        isLocked = true;

        for(DeclarationObserverInterface observer: observers) {
            observer.declarationsLocked();
        }

    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public Optional<DataType> getType(String variableName) {
        if(dataTypeMap.containsKey(variableName)) {
            return Optional.of(dataTypeMap.get(variableName));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Map<String, DataType> getDeclaredVariables() {
        Map<String, DataType> mapToReturn = new HashMap<>();
        for(Map.Entry<String, DataType> entry: dataTypeMap.entrySet()) {
            mapToReturn.put(entry.getKey(),entry.getValue());
        }
        return mapToReturn;
    }

    @Override
    public void addObserver(DeclarationObserverInterface observer) {
        observers.add(observer);
    }

    @Override
    public void addObserverAndSendInfo(DeclarationObserverInterface observer) {
        observers.add(observer);

        // wyslanie informacji i wszystkich zmiennych w systemie
        for(Map.Entry<String, DataType> entry: dataTypeMap.entrySet()) {
            observer.newVariable(entry.getKey(),entry.getValue());
        }

        if(isLocked) {
            observer.declarationsLocked();
        }

    }

    @Override
    public void removeObserver(DeclarationObserverInterface observer) {
        observers.remove(observer);
    }
}
*/
