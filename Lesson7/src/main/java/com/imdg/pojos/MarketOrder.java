package com.imdg.pojos;

import java.io.Serializable;

/**
 * Created by Sobremesa on 31/10/2016.
 */
public class MarketOrder implements Serializable{

    /* Instrument/name of the company whose stocks were traded */
    private String instrument;
    /* Amount of stocks traded */
    private Integer volume;
    /* Price at which the order was traded */
    private int price;

    public MarketOrder(String instrument, int volume, int price) {
        this.instrument = instrument;
        this.volume = volume;
        this.price = price;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
