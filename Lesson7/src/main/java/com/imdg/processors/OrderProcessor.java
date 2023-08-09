package com.imdg.processors;

import com.hazelcast.map.AbstractEntryProcessor;
import com.imdg.pojos.MarketOrder;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Sobremesa on 31/10/2016.
 */
public class OrderProcessor
        extends AbstractEntryProcessor<String,MarketOrder> implements Serializable {


    /**
     * Metodo que debe procesar cada entrada y cambiar su volumen a 0, y devolver el antiguo
     * @param entry Hazelcast llamará a este metodo para cada entrada de la cache
     * @return Integer con el volumen que existia
     */
    @Override
    public Object process(Map.Entry<String,MarketOrder> entry) {
        // TODO Recuperar el objeto MarketOrder de la entrada
        // TODO Guardar el valor del volumen de la orden
        // TODO Actualizar el volumen de la orden a 0
        // TODO Actualizar el valor de la entrada con la orden actualizada
        // TODO Devolver el valor del volumen guardado anteriormente
        return null;
    }
}
