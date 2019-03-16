//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/AboutMeReplyInfo.java
//
//   Java class for ASN.1 definition AboutMeReplyInfo as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>AboutMeReplyInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class AboutMeReplyInfo implements ASN1Type {

  /** member variable representing the sequence member type of type java.math.BigInteger */
  public java.math.BigInteger type;
  /** member variable representing the sequence member fromuid of type java.math.BigInteger */
  public java.math.BigInteger fromuid;
  /** member variable representing the sequence member topicuid of type java.math.BigInteger */
  public java.math.BigInteger topicuid;
  /** member variable representing the sequence member topicid of type java.math.BigInteger */
  public java.math.BigInteger topicid;
  /** member variable representing the sequence member globalid of type java.math.BigInteger */
  public java.math.BigInteger globalid;
  /** member variable representing the sequence member commentuid of type java.math.BigInteger */
  public java.math.BigInteger commentuid;
  /** member variable representing the sequence member auditstatus of type java.math.BigInteger */
  public java.math.BigInteger auditstatus;
  /** member variable representing the sequence member nick of type byte[] */
  public byte[] nick;
  /** member variable representing the sequence member color of type byte[] */
  public byte[] color;
  /** member variable representing the sequence member fonttype of type java.math.BigInteger */
  public java.math.BigInteger fonttype;
  /** member variable representing the sequence member headpickey of type byte[] */
  public byte[] headpickey;
  /** member variable representing the sequence member sex of type java.math.BigInteger */
  public java.math.BigInteger sex;
  /** member variable representing the sequence member createtime of type java.math.BigInteger */
  public java.math.BigInteger createtime;
  /** member variable representing the sequence member commentcontentlist of type GoGirlDataInfoList */
  public GoGirlDataInfoList commentcontentlist = new GoGirlDataInfoList();
  /** member variable representing the sequence member dynamicscontentlist of type GoGirlDataInfoList */
  public GoGirlDataInfoList dynamicscontentlist = new GoGirlDataInfoList();
  /** member variable representing the sequence member imei of type byte[] */
  public byte[] imei;
  /** member variable representing the sequence member revint0 of type java.math.BigInteger */
  public java.math.BigInteger revint0;
  /** member variable representing the sequence member revint1 of type java.math.BigInteger */
  public java.math.BigInteger revint1;
  /** member variable representing the sequence member revint2 of type java.math.BigInteger */
  public java.math.BigInteger revint2;
  /** member variable representing the sequence member revstr0 of type byte[] */
  public byte[] revstr0;
  /** member variable representing the sequence member revstr1 of type byte[] */
  public byte[] revstr1;
  /** member variable representing the sequence member revstr2 of type byte[] */
  public byte[] revstr2;

  /** default constructor */
  public AboutMeReplyInfo() {}

  /** copy constructor */
  public AboutMeReplyInfo (AboutMeReplyInfo arg) {
    type = arg.type;
    fromuid = arg.fromuid;
    topicuid = arg.topicuid;
    topicid = arg.topicid;
    globalid = arg.globalid;
    commentuid = arg.commentuid;
    auditstatus = arg.auditstatus;
    nick = new byte[arg.nick.length];
    System.arraycopy(arg.nick,0,nick,0,arg.nick.length);
    color = new byte[arg.color.length];
    System.arraycopy(arg.color,0,color,0,arg.color.length);
    fonttype = arg.fonttype;
    headpickey = new byte[arg.headpickey.length];
    System.arraycopy(arg.headpickey,0,headpickey,0,arg.headpickey.length);
    sex = arg.sex;
    createtime = arg.createtime;
    commentcontentlist = new GoGirlDataInfoList(arg.commentcontentlist);
    dynamicscontentlist = new GoGirlDataInfoList(arg.dynamicscontentlist);
    imei = new byte[arg.imei.length];
    System.arraycopy(arg.imei,0,imei,0,arg.imei.length);
    revint0 = arg.revint0;
    revint1 = arg.revint1;
    revint2 = arg.revint2;
    revstr0 = new byte[arg.revstr0.length];
    System.arraycopy(arg.revstr0,0,revstr0,0,arg.revstr0.length);
    revstr1 = new byte[arg.revstr1.length];
    System.arraycopy(arg.revstr1,0,revstr1,0,arg.revstr1.length);
    revstr2 = new byte[arg.revstr2.length];
    System.arraycopy(arg.revstr2,0,revstr2,0,arg.revstr2.length);
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
    enc.encodeInteger(fromuid);
    enc.encodeInteger(topicuid);
    enc.encodeInteger(topicid);
    enc.encodeInteger(globalid);
    enc.encodeInteger(commentuid);
    enc.encodeInteger(auditstatus);
    enc.encodeOctetString(nick);
    enc.encodeOctetString(color);
    enc.encodeInteger(fonttype);
    enc.encodeOctetString(headpickey);
    enc.encodeInteger(sex);
    enc.encodeInteger(createtime);
    commentcontentlist.encode(enc);
    dynamicscontentlist.encode(enc);
    enc.encodeOctetString(imei);
    enc.encodeInteger(revint0);
    enc.encodeInteger(revint1);
    enc.encodeInteger(revint2);
    enc.encodeOctetString(revstr0);
    enc.encodeOctetString(revstr1);
    enc.encodeOctetString(revstr2);
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
    fromuid = dec.decodeInteger();
    topicuid = dec.decodeInteger();
    topicid = dec.decodeInteger();
    globalid = dec.decodeInteger();
    commentuid = dec.decodeInteger();
    auditstatus = dec.decodeInteger();
    nick = dec.decodeOctetString();
    color = dec.decodeOctetString();
    fonttype = dec.decodeInteger();
    headpickey = dec.decodeOctetString();
    sex = dec.decodeInteger();
    createtime = dec.decodeInteger();
    commentcontentlist.decode(dec);
    dynamicscontentlist.decode(dec);
    imei = dec.decodeOctetString();
    revint0 = dec.decodeInteger();
    revint1 = dec.decodeInteger();
    revint2 = dec.decodeInteger();
    revstr0 = dec.decodeOctetString();
    revstr1 = dec.decodeOctetString();
    revstr2 = dec.decodeOctetString();
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
    os.print("fromuid = ");
    os.print(fromuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("topicuid = ");
    os.print(topicuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("topicid = ");
    os.print(topicid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("globalid = ");
    os.print(globalid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("commentuid = ");
    os.print(commentuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("auditstatus = ");
    os.print(auditstatus.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("nick = ");
    os.print(Hex.toString(nick));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("color = ");
    os.print(Hex.toString(color));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fonttype = ");
    os.print(fonttype.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("headpickey = ");
    os.print(Hex.toString(headpickey));
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
    os.print("commentcontentlist = ");
    commentcontentlist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("dynamicscontentlist = ");
    dynamicscontentlist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("imei = ");
    os.print(Hex.toString(imei));
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
