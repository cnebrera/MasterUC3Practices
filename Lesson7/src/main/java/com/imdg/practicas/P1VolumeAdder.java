package com.imdg.practicas;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.imdg.pojos.MarketOrder;

import java.util.ArrayList;
import java.util.Map;

public class P1VolumeAdder {

    public static void main(String[] args) {
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        HazelcastInstance client = Hazelcast.newHazelcastInstance( config );



        IMap<String, MarketOrder> mapCustomers = client.getMap("ordenesMercado");
        //Limpia cache
        mapCustomers.clear();

        //Añade 1000 ordenes a la cache
        for (int i=0; i<1000; ++i) {
            MarketOrder order = new MarketOrder("BBVA",400, 642);
            MarketOrder orderRep = new MarketOrder("Intel",500,3400);
            mapCustomers.put("Ibex35OrderID_"+i, order);
            mapCustomers.put("DowJonesOrderID_"+i, orderRep);
        }

        //Actualiza 100 ordenes
        for (int i=0; i<100; ++i) {
            MarketOrder order = new MarketOrder("BBVA",555, 642);
            MarketOrder orderRep = new MarketOrder("Intel",1000,3400);
            mapCustomers.put("Ibex35OrderID_"+i, order);
            mapCustomers.put("DowJonesOrderID_"+i, orderRep);
        }

        client.shutdown();

    }
}
