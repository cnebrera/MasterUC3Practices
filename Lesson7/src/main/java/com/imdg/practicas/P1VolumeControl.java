package com.imdg.practicas;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.PredicateBuilder;
import com.imdg.listeners.VolumeListener;
import com.imdg.pojos.MarketOrder;

import java.util.ArrayList;

/**
 * Created by Sobremesa on 31/10/2016.
 */
public class P1VolumeControl {


    public static void main(String[] args) {
        // Instanciar hazelcast Cliente y crear una cache{
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        HazelcastInstance client = Hazelcast.newHazelcastInstance( config );
        IMap<String, MarketOrder> mapCustomers = client.getMap("ordenesMercado");

        //Añadimos Listener
        mapCustomers.addEntryListener(new VolumeListener("BBVA"),true);

    }
}
