/*
 *
 *  * Copyright 2015 AT&T Foundry
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.onosproject.aaa.packet;

import org.onlab.packet.BasePacket;
import org.onlab.packet.IPacket;

import java.nio.ByteBuffer;


/**
 *
 */
public class EAP extends BasePacket {
    public static final short MIN_LEN = 0x4;
    public static final short EAP_HDR_LEN_REQ_RESP = 5;
    public static final short EAP_HDR_LEN_SUC_FAIL = 4;

    /* EAP Code */
    public static final byte REQUEST  = 0x1;
    public static final byte RESPONSE = 0x2;
    public static final byte SUCCESS  = 0x3;
    public static final byte FAILURE  = 0x4;

    /* EAP Attribute Type */
    public static final byte ATTR_IDENTITY = 0x1;
    public static final byte ATTR_NOTIFICATION = 0x2;
    public static final byte ATTR_NAK = 0x3;
    public static final byte ATTR_MD5 = 0x4;
    public static final byte ATTR_OTP = 0x5;
    public static final byte ATTR_GTC = 0x6;
    public static final byte ATTR_TLS = 0xd;

    protected byte code;
    protected byte identifier;
    protected short length;
    protected byte type;
    protected byte[] data;


    /**
     * Get the EAP code.
     * @return EAP code
     */
    public byte getCode() {
        return this.code;
    }


    /**
     * Set the EAP code.
     * @param code EAP code
     * @return this
     */
    public EAP setCode(final byte code) {
        this.code = code;
        return this;
    }

    /**
     * Get the EAP identifier.
     * @return EAP identifier
     */
    public byte getIdentifier() {
        return this.identifier;
    }

    /**
     * Set the EAP identifier.
     * @param identifier
     * @return this
     */
    public EAP setIdentifier(final byte identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Get the get packet length.
     * @return packet length
     */
    public short getLength() {
        return this.length;
    }

    /**
     * Set the packet length.
     * @param length packet length
     * @return this
     */
    public EAP setLength(final short length) {
        this.length = length;
        return this;
    }

    /**
     * Get the data type.
     * @return data type
     */
    public byte getDataType() {
        return this.type;
    }

    /**
     * Set the data type.
     * @param type data type
     * @return this
     */
    public EAP setDataType(final byte type) {
        this.type = type;
        return this;
    }

    /**
     * Get the EAP data.
     * @return EAP data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * Set the EAP data.
     * @param data EAP data to be set
     * @return this
     */
    public EAP setData(final byte[] data) {
        this.data = data;
        return this;
    }

    /**
     * Default EAP constructor that set the EAP code to 0.
     */
    public EAP() {
        this.code = 0;
    }

    /**
     * EAP constructor that initially sets all fields.
     * @param code EAP code
     * @param identifier EAP identifier
     * @param type packet type
     * @param data EAP data
     */
    public EAP(byte code, byte identifier, byte type, byte[] data) {
        this.code = code;
        this.identifier = identifier;
        if (this.code == REQUEST || this.code == RESPONSE) {
            this.length = (short) (5 + (data == null ? 0 : data.length));
            this.type = type;
        } else {
            this.length = (short) (4 + (data == null ? 0 : data.length));
        }
        this.data = data;
    }

    /**
     * Serializes the packet, based on the code/type using the payload
     * to compute its length.
     * @return the serialized payload
     */
    @Override
    public byte[] serialize() {
        final byte[] data = new byte[this.length];

        final ByteBuffer bb = ByteBuffer.wrap(data);
        bb.put(this.code);
        bb.put(this.identifier);
        bb.putShort(this.length);
        if (this.code == REQUEST || this.code == RESPONSE) {
            bb.put(this.type);
        }
        if (this.data != null) {
            bb.put(this.data);
        }
        return data;
    }

    @Override
    public IPacket deserialize(final byte[] data, final int offset,
                               final int length) {
        final ByteBuffer bb = ByteBuffer.wrap(data, offset, length);
        this.code = bb.get();
        this.identifier = bb.get();
        this.length = bb.getShort();

        int dataLength;
        if (this.code == REQUEST || this.code == RESPONSE) {
            this.type = bb.get();
            dataLength = this.length - 5;
        } else {
            dataLength = this.length - 4;
        }
        this.data = new byte[dataLength];
        bb.get(this.data);
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 3889;
        int result = super.hashCode();
        result = prime * result + this.code;
        result = prime * result + this.identifier;
        result = prime * result + this.length;
        result = prime * result + this.type;
        return result;
    }
}
