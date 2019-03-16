//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqDeliverGiftV3.java
//
//   Java class for ASN.1 definition ReqDeliverGiftV3 as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqDeliverGiftV3</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqDeliverGiftV3 implements ASN1Type {

  /** member variable representing the sequence member fromuid of type java.math.BigInteger */
  public java.math.BigInteger fromuid;
  /** member variable representing the sequence member touid of type java.math.BigInteger */
  public java.math.BigInteger touid;
  /** member variable representing the sequence member giftid of type java.math.BigInteger */
  public java.math.BigInteger giftid;
  /** member variable representing the sequence member giftnum of type java.math.BigInteger */
  public java.math.BigInteger giftnum;
  /** member variable representing the sequence member isanonymous of type java.math.BigInteger */
  public java.math.BigInteger isanonymous;

  /** default constructor */
  public ReqDeliverGiftV3() {}

  /** copy constructor */
  public ReqDeliverGiftV3 (ReqDeliverGiftV3 arg) {
    fromuid = arg.fromuid;
    touid = arg.touid;
    giftid = arg.giftid;
    giftnum = arg.giftnum;
    isanonymous = arg.isanonymous;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(fromuid);
    enc.encodeInteger(touid);
    enc.encodeInteger(giftid);
    enc.encodeInteger(giftnum);
    enc.encodeInteger(isanonymous);
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
    fromuid = dec.decodeInteger();
    touid = dec.decodeInteger();
    giftid = dec.decodeInteger();
    giftnum = dec.decodeInteger();
    isanonymous = dec.decodeInteger();
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
    os.print("fromuid = ");
    os.print(fromuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("touid = ");
    os.print(touid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("giftid = ");
    os.print(giftid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("giftnum = ");
    os.print(giftnum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("isanonymous = ");
    os.print(isanonymous.toString());
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
