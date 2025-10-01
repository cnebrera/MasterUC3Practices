package com.imdg.practicas;

import com.hazelcast.config.Config;

public class Practica1IMDG {

    public static void main(String[] args) {
        // Configuración recomendada para la práctica
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost").setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        // TODO Crear una instancia de hazelcast con la configuración config

        // TODO Crear una caché llamada customers

        // TODO insertar una nueva entrada en la caché con la clave “Test” y un valor con vuestras iniciales

        // Leer el output de consola y ver como hazelcast va encontrando "miembros"
        // Comprobar que se conectan (en el output deberían verse 3 miembros en la consola) y capturarlo

        //while (true);
    }
}
