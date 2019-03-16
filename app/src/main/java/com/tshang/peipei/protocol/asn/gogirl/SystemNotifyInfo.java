//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/SystemNotifyInfo.java
//
//   Java class for ASN.1 definition SystemNotifyInfo as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Tue Jan 26 18:38:16 2016
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>SystemNotifyInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class SystemNotifyInfo implements ASN1Type {

  /** member variable representing the sequence member createtime of type java.math.BigInteger */
  public java.math.BigInteger createtime;
  /** member variable representing the sequence member type of type java.math.BigInteger */
  public java.math.BigInteger type;
  /** member variable representing the sequence member title of type byte[] */
  public byte[] title;
  /** member variable representing the sequence member detail of type byte[] */
  public byte[] detail;
  /** member variable representing the sequence member sharedetail of type byte[] */
  public byte[] sharedetail;
  /** member variable representing the sequence member picurl of type byte[] */
  public byte[] picurl;
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
  /** member variable representing the sequence member revstr0 of type byte[] */
  public byte[] revstr0;
  /** member variable representing the sequence member revstr1 of type byte[] */
  public byte[] revstr1;
  /** member variable representing the sequence member revstr2 of type byte[] */
  public byte[] revstr2;
  /** member variable representing the sequence member revstr3 of type byte[] */
  public byte[] revstr3;
  /** member variable representing the sequence member revstr4 of type byte[] */
  public byte[] revstr4;

  /** default constructor */
  public SystemNotifyInfo() {}

  /** copy constructor */
  public SystemNotifyInfo (SystemNotifyInfo arg) {
    createtime = arg.createtime;
    type = arg.type;
    title = new byte[arg.title.length];
    System.arraycopy(arg.title,0,title,0,arg.title.length);
    detail = new byte[arg.detail.length];
    System.arraycopy(arg.detail,0,detail,0,arg.detail.length);
    sharedetail = new byte[arg.sharedetail.length];
    System.arraycopy(arg.sharedetail,0,sharedetail,0,arg.sharedetail.length);
    picurl = new byte[arg.picurl.length];
    System.arraycopy(arg.picurl,0,picurl,0,arg.picurl.length);
    revint0 = arg.revint0;
    revint1 = arg.revint1;
    revint2 = arg.revint2;
    revint3 = arg.revint3;
    revint4 = arg.revint4;
    revstr0 = new byte[arg.revstr0.length];
    System.arraycopy(arg.revstr0,0,revstr0,0,arg.revstr0.length);
    revstr1 = new byte[arg.revstr1.length];
    System.arraycopy(arg.revstr1,0,revstr1,0,arg.revstr1.length);
    revstr2 = new byte[arg.revstr2.length];
    System.arraycopy(arg.revstr2,0,revstr2,0,arg.revstr2.length);
    revstr3 = new byte[arg.revstr3.length];
    System.arraycopy(arg.revstr3,0,revstr3,0,arg.revstr3.length);
    revstr4 = new byte[arg.revstr4.length];
    System.arraycopy(arg.revstr4,0,revstr4,0,arg.revstr4.length);
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(createtime);
    enc.encodeInteger(type);
    enc.encodeOctetString(title);
    enc.encodeOctetString(detail);
    enc.encodeOctetString(sharedetail);
    enc.encodeOctetString(picurl);
    enc.encodeInteger(revint0);
    enc.encodeInteger(revint1);
    enc.encodeInteger(revint2);
    enc.encodeInteger(revint3);
    enc.encodeInteger(revint4);
    enc.encodeOctetString(revstr0);
    enc.encodeOctetString(revstr1);
    enc.encodeOctetString(revstr2);
    enc.encodeOctetString(revstr3);
    enc.encodeOctetString(revstr4);
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
    createtime = dec.decodeInteger();
    type = dec.decodeInteger();
    title = dec.decodeOctetString();
    detail = dec.decodeOctetString();
    sharedetail = dec.decodeOctetString();
    picurl = dec.decodeOctetString();
    revint0 = dec.decodeInteger();
    revint1 = dec.decodeInteger();
    revint2 = dec.decodeInteger();
    revint3 = dec.decodeInteger();
    revint4 = dec.decodeInteger();
    revstr0 = dec.decodeOctetString();
    revstr1 = dec.decodeOctetString();
    revstr2 = dec.decodeOctetString();
    revstr3 = dec.decodeOctetString();
    revstr4 = dec.decodeOctetString();
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
    os.print("createtime = ");
    os.print(createtime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("type = ");
    os.print(type.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("title = ");
    os.print(Hex.toString(title));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("detail = ");
    os.print(Hex.toString(detail));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("sharedetail = ");
    os.print(Hex.toString(sharedetail));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("picurl = ");
    os.print(Hex.toString(picurl));
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
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr0 = ");
    os.print(Hex.toString(revstr0));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr1 = ");
    os.print(Hex.toString(revstr1));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr2 = ");
    os.print(Hex.toString(revstr2));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr3 = ");
    os.print(Hex.toString(revstr3));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr4 = ");
    os.print(Hex.toString(revstr4));
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