//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/RspGetRelevantCounter.java
//
//   Java class for ASN.1 definition RspGetRelevantCounter as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Tue Jan 26 18:38:18 2016
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>RspGetRelevantCounter</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class RspGetRelevantCounter implements ASN1Type {

  /** member variable representing the sequence member retcode of type java.math.BigInteger */
  public java.math.BigInteger retcode;
  /** member variable representing the sequence member retmsg of type byte[] */
  public byte[] retmsg;
  /** member variable representing the sequence member fanscounter of type java.math.BigInteger */
  public java.math.BigInteger fanscounter;
  /** member variable representing the sequence member visitecounter of type java.math.BigInteger */
  public java.math.BigInteger visitecounter;
  /** member variable representing the sequence member revint0 of type java.math.BigInteger */
  public java.math.BigInteger revint0;
  /** member variable representing the sequence member revint1 of type java.math.BigInteger */
  public java.math.BigInteger revint1;
  /** member variable representing the sequence member revint2 of type java.math.BigInteger */
  public java.math.BigInteger revint2;
  /** member variable representing the sequence member revint3 of type java.math.BigInteger */
  public java.math.BigInteger revint3;
  /** member variable representing the sequence member revint4 of type java.math.BigInteger */
  public java.math.BigInteger revint4;

  /** default constructor */
  public RspGetRelevantCounter() {}

  /** copy constructor */
  public RspGetRelevantCounter (RspGetRelevantCounter arg) {
    retcode = arg.retcode;
    retmsg = new byte[arg.retmsg.length];
    System.arraycopy(arg.retmsg,0,retmsg,0,arg.retmsg.length);
    fanscounter = arg.fanscounter;
    visitecounter = arg.visitecounter;
    revint0 = arg.revint0;
    revint1 = arg.revint1;
    revint2 = arg.revint2;
    revint3 = arg.revint3;
    revint4 = arg.revint4;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(retcode);
    enc.encodeOctetString(retmsg);
    enc.encodeInteger(fanscounter);
    enc.encodeInteger(visitecounter);
    enc.encodeInteger(revint0);
    enc.encodeInteger(revint1);
    enc.encodeInteger(revint2);
    enc.encodeInteger(revint3);
    enc.encodeInteger(revint4);
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
    retcode = dec.decodeInteger();
    retmsg = dec.decodeOctetString();
    fanscounter = dec.decodeInteger();
    visitecounter = dec.decodeInteger();
    revint0 = dec.decodeInteger();
    revint1 = dec.decodeInteger();
    revint2 = dec.decodeInteger();
    revint3 = dec.decodeInteger();
    revint4 = dec.decodeInteger();
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
    os.print("retcode = ");
    os.print(retcode.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("retmsg = ");
    os.print(Hex.toString(retmsg));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fanscounter = ");
    os.print(fanscounter.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("visitecounter = ");
    os.print(visitecounter.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint0 = ");
    os.print(revint0.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint1 = ");
    os.print(revint1.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint2 = ");
    os.print(revint2.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint3 = ");
    os.print(revint3.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint4 = ");
    os.print(revint4.toString());
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