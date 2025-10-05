package com.imdg.practicas;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.imdg.listeners.VolumeListener;
import com.imdg.pojos.MarketOrder;

public class P1VolumeControl {


    public static void main(String[] args) {
        // Instanciar hazelcast Cliente y crear una cache{
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        HazelcastInstance client = Hazelcast.newHazelcastInstance(config);
        IMap<String, MarketOrder> mapCustomers = client.getMap("ordenesMercado");

        //Añadimos Listener
        mapCustomers.addEntryListener(new VolumeListener("BBVA"), true);

    }
}
