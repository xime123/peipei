//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/DareInfo.java
//
//   Java class for ASN.1 definition DareInfo as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>DareInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class DareInfo implements ASN1Type {

  /** member variable representing the sequence member id of type java.math.BigInteger */
  public java.math.BigInteger id;
  /** member variable representing the sequence member globalid of type byte[] */
  public byte[] globalid;
  /** member variable representing the sequence member joinuserlist of type JoinUserList */
  public JoinUserList joinuserlist = new JoinUserList();
  /** member variable representing the sequence member loseruserlist of type LoserUserList */
  public LoserUserList loseruserlist = new LoserUserList();
  /** member variable representing the sequence member creattime of type java.math.BigInteger */
  public java.math.BigInteger creattime;
  /** member variable representing the sequence member memo of type byte[] */
  public byte[] memo;
  /** member variable representing the sequence member fine of type java.math.BigInteger */
  public java.math.BigInteger fine;
  /** member variable representing the sequence member placetag of type java.math.BigInteger */
  public java.math.BigInteger placetag;
  /** member variable representing the sequence member groupid of type java.math.BigInteger */
  public java.math.BigInteger groupid;
  /** member variable representing the sequence member size of type java.math.BigInteger */
  public java.math.BigInteger size;
  /** member variable representing the sequence member gameover of type java.math.BigInteger */
  public java.math.BigInteger gameover;
  /** member variable representing the sequence member status of type java.math.BigInteger */
  public java.math.BigInteger status;
  /** member variable representing the sequence member revint1 of type java.math.BigInteger */
  public java.math.BigInteger revint1;
  /** member variable representing the sequence member revint2 of type java.math.BigInteger */
  public java.math.BigInteger revint2;
  /** member variable representing the sequence member revint3 of type java.math.BigInteger */
  public java.math.BigInteger revint3;
  /** member variable representing the sequence member revint4 of type java.math.BigInteger */
  public java.math.BigInteger revint4;
  /** member variable representing the sequence member revint5 of type java.math.BigInteger */
  public java.math.BigInteger revint5;
  /** member variable representing the sequence member revint6 of type java.math.BigInteger */
  public java.math.BigInteger revint6;
  /** member variable representing the sequence member revint7 of type java.math.BigInteger */
  public java.math.BigInteger revint7;
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

  /** default constructor */
  public DareInfo() {}

  /** copy constructor */
  public DareInfo (DareInfo arg) {
    id = arg.id;
    globalid = new byte[arg.globalid.length];
    System.arraycopy(arg.globalid,0,globalid,0,arg.globalid.length);
    joinuserlist = new JoinUserList(arg.joinuserlist);
    loseruserlist = new LoserUserList(arg.loseruserlist);
    creattime = arg.creattime;
    memo = new byte[arg.memo.length];
    System.arraycopy(arg.memo,0,memo,0,arg.memo.length);
    fine = arg.fine;
    placetag = arg.placetag;
    groupid = arg.groupid;
    size = arg.size;
    gameover = arg.gameover;
    status = arg.status;
    revint1 = arg.revint1;
    revint2 = arg.revint2;
    revint3 = arg.revint3;
    revint4 = arg.revint4;
    revint5 = arg.revint5;
    revint6 = arg.revint6;
    revint7 = arg.revint7;
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
    enc.encodeOctetString(globalid);
    joinuserlist.encode(enc);
    loseruserlist.encode(enc);
    enc.encodeInteger(creattime);
    enc.encodeOctetString(memo);
    enc.encodeInteger(fine);
    enc.encodeInteger(placetag);
    enc.encodeInteger(groupid);
    enc.encodeInteger(size);
    enc.encodeInteger(gameover);
    enc.encodeInteger(status);
    enc.encodeInteger(revint1);
    enc.encodeInteger(revint2);
    enc.encodeInteger(revint3);
    enc.encodeInteger(revint4);
    enc.encodeInteger(revint5);
    enc.encodeInteger(revint6);
    enc.encodeInteger(revint7);
    enc.encodeOctetString(revstr0);
    enc.encodeOctetString(revstr1);
    enc.encodeOctetString(revstr2);
    enc.encodeOctetString(revstr3);
    enc.encodeOctetString(revstr4);
    enc.encodeOctetString(revstr5);
    enc.encodeOctetString(revstr6);
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
    globalid = dec.decodeOctetString();
    joinuserlist.decode(dec);
    loseruserlist.decode(dec);
    creattime = dec.decodeInteger();
    memo = dec.decodeOctetString();
    fine = dec.decodeInteger();
    placetag = dec.decodeInteger();
    groupid = dec.decodeInteger();
    size = dec.decodeInteger();
    gameover = dec.decodeInteger();
    status = dec.decodeInteger();
    revint1 = dec.decodeInteger();
    revint2 = dec.decodeInteger();
    revint3 = dec.decodeInteger();
    revint4 = dec.decodeInteger();
    revint5 = dec.decodeInteger();
    revint6 = dec.decodeInteger();
    revint7 = dec.decodeInteger();
    revstr0 = dec.decodeOctetString();
    revstr1 = dec.decodeOctetString();
    revstr2 = dec.decodeOctetString();
    revstr3 = dec.decodeOctetString();
    revstr4 = dec.decodeOctetString();
    revstr5 = dec.decodeOctetString();
    revstr6 = dec.decodeOctetString();
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
    os.print("globalid = ");
    os.print(Hex.toString(globalid));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("joinuserlist = ");
    joinuserlist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("loseruserlist = ");
    loseruserlist.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("creattime = ");
    os.print(creattime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("memo = ");
    os.print(Hex.toString(memo));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fine = ");
    os.print(fine.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("placetag = ");
    os.print(placetag.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("groupid = ");
    os.print(groupid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("size = ");
    os.print(size.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("gameover = ");
    os.print(gameover.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("status = ");
    os.print(status.toString());
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
    os.print("revint5 = ");
    os.print(revint5.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint6 = ");
    os.print(revint6.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("revint7 = ");
    os.print(revint7.toString());
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
