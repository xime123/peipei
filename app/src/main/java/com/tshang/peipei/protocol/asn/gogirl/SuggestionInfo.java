//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/SuggestionInfo.java
//
//   Java class for ASN.1 definition SuggestionInfo as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>SuggestionInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class SuggestionInfo implements ASN1Type {

  /** member variable representing the sequence member id of type java.math.BigInteger */
  public java.math.BigInteger id;
  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member nick of type byte[] */
  public byte[] nick;
  /** member variable representing the sequence member from of type byte[] */
  public byte[] from;
  /** member variable representing the sequence member content of type byte[] */
  public byte[] content;
  /** member variable representing the sequence member createtime of type java.math.BigInteger */
  public java.math.BigInteger createtime;
  /** member variable representing the sequence member reply of type byte[] */
  public byte[] reply;
  /** member variable representing the sequence member replyname of type byte[] */
  public byte[] replyname;
  /** member variable representing the sequence member replytime of type java.math.BigInteger */
  public java.math.BigInteger replytime;
  /** member variable representing the sequence member memo of type byte[] */
  public byte[] memo;
  /** member variable representing the sequence member revint1 of type java.math.BigInteger */
  public java.math.BigInteger revint1;
  /** member variable representing the sequence member revint2 of type java.math.BigInteger */
  public java.math.BigInteger revint2;
  /** member variable representing the sequence member revstr1 of type byte[] */
  public byte[] revstr1;
  /** member variable representing the sequence member revstr2 of type byte[] */
  public byte[] revstr2;
  /** member variable representing the sequence member revstr3 of type byte[] */
  public byte[] revstr3;
  /** member variable representing the sequence member revstr4 of type byte[] */
  public byte[] revstr4;

  /** default constructor */
  public SuggestionInfo() {}

  /** copy constructor */
  public SuggestionInfo (SuggestionInfo arg) {
    id = arg.id;
    uid = arg.uid;
    nick = new byte[arg.nick.length];
    System.arraycopy(arg.nick,0,nick,0,arg.nick.length);
    from = new byte[arg.from.length];
    System.arraycopy(arg.from,0,from,0,arg.from.length);
    content = new byte[arg.content.length];
    System.arraycopy(arg.content,0,content,0,arg.content.length);
    createtime = arg.createtime;
    reply = new byte[arg.reply.length];
    System.arraycopy(arg.reply,0,reply,0,arg.reply.length);
    replyname = new byte[arg.replyname.length];
    System.arraycopy(arg.replyname,0,replyname,0,arg.replyname.length);
    replytime = arg.replytime;
    memo = new byte[arg.memo.length];
    System.arraycopy(arg.memo,0,memo,0,arg.memo.length);
    revint1 = arg.revint1;
    revint2 = arg.revint2;
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
    enc.encodeInteger(id);
    enc.encodeInteger(uid);
    enc.encodeOctetString(nick);
    enc.encodeOctetString(from);
    enc.encodeOctetString(content);
    enc.encodeInteger(createtime);
    enc.encodeOctetString(reply);
    enc.encodeOctetString(replyname);
    enc.encodeInteger(replytime);
    enc.encodeOctetString(memo);
    enc.encodeInteger(revint1);
    enc.encodeInteger(revint2);
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
    id = dec.decodeInteger();
    uid = dec.decodeInteger();
    nick = dec.decodeOctetString();
    from = dec.decodeOctetString();
    content = dec.decodeOctetString();
    createtime = dec.decodeInteger();
    reply = dec.decodeOctetString();
    replyname = dec.decodeOctetString();
    replytime = dec.decodeInteger();
    memo = dec.decodeOctetString();
    revint1 = dec.decodeInteger();
    revint2 = dec.decodeInteger();
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
    os.print("from = ");
    os.print(Hex.toString(from));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("content = ");
    os.print(Hex.toString(content));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("createtime = ");
    os.print(createtime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("reply = ");
    os.print(Hex.toString(reply));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("replyname = ");
    os.print(Hex.toString(replyname));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("replytime = ");
    os.print(replytime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("memo = ");
    os.print(Hex.toString(memo));
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
