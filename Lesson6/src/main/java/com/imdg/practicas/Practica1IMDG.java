package com.imdg.practicas;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.imdg.pojos.Person;

import java.util.Map;
import java.util.Queue;

public class Practica1IMDG {

    public static void main(String[] args) {
        // Configuración recomendada para la práctica
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        // Crear una instancia de hazelcast y crear una caché
        // Insertar un dato y arrancar 3 veces el main
        // Leer el output de consola y ver como hazelcast va encontrando "miembros"
        // Comprobar que se conectan (en el output deberían verse 3 miembros en la consola) y capturarlo

    }
}
