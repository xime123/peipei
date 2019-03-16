//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/CommentInfo.java
//
//   Java class for ASN.1 definition CommentInfo as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>CommentInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class CommentInfo implements ASN1Type {

  /** member variable representing the sequence member id of type java.math.BigInteger */
  public java.math.BigInteger id;
  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member nick of type byte[] */
  public byte[] nick;
  /** member variable representing the sequence member sex of type java.math.BigInteger */
  public java.math.BigInteger sex;
  /** member variable representing the sequence member createtime of type java.math.BigInteger */
  public java.math.BigInteger createtime;
  /** member variable representing the sequence member commentstatus of type java.math.BigInteger */
  public java.math.BigInteger commentstatus;
  /** member variable representing the sequence member commentcontentlist of type GoGirlDataInfoList */
  public GoGirlDataInfoList commentcontentlist = new GoGirlDataInfoList();
  /** member variable representing the sequence member imei of type byte[] */
  public byte[] imei;
  /** member variable representing the sequence member province of type byte[] */
  public byte[] province;
  /** member variable representing the sequence member city of type byte[] */
  public byte[] city;
  /** member variable representing the sequence member detailaddr of type byte[] */
  public byte[] detailaddr;
  /** member variable representing the sequence member replylist of type CommentInfoList */
  public CommentInfoList replylist = new CommentInfoList();
  /** member variable representing the sequence member updown of type java.math.BigInteger */
  public java.math.BigInteger updown;
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
  /** member variable representing the sequence member revstr5 of type byte[] */
  public byte[] revstr5;
  /** member variable representing the sequence member revstr6 of type byte[] */
  public byte[] revstr6;
  /** member variable representing the sequence member revstr7 of type byte[] */
  public byte[] revstr7;
  /** member variable representing the sequence member revstr8 of type byte[] */
  public byte[] revstr8;
  /** member variable representing the sequence member revstr9 of type byte[] */
  public byte[] revstr9;

  /** default constructor */
  public CommentInfo() {}

  /** copy constructor */
  public CommentInfo (CommentInfo arg) {
    id = arg.id;
    uid = arg.uid;
    nick = new byte[arg.nick.length];
    System.arraycopy(arg.nick,0,nick,0,arg.nick.length);
    sex = arg.sex;
    createtime = arg.createtime;
    commentstatus = arg.commentstatus;
    commentcontentlist = new GoGirlDataInfoList(arg.commentcontentlist);
    imei = new byte[arg.imei.length];
    System.arraycopy(arg.imei,0,imei,0,arg.imei.length);
    province = new byte[arg.province.length];
    System.arraycopy(arg.province,0,province,0,arg.province.length);
    city = new byte[arg.city.length];
    System.arraycopy(arg.city,0,city,0,arg.city.length);
    detailaddr = new byte[arg.detailaddr.length];
    System.arraycopy(arg.detailaddr,0,detailaddr,0,arg.detailaddr.length);
    replylist = new CommentInfoList(arg.replylist);
    updown = arg.updown;
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
    revstr5 = new byte[arg.revstr5.length];
    System.arraycopy(arg.revstr5,0,revstr5,0,arg.revstr5.length);
    revstr6 = new byte[arg.revstr6.length];
    System.arraycopy(arg.revstr6,0,revstr6,0,arg.revstr6.length);
    revstr7 = new byte[arg.revstr7.length];
    System.arraycopy(arg.revstr7,0,revstr7,0,arg.revstr7.length);
    revstr8 = new byte[arg.revstr8.length];
    System.arraycopy(arg.revstr8,0,revstr8,0,arg.revstr8.length);
    revstr9 = new byte[arg.revstr9.length];
    System.arraycopy(arg.revstr9,0,revstr9,0,arg.revstr9.length);
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(id);
    enc.encodeInteger(uid);
    enc.encodeOctetString(nick);
    enc.encodeInteger(sex);
    enc.encodeInteger(createtime);
    enc.encodeInteger(commentstatus);
    commentcontentlist.encode(enc);
    enc.encodeOctetString(imei);
    enc.encodeOctetString(province);
    enc.encodeOctetString(city);
    enc.encodeOctetString(detailaddr);
    replylist.encode(enc);
    enc.encodeInteger(updown);
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
    enc.encodeOctetString(revstr5);
    enc.encodeOctetString(revstr6);
    enc.encodeOctetString(revstr7);
    enc.encodeOctetString(revstr8);
    enc.encodeOctetString(revstr9);
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
    id = dec.decodeInteger();
    uid = dec.decodeInteger();
    nick = dec.decodeOctetString();
    sex = dec.decodeInteger();
    createtime = dec.decodeInteger();
    commentstatus = dec.decodeInteger();
    commentcontentlist.decode(dec);
    imei = dec.decodeOctetString();
    province = dec.decodeOctetString();
    city = dec.decodeOctetString();
    detailaddr = dec.decodeOctetString();
    replylist.decode(dec);
    updown = dec.decodeInteger();
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
    revstr5 = dec.decodeOctetString();
    revstr6 = dec.decodeOctetString();
    revstr7 = dec.decodeOctetString();
    revstr8 = dec.decodeOctetString();
    revstr9 = dec.decodeOctetString();
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
    os.print("id = ");
    os.print(id.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("uid = ");
    os.print(uid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("nick = ");
    os.print(Hex.toString(nick));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("sex = ");
    os.print(sex.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("createtime = ");
    os.print(createtime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("commentstatus = ");
    os.print(commentstatus.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("commentcontentlist = ");
    commentcontentlist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("imei = ");
    os.print(Hex.toString(imei));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("province = ");
    os.print(Hex.toString(province));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("city = ");
    os.print(Hex.toString(city));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("detailaddr = ");
    os.print(Hex.toString(detailaddr));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("replylist = ");
    replylist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("updown = ");
    os.print(updown.toString());
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
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr5 = ");
    os.print(Hex.toString(revstr5));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr6 = ");
    os.print(Hex.toString(revstr6));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr7 = ");
    os.print(Hex.toString(revstr7));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr8 = ");
    os.print(Hex.toString(revstr8));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revstr9 = ");
    os.print(Hex.toString(revstr9));
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
