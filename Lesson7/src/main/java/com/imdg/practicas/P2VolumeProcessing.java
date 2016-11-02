package com.imdg.practicas;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.impl.predicates.EqualPredicate;
import com.hazelcast.query.impl.predicates.NotEqualPredicate;
import com.imdg.pojos.MarketOrder;
import com.imdg.processors.OrderProcessor;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Sobremesa on 31/10/2016.
 */
public class P2VolumeProcessing {

    private static void rellenaCache(IMap<String, MarketOrder> mapCustomers) {

        //Limpia cache
        mapCustomers.clear();

        //Añade 1000 ordenes a la cache
        for (int i=0; i<1000; ++i) {
            MarketOrder order = new MarketOrder("BBVA",400, 642);
            MarketOrder orderRep = new MarketOrder("Intel",500,3400);
            mapCustomers.set("Ibex35OrderID_"+i, order);
            mapCustomers.set("DowJonesOrderID_"+i, orderRep);
        }

        //Actualiza 100 ordenes
        for (int i=0; i<100; ++i) {
            MarketOrder order = new MarketOrder("BBVA",555, 642);
            MarketOrder orderRep = new MarketOrder("Intel",1000,3400);
            mapCustomers.set("Ibex35OrderID_"+i, order);
            mapCustomers.set("DowJonesOrderID_"+i, orderRep);
        }

    }


    public static void main(String[] args) throws Exception {
        // Instanciar hazelcast Cliente y crear una cache{
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        HazelcastInstance client = Hazelcast.newHazelcastInstance( config );
        IMap<String, MarketOrder> mapCustomers = client.getMap("ordenesMercado");
        //Rellena cache (simulación, en un caso real la rellenaría la operativa diaria)
        rellenaCache(mapCustomers);


        //Añadir listener a la cache que imprima/de una alerta cuando detecte que el volumen acumulado ha llegado a 30000
        Map<String, Object> ret=mapCustomers.executeOnEntries(new OrderProcessor());
        //Ret contiene parejas <Clave, ObjetoRetornadoEnProcess del Processor>

        //Procesar los objetos que ha devuelto nuestro processor,
        //Recordad que nuestro processor devuelve un entero
        for (Map.Entry<String,Object> orderVolumes : ret.entrySet()) {

        }


        if( mapCustomers.entrySet(new EqualPredicate("volume",0)).isEmpty() ) {
            throw new Exception("Los volumenes de todos los elementos deben quedar a 0");
        }

        client.shutdown();
    }
}
