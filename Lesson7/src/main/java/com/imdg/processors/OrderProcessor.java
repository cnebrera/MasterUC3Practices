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
        return null;
    }
}
