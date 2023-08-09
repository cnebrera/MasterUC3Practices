package com.imdg.listeners;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapListener;
import com.imdg.pojos.MarketOrder;

import java.io.Serializable;

/**
 * Created by Sobremesa on 31/10/2016.
 */
public class VolumeListener
            implements EntryAddedListener<String, MarketOrder>,
        EntryUpdatedListener<String, MarketOrder>, Serializable {

    private String instrumentoAControlar;
    private int volumenAcumulado=0;

    public VolumeListener(String instrument) {
        this.instrumentoAControlar=instrument;
    }

    /**
     * Escuchar entradas que se añaden y sumarlo al volumen/imprimir alerta si llegamos a 30000
     * @param entryEvent
     */
    @Override
    public void entryAdded(EntryEvent<String, MarketOrder> entryEvent) {
        // TODO Recuperar la propiedad instrument del objeto MarketOrder y comprobar si es igual a instrumentoAControlar
        // TODO Si es igual, actualizar el volumen acumulado de este listener con el volumen del objeto MarketOrder
        // TODO Comprobar si el volumen acumulado es mayor que 30000
        // TODO Si es mayor, poner el volumen acumulado a 0 e imprimir por pantalla un mensaje de alerta
    }

    /**
     * Escuchar entradas que se añaden, restar valor antiguo y
     * sumar el nuevo al volumen/imprimir alerta si llegamos a 30000
     * @param entryEvent
     */
    @Override
    public void entryUpdated(EntryEvent<String, MarketOrder> entryEvent) {

    }
}
