//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/GoGirlChatData.java
//
//   Java class for ASN.1 definition GoGirlChatData as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>GoGirlChatData</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class GoGirlChatData implements ASN1Type {

  /** member variable representing the sequence member from of type java.math.BigInteger */
  public java.math.BigInteger from;
  /** member variable representing the sequence member fromtype of type java.math.BigInteger */
  public java.math.BigInteger fromtype;
  /** member variable representing the sequence member fromnick of type byte[] */
  public byte[] fromnick;
  /** member variable representing the sequence member fromsex of type java.math.BigInteger */
  public java.math.BigInteger fromsex;
  /** member variable representing the sequence member to of type java.math.BigInteger */
  public java.math.BigInteger to;
  /** member variable representing the sequence member totype of type java.math.BigInteger */
  public java.math.BigInteger totype;
  /** member variable representing the sequence member tonick of type byte[] */
  public byte[] tonick;
  /** member variable representing the sequence member tosex of type java.math.BigInteger */
  public java.math.BigInteger tosex;
  /** member variable representing the sequence member createtimes of type java.math.BigInteger */
  public java.math.BigInteger createtimes;
  /** member variable representing the sequence member createtimeus of type java.math.BigInteger */
  public java.math.BigInteger createtimeus;
  /** member variable representing the sequence member chatdatalist of type GoGirlDataInfoList */
  public GoGirlDataInfoList chatdatalist = new GoGirlDataInfoList();
  /** member variable representing the sequence member revint0 of type java.math.BigInteger */
  public java.math.BigInteger revint0;
  /** member variable representing the sequence member revint1 of type java.math.BigInteger */
  public java.math.BigInteger revint1;
  /** member variable representing the sequence member revint2 of type java.math.BigInteger */
  public java.math.BigInteger revint2;
  /** member variable representing the sequence member revint3 of type java.math.BigInteger */
  public java.math.BigInteger revint3;
  /** member variable representing the sequence member revstr0 of type byte[] */
  public byte[] revstr0;
  /** member variable representing the sequence member revstr1 of type byte[] */
  public byte[] revstr1;
  /** member variable representing the sequence member revstr2 of type byte[] */
  public byte[] revstr2;
  /** member variable representing the sequence member revstr3 of type byte[] */
  public byte[] revstr3;

  /** default constructor */
  public GoGirlChatData() {}

  /** copy constructor */
  public GoGirlChatData (GoGirlChatData arg) {
    from = arg.from;
    fromtype = arg.fromtype;
    fromnick = new byte[arg.fromnick.length];
    System.arraycopy(arg.fromnick,0,fromnick,0,arg.fromnick.length);
    fromsex = arg.fromsex;
    to = arg.to;
    totype = arg.totype;
    tonick = new byte[arg.tonick.length];
    System.arraycopy(arg.tonick,0,tonick,0,arg.tonick.length);
    tosex = arg.tosex;
    createtimes = arg.createtimes;
    createtimeus = arg.createtimeus;
    chatdatalist = new GoGirlDataInfoList(arg.chatdatalist);
    revint0 = arg.revint0;
    revint1 = arg.revint1;
    revint2 = arg.revint2;
    revint3 = arg.revint3;
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
    enc.encodeInteger(from);
    enc.encodeInteger(fromtype);
    enc.encodeOctetString(fromnick);
    enc.encodeInteger(fromsex);
    enc.encodeInteger(to);
    enc.encodeInteger(totype);
    enc.encodeOctetString(tonick);
    enc.encodeInteger(tosex);
    enc.encodeInteger(createtimes);
    enc.encodeInteger(createtimeus);
    chatdatalist.encode(enc);
    enc.encodeInteger(revint0);
    enc.encodeInteger(revint1);
    enc.encodeInteger(revint2);
    enc.encodeInteger(revint3);
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
    from = dec.decodeInteger();
    fromtype = dec.decodeInteger();
    fromnick = dec.decodeOctetString();
    fromsex = dec.decodeInteger();
    to = dec.decodeInteger();
    totype = dec.decodeInteger();
    tonick = dec.decodeOctetString();
    tosex = dec.decodeInteger();
    createtimes = dec.decodeInteger();
    createtimeus = dec.decodeInteger();
    chatdatalist.decode(dec);
    revint0 = dec.decodeInteger();
    revint1 = dec.decodeInteger();
    revint2 = dec.decodeInteger();
    revint3 = dec.decodeInteger();
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
    os.print("from = ");
    os.print(from.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fromtype = ");
    os.print(fromtype.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fromnick = ");
    os.print(Hex.toString(fromnick));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fromsex = ");
    os.print(fromsex.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("to = ");
    os.print(to.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("totype = ");
    os.print(totype.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("tonick = ");
    os.print(Hex.toString(tonick));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("tosex = ");
    os.print(tosex.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("createtimes = ");
    os.print(createtimes.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("createtimeus = ");
    os.print(createtimeus.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("chatdatalist = ");
    chatdatalist.print(os, indent+2);
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
