package com.imdg.practicas;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;

import java.util.ArrayList;

public class Practica3IMDG {

    public static void main(String[] args) {
	    // Instanciar hazelcast Cliente y crear una caché
        ClientConfig config = new ClientConfig();
        ArrayList<String> ips=new ArrayList();
        ips.add("127.0.0.1");
        config.getNetworkConfig().setAddresses(ips);

        HazelcastInstance client = HazelcastClient.newHazelcastClient( config );

        //Vuestro código va aquí


        //Apagar la instancia
        client.shutdown();

    }
}
