//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ShowRoomLatestStatus.java
//
//   Java class for ASN.1 definition ShowRoomLatestStatus as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ShowRoomLatestStatus</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ShowRoomLatestStatus implements ASN1Type {

  /** member variable representing the sequence member roomid of type java.math.BigInteger */
  public java.math.BigInteger roomid;
  /** member variable representing the sequence member owneruid of type java.math.BigInteger */
  public java.math.BigInteger owneruid;
  /** member variable representing the sequence member starttime of type java.math.BigInteger */
  public java.math.BigInteger starttime;
  /** member variable representing the sequence member endtime of type java.math.BigInteger */
  public java.math.BigInteger endtime;
  /** member variable representing the sequence member curmembernum of type java.math.BigInteger */
  public java.math.BigInteger curmembernum;
  /** member variable representing the sequence member maxmembernum of type java.math.BigInteger */
  public java.math.BigInteger maxmembernum;
  /** member variable representing the sequence member lefttime of type java.math.BigInteger */
  public java.math.BigInteger lefttime;
  /** member variable representing the sequence member hotnum of type java.math.BigInteger */
  public java.math.BigInteger hotnum;
  /** member variable representing the sequence member giftnum of type java.math.BigInteger */
  public java.math.BigInteger giftnum;
  /** member variable representing the sequence member activitynum of type java.math.BigInteger */
  public java.math.BigInteger activitynum;
  /** member variable representing the sequence member inoutuid of type java.math.BigInteger */
  public java.math.BigInteger inoutuid;
  /** member variable representing the sequence member inoutnick of type byte[] */
  public byte[] inoutnick;
  /** member variable representing the sequence member inoutsex of type java.math.BigInteger */
  public java.math.BigInteger inoutsex;
  /** member variable representing the sequence member inorout of type java.math.BigInteger */
  public java.math.BigInteger inorout;
  /** member variable representing the sequence member ridingid of type java.math.BigInteger */
  public java.math.BigInteger ridingid;
  /** member variable representing the sequence member ridingname of type byte[] */
  public byte[] ridingname;
  /** member variable representing the sequence member ridingurl of type byte[] */
  public byte[] ridingurl;
  /** member variable representing the sequence member addhotnick of type byte[] */
  public byte[] addhotnick;
  /** member variable representing the sequence member addhotuid of type java.math.BigInteger */
  public java.math.BigInteger addhotuid;
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
  public ShowRoomLatestStatus() {}

  /** copy constructor */
  public ShowRoomLatestStatus (ShowRoomLatestStatus arg) {
    roomid = arg.roomid;
    owneruid = arg.owneruid;
    starttime = arg.starttime;
    endtime = arg.endtime;
    curmembernum = arg.curmembernum;
    maxmembernum = arg.maxmembernum;
    lefttime = arg.lefttime;
    hotnum = arg.hotnum;
    giftnum = arg.giftnum;
    activitynum = arg.activitynum;
    inoutuid = arg.inoutuid;
    inoutnick = new byte[arg.inoutnick.length];
    System.arraycopy(arg.inoutnick,0,inoutnick,0,arg.inoutnick.length);
    inoutsex = arg.inoutsex;
    inorout = arg.inorout;
    ridingid = arg.ridingid;
    ridingname = new byte[arg.ridingname.length];
    System.arraycopy(arg.ridingname,0,ridingname,0,arg.ridingname.length);
    ridingurl = new byte[arg.ridingurl.length];
    System.arraycopy(arg.ridingurl,0,ridingurl,0,arg.ridingurl.length);
    addhotnick = new byte[arg.addhotnick.length];
    System.arraycopy(arg.addhotnick,0,addhotnick,0,arg.addhotnick.length);
    addhotuid = arg.addhotuid;
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
    enc.encodeInteger(roomid);
    enc.encodeInteger(owneruid);
    enc.encodeInteger(starttime);
    enc.encodeInteger(endtime);
    enc.encodeInteger(curmembernum);
    enc.encodeInteger(maxmembernum);
    enc.encodeInteger(lefttime);
    enc.encodeInteger(hotnum);
    enc.encodeInteger(giftnum);
    enc.encodeInteger(activitynum);
    enc.encodeInteger(inoutuid);
    enc.encodeOctetString(inoutnick);
    enc.encodeInteger(inoutsex);
    enc.encodeInteger(inorout);
    enc.encodeInteger(ridingid);
    enc.encodeOctetString(ridingname);
    enc.encodeOctetString(ridingurl);
    enc.encodeOctetString(addhotnick);
    enc.encodeInteger(addhotuid);
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
    roomid = dec.decodeInteger();
    owneruid = dec.decodeInteger();
    starttime = dec.decodeInteger();
    endtime = dec.decodeInteger();
    curmembernum = dec.decodeInteger();
    maxmembernum = dec.decodeInteger();
    lefttime = dec.decodeInteger();
    hotnum = dec.decodeInteger();
    giftnum = dec.decodeInteger();
    activitynum = dec.decodeInteger();
    inoutuid = dec.decodeInteger();
    inoutnick = dec.decodeOctetString();
    inoutsex = dec.decodeInteger();
    inorout = dec.decodeInteger();
    ridingid = dec.decodeInteger();
    ridingname = dec.decodeOctetString();
    ridingurl = dec.decodeOctetString();
    addhotnick = dec.decodeOctetString();
    addhotuid = dec.decodeInteger();
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
    os.print("roomid = ");
    os.print(roomid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("owneruid = ");
    os.print(owneruid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("starttime = ");
    os.print(starttime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("endtime = ");
    os.print(endtime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("curmembernum = ");
    os.print(curmembernum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("maxmembernum = ");
    os.print(maxmembernum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("lefttime = ");
    os.print(lefttime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("hotnum = ");
    os.print(hotnum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("giftnum = ");
    os.print(giftnum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("activitynum = ");
    os.print(activitynum.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("inoutuid = ");
    os.print(inoutuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("inoutnick = ");
    os.print(Hex.toString(inoutnick));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("inoutsex = ");
    os.print(inoutsex.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("inorout = ");
    os.print(inorout.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("ridingid = ");
    os.print(ridingid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("ridingname = ");
    os.print(Hex.toString(ridingname));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("ridingurl = ");
    os.print(Hex.toString(ridingurl));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("addhotnick = ");
    os.print(Hex.toString(addhotnick));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("addhotuid = ");
    os.print(addhotuid.toString());
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