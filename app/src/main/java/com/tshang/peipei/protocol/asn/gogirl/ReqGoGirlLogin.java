//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqGoGirlLogin.java
//
//   Java class for ASN.1 definition ReqGoGirlLogin as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqGoGirlLogin</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqGoGirlLogin implements ASN1Type {

  /** member variable representing the sequence member username of type byte[] */
  public byte[] username;
  /** member variable representing the sequence member verifystr of type byte[] */
  public byte[] verifystr;
  /** member variable representing the sequence member checksum of type byte[] */
  public byte[] checksum;
  /** member variable representing the sequence member imei of type byte[] */
  public byte[] imei;
  /** member variable representing the sequence member sdkver of type byte[] */
  public byte[] sdkver;
  /** member variable representing the sequence member phoneos of type java.math.BigInteger */
  public java.math.BigInteger phoneos;
  /** member variable representing the sequence member phonebrand of type byte[] */
  public byte[] phonebrand;
  /** member variable representing the sequence member appver of type java.math.BigInteger */
  public java.math.BigInteger appver;

  /** default constructor */
  public ReqGoGirlLogin() {}

  /** copy constructor */
  public ReqGoGirlLogin (ReqGoGirlLogin arg) {
    username = new byte[arg.username.length];
    System.arraycopy(arg.username,0,username,0,arg.username.length);
    verifystr = new byte[arg.verifystr.length];
    System.arraycopy(arg.verifystr,0,verifystr,0,arg.verifystr.length);
    checksum = new byte[arg.checksum.length];
    System.arraycopy(arg.checksum,0,checksum,0,arg.checksum.length);
    imei = new byte[arg.imei.length];
    System.arraycopy(arg.imei,0,imei,0,arg.imei.length);
    sdkver = new byte[arg.sdkver.length];
    System.arraycopy(arg.sdkver,0,sdkver,0,arg.sdkver.length);
    phoneos = arg.phoneos;
    phonebrand = new byte[arg.phonebrand.length];
    System.arraycopy(arg.phonebrand,0,phonebrand,0,arg.phonebrand.length);
    appver = arg.appver;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeOctetString(username);
    enc.encodeOctetString(verifystr);
    enc.encodeOctetString(checksum);
    enc.encodeOctetString(imei);
    enc.encodeOctetString(sdkver);
    enc.encodeInteger(phoneos);
    enc.encodeOctetString(phonebrand);
    enc.encodeInteger(appver);
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
    username = dec.decodeOctetString();
    verifystr = dec.decodeOctetString();
    checksum = dec.decodeOctetString();
    imei = dec.decodeOctetString();
    sdkver = dec.decodeOctetString();
    phoneos = dec.decodeInteger();
    phonebrand = dec.decodeOctetString();
    appver = dec.decodeInteger();
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
    os.print("username = ");
    os.print(Hex.toString(username));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("verifystr = ");
    os.print(Hex.toString(verifystr));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("checksum = ");
    os.print(Hex.toString(checksum));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("imei = ");
    os.print(Hex.toString(imei));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("sdkver = ");
    os.print(Hex.toString(sdkver));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("phoneos = ");
    os.print(phoneos.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("phonebrand = ");
    os.print(Hex.toString(phonebrand));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("appver = ");
    os.print(appver.toString());
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
