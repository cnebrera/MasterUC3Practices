package com.cnebrera.uc3.tech.lesson9.utils;

import com.cnebrera.uc3.tech.lesson9.model.Instrument;
import com.cnebrera.uc3.tech.lesson9.model.ReferenceData;
import com.cnebrera.uc3.tech.lesson9.proto.Lesson9;

import java.util.ArrayList;

/**
 * Utils class for creating objects for testing
 */
public final class Utils {

    public static ReferenceData getReferenceData() {
        Instrument i1 = new Instrument();
        i1.setInstrumentId(1);
        i1.setSymbol("BBVA");
        Instrument i2 = new Instrument();
        i2.setInstrumentId(2);
        i2.setSymbol("SAN");
        Instrument i3 = new Instrument();
        i3.setInstrumentId(3);
        i3.setSymbol("FDAX");
        Instrument i4 = new Instrument();
        i4.setInstrumentId(4);
        i4.setSymbol("TEF");
        Instrument i5 = new Instrument();
        i5.setInstrumentId(5);
        i5.setSymbol("IBER");
        ArrayList<Instrument> instruments = new ArrayList<>();
        instruments.add(i1);
        instruments.add(i2);
        instruments.add(i3);
        instruments.add(i4);
        instruments.add(i5);
        ReferenceData referenceData = new ReferenceData();
        referenceData.setMarketId(1);
        referenceData.setAlgorithmIdentifier("TWAP");
        referenceData.setListOfInstruments(instruments);
        return referenceData;
    }

    public static Lesson9.ReferenceData getProtoReferenceData() {
        ReferenceData referenceData = Utils.getReferenceData();
        Lesson9.ReferenceData.Builder referenceDataBuilder = Lesson9.ReferenceData.newBuilder();
        // Set the parameters in the builder using the values read in referenceData
        referenceDataBuilder.setMarketId(referenceData.getMarketId())
                .setAlgorithmIdentifier(referenceData.getAlgorithmIdentifier());
        for (Instrument value : referenceData.getListOfInstruments()) {
            referenceDataBuilder.addInstrument(Lesson9.Instrument.newBuilder()
                    .setInstrumentId(value.getInstrumentId())
                    .setSymbol(value.getSymbol()));
        }
        return referenceDataBuilder.build();
    }

}
