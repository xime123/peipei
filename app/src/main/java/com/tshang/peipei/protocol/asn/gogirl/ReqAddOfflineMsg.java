//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqAddOfflineMsg.java
//
//   Java class for ASN.1 definition ReqAddOfflineMsg as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqAddOfflineMsg</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqAddOfflineMsg implements ASN1Type {

  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member offlinemsg of type GoGirlChatData */
  public GoGirlChatData offlinemsg = new GoGirlChatData();
  /** member variable representing the sequence member totoken of type byte[] */
  public byte[] totoken;
  /** member variable representing the sequence member tophoneos of type java.math.BigInteger */
  public java.math.BigInteger tophoneos;
  /** member variable representing the sequence member needofflinepush of type java.math.BigInteger */
  public java.math.BigInteger needofflinepush;

  /** default constructor */
  public ReqAddOfflineMsg() {}

  /** copy constructor */
  public ReqAddOfflineMsg (ReqAddOfflineMsg arg) {
    uid = arg.uid;
    offlinemsg = new GoGirlChatData(arg.offlinemsg);
    totoken = new byte[arg.totoken.length];
    System.arraycopy(arg.totoken,0,totoken,0,arg.totoken.length);
    tophoneos = arg.tophoneos;
    needofflinepush = arg.needofflinepush;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(uid);
    offlinemsg.encode(enc);
    enc.encodeOctetString(totoken);
    enc.encodeInteger(tophoneos);
    enc.encodeInteger(needofflinepush);
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
    uid = dec.decodeInteger();
    offlinemsg.decode(dec);
    totoken = dec.decodeOctetString();
    tophoneos = dec.decodeInteger();
    needofflinepush = dec.decodeInteger();
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
    os.print("uid = ");
    os.print(uid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("offlinemsg = ");
    offlinemsg.print(os, indent+2);
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("totoken = ");
    os.print(Hex.toString(totoken));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("tophoneos = ");
    os.print(tophoneos.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("needofflinepush = ");
    os.print(needofflinepush.toString());
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
