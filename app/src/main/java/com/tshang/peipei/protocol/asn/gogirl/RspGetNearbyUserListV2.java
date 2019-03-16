//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/RspGetNearbyUserListV2.java
//
//   Java class for ASN.1 definition RspGetNearbyUserListV2 as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>RspGetNearbyUserListV2</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class RspGetNearbyUserListV2 implements ASN1Type {

  /** member variable representing the sequence member retcode of type java.math.BigInteger */
  public java.math.BigInteger retcode;
  /** member variable representing the sequence member retmsg of type byte[] */
  public byte[] retmsg;
  /** member variable representing the sequence member nextdis of type java.math.BigInteger */
  public java.math.BigInteger nextdis;
  /** member variable representing the sequence member nextstart of type java.math.BigInteger */
  public java.math.BigInteger nextstart;
  /** member variable representing the sequence member userandskilllist of type UserAndSkillInfoList */
  public UserAndSkillInfoList userandskilllist = new UserAndSkillInfoList();

  /** default constructor */
  public RspGetNearbyUserListV2() {}

  /** copy constructor */
  public RspGetNearbyUserListV2 (RspGetNearbyUserListV2 arg) {
    retcode = arg.retcode;
    retmsg = new byte[arg.retmsg.length];
    System.arraycopy(arg.retmsg,0,retmsg,0,arg.retmsg.length);
    nextdis = arg.nextdis;
    nextstart = arg.nextstart;
    userandskilllist = new UserAndSkillInfoList(arg.userandskilllist);
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(retcode);
    enc.encodeOctetString(retmsg);
    enc.encodeInteger(nextdis);
    enc.encodeInteger(nextstart);
    userandskilllist.encode(enc);
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
    retcode = dec.decodeInteger();
    retmsg = dec.decodeOctetString();
    nextdis = dec.decodeInteger();
    nextstart = dec.decodeInteger();
    userandskilllist.decode(dec);
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
    os.print("retcode = ");
    os.print(retcode.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("retmsg = ");
    os.print(Hex.toString(retmsg));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("nextdis = ");
    os.print(nextdis.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("nextstart = ");
    os.print(nextstart.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("userandskilllist = ");
    userandskilllist.print(os, indent+2);
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
