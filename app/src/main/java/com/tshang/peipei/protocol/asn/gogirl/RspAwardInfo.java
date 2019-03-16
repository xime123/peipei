//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/RspAwardInfo.java
//
//   Java class for ASN.1 definition RspAwardInfo as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Sat Oct 10 11:02:49 2015
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>RspAwardInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Sat Oct 10 11:02:49 2015

  */

public class RspAwardInfo implements ASN1Type {

  /** member variable representing the sequence member retcode of type java.math.BigInteger */
  public java.math.BigInteger retcode;
  /** member variable representing the sequence member retmsg of type byte[] */
  public byte[] retmsg;
  /** member variable representing the sequence member isend of type java.math.BigInteger */
  public java.math.BigInteger isend;
  /** member variable representing the sequence member awardDetailList of type AwardDetailList */
  public AwardDetailList awardDetailList = new AwardDetailList();

  /** default constructor */
  public RspAwardInfo() {}

  /** copy constructor */
  public RspAwardInfo (RspAwardInfo arg) {
    retcode = arg.retcode;
    retmsg = new byte[arg.retmsg.length];
    System.arraycopy(arg.retmsg,0,retmsg,0,arg.retmsg.length);
    isend = arg.isend;
    awardDetailList = new AwardDetailList(arg.awardDetailList);
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
    enc.encodeInteger(isend);
    awardDetailList.encode(enc);
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
    isend = dec.decodeInteger();
    awardDetailList.decode(dec);
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
    os.print("isend = ");
    os.print(isend.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("awardDetailList = ");
    awardDetailList.print(os, indent+2);
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
