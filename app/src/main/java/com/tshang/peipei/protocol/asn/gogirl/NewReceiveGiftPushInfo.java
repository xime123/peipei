//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/NewReceiveGiftPushInfo.java
//
//   Java class for ASN.1 definition NewReceiveGiftPushInfo as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>NewReceiveGiftPushInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class NewReceiveGiftPushInfo implements ASN1Type {

  /** member variable representing the sequence member createtime of type java.math.BigInteger */
  public java.math.BigInteger createtime;
  /** member variable representing the sequence member deliveruid of type java.math.BigInteger */
  public java.math.BigInteger deliveruid;
  /** member variable representing the sequence member giftdeallist of type GiftDealInfoList */
  public GiftDealInfoList giftdeallist = new GiftDealInfoList();
  /** member variable representing the sequence member series of type java.math.BigInteger */
  public java.math.BigInteger series;
  /** member variable representing the sequence member boxnum of type java.math.BigInteger */
  public java.math.BigInteger boxnum;
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

  /** default constructor */
  public NewReceiveGiftPushInfo() {}

  /** copy constructor */
  public NewReceiveGiftPushInfo (NewReceiveGiftPushInfo arg) {
    createtime = arg.createtime;
    deliveruid = arg.deliveruid;
    giftdeallist = new GiftDealInfoList(arg.giftdeallist);
    series = arg.series;
    boxnum = arg.boxnum;
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
    enc.encodeInteger(deliveruid);
    giftdeallist.encode(enc);
    enc.encodeInteger(series);
    enc.encodeInteger(boxnum);
    enc.encodeInteger(revint2);
    enc.encodeInteger(revint3);
    enc.encodeInteger(revint4);
    enc.encodeOctetString(revstr0);
    enc.encodeOctetString(revstr1);
    enc.encodeOctetString(revstr2);
    enc.encodeOctetString(revstr3);
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
    deliveruid = dec.decodeInteger();
    giftdeallist.decode(dec);
    series = dec.decodeInteger();
    boxnum = dec.decodeInteger();
    revint2 = dec.decodeInteger();
    revint3 = dec.decodeInteger();
    revint4 = dec.decodeInteger();
    revstr0 = dec.decodeOctetString();
    revstr1 = dec.decodeOctetString();
    revstr2 = dec.decodeOctetString();
    revstr3 = dec.decodeOctetString();
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
    os.print("deliveruid = ");
    os.print(deliveruid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("giftdeallist = ");
    giftdeallist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("series = ");
    os.print(series.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("boxnum = ");
    os.print(boxnum.toString());
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