//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/RspGoGirlConnSvrV2.java
//
//   Java class for ASN.1 definition RspGoGirlConnSvrV2 as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>RspGoGirlConnSvrV2</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class RspGoGirlConnSvrV2 implements ASN1Type {

  /** member variable representing the sequence member retcode of type java.math.BigInteger */
  public java.math.BigInteger retcode;
  /** member variable representing the sequence member retmsg of type byte[] */
  public byte[] retmsg;
  /** member variable representing the sequence member userinfo of type GoGirlUserInfo */
  public GoGirlUserInfo userinfo = new GoGirlUserInfo();
  /** member variable representing the sequence member redirectip of type java.math.BigInteger */
  public java.math.BigInteger redirectip;
  /** member variable representing the sequence member redirectport of type java.math.BigInteger */
  public java.math.BigInteger redirectport;
  /** member variable representing the sequence member isfirstconn of type java.math.BigInteger */
  public java.math.BigInteger isfirstconn;
  /** member variable representing the sequence member loginrewards of type LoginRewardInfoList */
  public LoginRewardInfoList loginrewards = new LoginRewardInfoList();

  /** default constructor */
  public RspGoGirlConnSvrV2() {}

  /** copy constructor */
  public RspGoGirlConnSvrV2 (RspGoGirlConnSvrV2 arg) {
    retcode = arg.retcode;
    retmsg = new byte[arg.retmsg.length];
    System.arraycopy(arg.retmsg,0,retmsg,0,arg.retmsg.length);
    userinfo = new GoGirlUserInfo(arg.userinfo);
    redirectip = arg.redirectip;
    redirectport = arg.redirectport;
    isfirstconn = arg.isfirstconn;
    loginrewards = new LoginRewardInfoList(arg.loginrewards);
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
    userinfo.encode(enc);
    enc.encodeInteger(redirectip);
    enc.encodeInteger(redirectport);
    enc.encodeInteger(isfirstconn);
    loginrewards.encode(enc);
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
    userinfo.decode(dec);
    redirectip = dec.decodeInteger();
    redirectport = dec.decodeInteger();
    isfirstconn = dec.decodeInteger();
    loginrewards.decode(dec);
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
    os.print("userinfo = ");
    userinfo.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("redirectip = ");
    os.print(redirectip.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("redirectport = ");
    os.print(redirectport.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("isfirstconn = ");
    os.print(isfirstconn.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("loginrewards = ");
    loginrewards.print(os, indent+2);
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