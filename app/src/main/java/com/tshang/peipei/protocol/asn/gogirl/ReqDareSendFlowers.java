//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqDareSendFlowers.java
//
//   Java class for ASN.1 definition ReqDareSendFlowers as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqDareSendFlowers</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqDareSendFlowers implements ASN1Type {

  /** member variable representing the sequence member dareid of type byte[] */
  public byte[] dareid;
  /** member variable representing the sequence member userid of type java.math.BigInteger */
  public java.math.BigInteger userid;
  /** member variable representing the sequence member groupid of type java.math.BigInteger */
  public java.math.BigInteger groupid;
  /** member variable representing the sequence member gifttype of type java.math.BigInteger */
  public java.math.BigInteger gifttype;

  /** default constructor */
  public ReqDareSendFlowers() {}

  /** copy constructor */
  public ReqDareSendFlowers (ReqDareSendFlowers arg) {
    dareid = new byte[arg.dareid.length];
    System.arraycopy(arg.dareid,0,dareid,0,arg.dareid.length);
    userid = arg.userid;
    groupid = arg.groupid;
    gifttype = arg.gifttype;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeOctetString(dareid);
    enc.encodeInteger(userid);
    enc.encodeInteger(groupid);
    enc.encodeInteger(gifttype);
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
    dareid = dec.decodeOctetString();
    userid = dec.decodeInteger();
    groupid = dec.decodeInteger();
    gifttype = dec.decodeInteger();
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
    os.print("dareid = ");
    os.print(Hex.toString(dareid));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("userid = ");
    os.print(userid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("groupid = ");
    os.print(groupid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("gifttype = ");
    os.print(gifttype.toString());
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