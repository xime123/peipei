//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqRechargeFinish.java
//
//   Java class for ASN.1 definition ReqRechargeFinish as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Tue Jan 26 18:38:17 2016
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>ReqRechargeFinish</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqRechargeFinish implements ASN1Type {

  /** member variable representing the sequence member rechargeno of type byte[] */
  public byte[] rechargeno;
  /** member variable representing the sequence member paytype of type java.math.BigInteger */
  public java.math.BigInteger paytype;
  /** member variable representing the sequence member payresult of type java.math.BigInteger */
  public java.math.BigInteger payresult;
  /** member variable representing the sequence member notifyrechargecny of type java.math.BigInteger */
  public java.math.BigInteger notifyrechargecny;
  /** member variable representing the sequence member notifyrechargetime of type byte[] */
  public byte[] notifyrechargetime;
  /** member variable representing the sequence member fromos of type java.math.BigInteger */
  public java.math.BigInteger fromos;
  /** member variable representing the sequence member aibeiflow of type java.math.BigInteger */
  public java.math.BigInteger aibeiflow;

  /** default constructor */
  public ReqRechargeFinish() {}

  /** copy constructor */
  public ReqRechargeFinish (ReqRechargeFinish arg) {
    rechargeno = new byte[arg.rechargeno.length];
    System.arraycopy(arg.rechargeno,0,rechargeno,0,arg.rechargeno.length);
    paytype = arg.paytype;
    payresult = arg.payresult;
    notifyrechargecny = arg.notifyrechargecny;
    notifyrechargetime = new byte[arg.notifyrechargetime.length];
    System.arraycopy(arg.notifyrechargetime,0,notifyrechargetime,0,arg.notifyrechargetime.length);
    fromos = arg.fromos;
    aibeiflow = arg.aibeiflow;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeOctetString(rechargeno);
    enc.encodeInteger(paytype);
    enc.encodeInteger(payresult);
    enc.encodeInteger(notifyrechargecny);
    enc.encodeOctetString(notifyrechargetime);
    enc.encodeInteger(fromos);
    enc.encodeInteger(aibeiflow);
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
    rechargeno = dec.decodeOctetString();
    paytype = dec.decodeInteger();
    payresult = dec.decodeInteger();
    notifyrechargecny = dec.decodeInteger();
    notifyrechargetime = dec.decodeOctetString();
    fromos = dec.decodeInteger();
    aibeiflow = dec.decodeInteger();
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
    os.print("rechargeno = ");
    os.print(Hex.toString(rechargeno));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("paytype = ");
    os.print(paytype.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("payresult = ");
    os.print(payresult.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("notifyrechargecny = ");
    os.print(notifyrechargecny.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("notifyrechargetime = ");
    os.print(Hex.toString(notifyrechargetime));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fromos = ");
    os.print(fromos.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("aibeiflow = ");
    os.print(aibeiflow.toString());
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