package org.omilab.omirob.mbot.client;

/**
 * Port and Slot IDs as defined in MePort.h
 *
 * @author    Martin Kunz <martin.michael.kunz@gmail.com>
 */
public enum Port {
    NC((byte)0),
    PORT_1((byte)0x01),
    PORT_2((byte)0x02),
    PORT_3((byte)0x03),
    PORT_4((byte)0x04),
    PORT_5((byte)0x05),
    PORT_6((byte)0x06),
    PORT_7((byte)0x07),
    PORT_8((byte)0x08),
    PORT_9((byte)0x09),
    PORT_10((byte)0x0a),
    PORT_11((byte)0x0b),
    PORT_12((byte)0x0c),
    PORT_13((byte)0x0d),
    PORT_14((byte)0x0e),
    PORT_15((byte)0x0f),
    PORT_16((byte)0x10),
    M1((byte)0x09),
    M2((byte)0x09),
    PORT_RGB((byte)0x05),
    PORT_LightSensor((byte)0x05),
    SLOT1((byte)1),
    SLOT2((byte)1),
    SLOT3((byte)1),
    SLOT4((byte)1)
    ;

    private final byte id;

    Port(final byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public static Port getById(int id) {
        for(Port e : values()) {
            if(e.id==id) return e;
        }
        return null;
    }
}
