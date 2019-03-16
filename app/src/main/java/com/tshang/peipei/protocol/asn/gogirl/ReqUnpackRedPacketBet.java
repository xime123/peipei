//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqUnpackRedPacketBet.java
//
//   Java class for ASN.1 definition ReqUnpackRedPacketBet as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Tue Jan 26 18:38:19 2016
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>ReqUnpackRedPacketBet</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqUnpackRedPacketBet implements ASN1Type {

  /** member variable representing the sequence member type of type java.math.BigInteger */
  public java.math.BigInteger type;
  /** member variable representing the sequence member selfuid of type java.math.BigInteger */
  public java.math.BigInteger selfuid;
  /** member variable representing the sequence member redpacketuid of type java.math.BigInteger */
  public java.math.BigInteger redpacketuid;
  /** member variable representing the sequence member redpacketid of type java.math.BigInteger */
  public java.math.BigInteger redpacketid;

  /** default constructor */
  public ReqUnpackRedPacketBet() {}

  /** copy constructor */
  public ReqUnpackRedPacketBet (ReqUnpackRedPacketBet arg) {
    type = arg.type;
    selfuid = arg.selfuid;
    redpacketuid = arg.redpacketuid;
    redpacketid = arg.redpacketid;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(type);
    enc.encodeInteger(selfuid);
    enc.encodeInteger(redpacketuid);
    enc.encodeInteger(redpacketid);
    enc.endOf(seq_nr);
  }

  /** decoding method.
    * @param dec
    *        decoder object derived from com.ibm.asn1.ASN1Decoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            decoding error
    */
  public void decode (ASN1Decoder dec) throws ASN1Exception {
    int seq_nr = dec.decodeSequence();
    type = dec.decodeInteger();
    selfuid = dec.decodeInteger();
    redpacketuid = dec.decodeInteger();
    redpacketid = dec.decodeInteger();
    dec.endOf(seq_nr);
  }

  /** print method (variable indentation)
    * @param os
    *        PrintStream representing the print destination (file, etc)
    * @param indent
    *        number of blanks that preceed each output line.
    */
  public void print (PrintStream os, int indent) {
    os.println("{ -- SEQUENCE --");
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("type = ");
    os.print(type.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("selfuid = ");
    os.print(selfuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("redpacketuid = ");
    os.print(redpacketuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("redpacketid = ");
    os.print(redpacketid.toString());
    os.println();
    for(int ii = 0; ii < indent; ii++) os.print(' ');
    os.print('}');
  }

  /** default print method (fixed indentation)
    * @param os
    *        PrintStream representing the print destination (file, etc)
    */
  public void print (PrintStream os) {
    print(os,0);
  }

  /** toString method
    * @return the output of {@link #print(PrintStream) print} method (fixed indentation) as a string
    */
  public String toString () {
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    print(ps);
    ps.close();
    return baos.toString();
  }

}
