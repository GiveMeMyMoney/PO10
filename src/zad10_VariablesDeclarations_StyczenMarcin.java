import java.util.*;

import javax.naming.OperationNotSupportedException;

/**
 * Interfejs systemu umoĹźliwiajÄ?cego deklaracjÄ? zmiennych. System nie odpowiada
 * za przechowywanie wartoĹ?ci lecz tylko powiÄ?zaĹ? nazw zmiennych z typami
 * danych.
 */
interface VariablesDeclarationsInterface {

    /**
     * Typ wyliczeniowy zawierajÄ?cy listÄ? wszystkich wspieranych typĂłw zmiennych.
     */
    public enum DataType {
        INT, LONG, DOUBLE, BOOLEAN;
    }

    /**
     * Interfejs obserwatora deklaracji - dziÄ?ki niemu zarejestrowani obserwatorzy
     * sÄ? informowani o zmianach w deklaracjach zmiennych.
     */
    public interface DeclarationObserverInterface {
        /**
         * Metoda informuje obserwatora o zadeklarowaniu nowej zmiennej.
         *
         * @param variableName nazwa nowej zmiennej
         * @param type         typ danych
         */
        public void newVariable(String variableName, DataType type);

        /**
         * Metoda informuje obserwatora o usuniÄ?ciu zmiennej.
         *
         * @param variableName nazwa usuniÄ?tej zmiennej
         * @param type         typ danych jaki posiadaĹ?a usuniÄ?ta zmienna
         */
        public void removeVariable(String variableName, DataType type);

        /**
         * Metoda informuje obserwatora o zmianie typu zmiennej.
         *
         * @param variableName nazwa zmiennej, ktorej typ ulegĹ? zmianie
         * @param oldType      stary typ zmiennej
         * @param newType      nowy typ zmiennej
         * @throws InvalidCastException wyjÄ?tek zgĹ?aszany gdy zmiana typu nie jest moĹźliwa.
         */
        public void typeChange(String variableName, DataType oldType, DataType newType) throws InvalidCastException;

        /**
         * Proces deklaracji zmiennych zostaĹ? zablokowany - Ĺźadnych zmian w strukturze
         * zmiennych juĹź nie bÄ?dzie.
         */
        public void declarationsLocked();
    }

    /**
     * WyjÄ?tek generowany w przypadku prĂłby deklaracji zmiennej o nazwie, ktĂłra juĹź
     * istnieje w systemie.
     */
    public class VariableAlreadyExistsException extends Exception {
        private static final long serialVersionUID = -1359266555417969831L;
    }

    /**
     * WyjÄ?tek generowany gdy konwersja pomiÄ?dzy typami danych jest niedozwolona.
     */
    public class InvalidCastException extends Exception {
        private static final long serialVersionUID = 1437212007484257087L;
    }

    /**
     * Metoda umoĹźliwia deklaracjÄ? zmiennej i zmianÄ? typu zmiennej. JeĹ?li wywoĹ?anie
     * metody link moĹźe doprowadziÄ? do wyzwolenia odpowiednich metod przekazujÄ?cych
     * informacjÄ? obserwatorom. I tak:
     * <ul>
     * <li>JeĹ?li zadeklarowano nowÄ? zmiennÄ? obserwatorzy informowani sÄ? o tym fakcie
     * za pomocÄ? metody newVariable.
     * <li>JeĹ?li zmienna jest juĹź zadeklarowana a parametr force otrzymaĹ? wartoĹ?Ä?
     * false, to pojawa siÄ? wyjÄ?tek VariableAlreadyExistsException. Obserwatorzy nie
     * sÄ? informowani.
     * <li>JeĹ?li zmienna jest juĹź zadeklarowana a parametr farce otrzymaĹ? wartoĹ?Ä?
     * true, to:
     * <ul>
     * <li>JeĹ?li typ jest taki sam jak uĹźyto wczeĹ?niej, to obserwatorzy informowani
     * sÄ? za pomocÄ? dwĂłch metod: removeVariable oraz newVariable - spowoduje to
     * ponowne przypisanie zmiennej wartoĹ?ci poczÄ?tkowej.
     * <li>JeĹ?li typ jest inny niĹź uĹźywany poprzednio, obserwatorzy informowani sÄ?
     * za pomocÄ? metody typeChange.
     * </ul>
     * </ul>
     *
     * @param variableName nazwa zmiennej
     * @param type         typ danych przechowywanych w zmiennej
     * @param force        wymuszenie zmiany typu lub ponownej deklaracji istniejÄ?cej
     *                     zmiennej. Wymuszona zmiana typu zawsze jest zapamiÄ?tywana nawet
     *                     jeĹ?li dowolny z obserwatorĂłw uzna jÄ? za nielegalnÄ? (zgĹ?osi wyjÄ?tek
     *                     InvalidCastException).
     * @throws VariableAlreadyExistsException zmienna juz istnieje
     * @throws OperationNotSupportedException operacja nie jest mozliwa do wykonania z powodu uruchomienia
     *                                        blokady lub jako type wskazano NULL.
     * @throws InvalidCastException           z punktu widzenia co najmniej jednego obserwatora wymuszona
     *                                        zmiana typu nie jest moĹźliwa. W tej wersji systemu wyjÄ?tek ten
     *                                        pojawia siÄ? wyĹ?Ä?cznie wtedy, gdy zostanie zgĹ?oszony przez
     *                                        obserwatora - w takim przypadku metoda link przekazuje informacjÄ?
     *                                        do dalszych obserwatorĂłw i koĹ?czy pracÄ? zgĹ?aszajÄ?c omawiany
     *                                        wyjÄ?tek. <br>
     *                                        Nawet jeĹ?li dojdzie do zgĹ?oszenia InvalidCastException wymuszona
     *                                        zmiana typu jest zapamiÄ?tywana - za rozwiÄ?zanie problemu
     *                                        odpowiada uĹźytkownik. Sugerowane rozwiÄ?zanie: usuniÄ?cie zmiennej
     *                                        i ponowna deklaracja.
     */
    public void link(String variableName, DataType type, boolean force)
            throws VariableAlreadyExistsException, OperationNotSupportedException, InvalidCastException;

    /**
     * Metoda usuwa zmiennÄ? o podanej nazwie (o ile istnieje). Informuje o tym
     * fakcie obserwatorĂłw.
     *
     * @param variableName nazwa usuwanej zmiennej
     */
    public void removeVariable(String variableName);

    /**
     * Metoda blokuje moĹźliwoĹ?Ä? zmiany struktury zmiennych. Od chwili uruchomienia
     * lockTypes nie bÄ?dzie moĹźliwe ani zadeklarowanie nowej zmiennej ani zmiana
     * typu (nawet na taki sam) juĹź istniejÄ?cej. Metoda link koĹ?czy siÄ? od chwili
     * wykonania tej metody wyjÄ?tkiem OperationNotSupportedException.
     */
    public void lockTypes();

    /**
     * Metoda pozwalajÄ?ca sprawdziÄ? czy baza zmiennych jest zablokowana
     *
     * @return true - blokada zaĹ?oĹźona, false - brak blokady, to jest ustawienie
     * poczÄ?tkowe systemu
     */
    public boolean isLocked();

    /**
     * Metoda zwraca typ zmiennej o podanej nazwie, o ile taka zmienna istnieje.
     * JeĹ?li jej nie ma zwracany jest obiekt Optional zawierajÄ?cy NULL.
     *
     * @param variableName nazwa zmiennej
     * @return typ zmiennej variableName
     */
    public Optional<DataType> getType(String variableName);

    /**
     * Metoda zwraca mapÄ? zawierajÄ?cÄ? informacjÄ? o wszystkich zmiennych jakie
     * zostaĹ?y do chwili wykonania tej metody zadeklarowane. Uwaga: nie wolno
     * zwracaÄ? oryginalnej mapy (o ile taka istnieje) aby uĹźytkownik nie wprowadziĹ?
     * bez wiedzy systemu nowej zmiennej.
     *
     * @return mapa nazwa zmiennej na typ
     */
    public Map<String, DataType> getDeclaredVariables();

    /**
     * Metoda dodaje obserwatora.
     *
     * @param observer obiekt - obserwator
     */
    public void addObserver(DeclarationObserverInterface observer);

    /**
     * Metoda dodaje obserwatora i przekazuje do niego za pomocÄ? metodu newVariable
     * i declarationsLocked (o ile jest taka potrzeba) aktualny stan zadeklarowanych
     * zmiennych.
     *
     * @param observer obserwator
     */
    public void addObserverAndSendInfo(DeclarationObserverInterface observer);

    /**
     * Metoda usuwa obserwatora. Od tej chwili obserwator nie jest informowany o
     * zmianach w strukturze zmiennych.
     *
     * @param observer obserwator do usuniecia
     */
    public void removeObserver(DeclarationObserverInterface observer);
}

class VariablesDeclarations implements VariablesDeclarationsInterface {
    List<DeclarationObserverInterface> observerList = new ArrayList<>();
    Map<String, DataType> variableTypeMap = new HashMap<>();
    boolean isLock = false;

    private void hardInformObservers(String variableName, DataType newType) {
        observerList.forEach(observer -> {
            observer.removeVariable(variableName, newType);
            observer.newVariable(variableName, newType);
        });
    }

    @Override
    public void link(String variableName, DataType type, boolean force) throws VariableAlreadyExistsException, OperationNotSupportedException, InvalidCastException {
        if (!isLock) {
            //Jeśli zadeklarowano nową zmienną obserwatorzy informowani są o tym fakcie za pomocą metody newVariable.
            if (!variableTypeMap.containsKey(variableName)) {
                variableTypeMap.put(variableName, type);
                observerList.forEach(observer ->
                        observer.newVariable(variableName, type));
            } else {
                // Jeśli zmienna jest już zadeklarowana a parametr force otrzymał wartość false, to pojawa się wyjątek VariableAlreadyExistsException. Obserwatorzy nie są informowani.
                if (!force) {
                    throw new VariableAlreadyExistsException();
                } else {
                    //Jeśli typ jest taki sam jak użyto wcześniej, to obserwatorzy informowani są za pomocą dwóch metod: removeVariable oraz newVariable - spowoduje to ponowne przypisanie zmiennej wartości początkowej.
                    DataType oldDataType = variableTypeMap.get(variableName);
                    if (oldDataType.equals(type)) {
                        hardInformObservers(variableName, type);
                    } // Jeśli typ jest inny niż używany poprzednio, obserwatorzy informowani są za pomocą metody typeChange.
                    else {
                        variableTypeMap.put(variableName, type);
                        boolean invalidCastExceptionFlag = false;
                        for (DeclarationObserverInterface observer : observerList) {
                            try {
                                observer.typeChange(variableName, oldDataType, type);
                            } catch (InvalidCastException e) {
                                hardInformObservers(variableName, type);
                                invalidCastExceptionFlag = true;
                            }
                        }
                        if (invalidCastExceptionFlag) {
                            throw new InvalidCastException();
                        }
                    }
                }
            }
        } else {
            throw new OperationNotSupportedException();
        }
    }

    @Override
    public void removeVariable(String variableName) {
        if (!isLock && variableTypeMap.containsKey(variableName)) {
            observerList.forEach(observer ->
                    observer.removeVariable(variableName, variableTypeMap.get(variableName)));
            variableTypeMap.remove(variableName);
        }
    }

    @Override
    public void lockTypes() {
        isLock = true;
        observerList.forEach(DeclarationObserverInterface::declarationsLocked);
    }

    @Override
    public boolean isLocked() {
        return isLock;
    }

    @Override
    public Optional<DataType> getType(String variableName) {
        return Optional.ofNullable(variableTypeMap.get(variableName));
    }

    @Override
    public Map<String, DataType> getDeclaredVariables() {
        return variableTypeMap;
    }

    @Override
    public void addObserver(DeclarationObserverInterface observer) {
        observerList.add(observer);
    }

    @Override
    public void addObserverAndSendInfo(DeclarationObserverInterface observer) {
        observerList.add(observer);

        variableTypeMap.forEach(observer::newVariable);
        if (isLock) {
            observer.declarationsLocked();
        }
    }

    @Override
    public void removeObserver(DeclarationObserverInterface observer) {
        observerList.remove(observer);
    }
}
