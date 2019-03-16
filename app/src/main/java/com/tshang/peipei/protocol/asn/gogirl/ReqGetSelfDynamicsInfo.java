//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqGetSelfDynamicsInfo.java
//
//   Java class for ASN.1 definition ReqGetSelfDynamicsInfo as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Mon Aug 31 20:10:48 2015
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>ReqGetSelfDynamicsInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Mon Aug 31 20:10:47 2015

  */

public class ReqGetSelfDynamicsInfo implements ASN1Type {

  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member type of type java.math.BigInteger */
  public java.math.BigInteger type;
  /** member variable representing the sequence member replynum of type java.math.BigInteger */
  public java.math.BigInteger replynum;
  /** member variable representing the sequence member start of type java.math.BigInteger */
  public java.math.BigInteger start;
  /** member variable representing the sequence member num of type java.math.BigInteger */
  public java.math.BigInteger num;

  /** default constructor */
  public ReqGetSelfDynamicsInfo() {}

  /** copy constructor */
  public ReqGetSelfDynamicsInfo (ReqGetSelfDynamicsInfo arg) {
    uid = arg.uid;
    type = arg.type;
    replynum = arg.replynum;
    start = arg.start;
    num = arg.num;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(uid);
    enc.encodeInteger(type);
    enc.encodeInteger(replynum);
    enc.encodeInteger(start);
    enc.encodeInteger(num);
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
    uid = dec.decodeInteger();
    type = dec.decodeInteger();
    replynum = dec.decodeInteger();
    start = dec.decodeInteger();
    num = dec.decodeInteger();
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
    os.print("uid = ");
    os.print(uid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("type = ");
    os.print(type.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("replynum = ");
    os.print(replynum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("start = ");
    os.print(start.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("num = ");
    os.print(num.toString());
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
