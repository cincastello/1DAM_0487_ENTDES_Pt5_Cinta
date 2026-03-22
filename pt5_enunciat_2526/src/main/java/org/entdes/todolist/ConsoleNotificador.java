package org.entdes.todolist;

public class ConsoleNotificador implements INotificador {

    @Override
    public boolean notificar(String missatge) {
        System.out.println("[NOTIFICACIÓ] " + missatge);
        return true;
    }

}
