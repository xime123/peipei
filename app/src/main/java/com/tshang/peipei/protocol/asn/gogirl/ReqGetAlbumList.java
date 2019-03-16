//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqGetAlbumList.java
//
//   Java class for ASN.1 definition ReqGetAlbumList as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqGetAlbumList</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqGetAlbumList implements ASN1Type {

  /** member variable representing the sequence member selfuid of type java.math.BigInteger */
  public java.math.BigInteger selfuid;
  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member start of type java.math.BigInteger */
  public java.math.BigInteger start;
  /** member variable representing the sequence member num of type java.math.BigInteger */
  public java.math.BigInteger num;
  /** member variable representing the sequence member minaccess of type java.math.BigInteger */
  public java.math.BigInteger minaccess;
  /** member variable representing the sequence member maxaccess of type java.math.BigInteger */
  public java.math.BigInteger maxaccess;

  /** default constructor */
  public ReqGetAlbumList() {}

  /** copy constructor */
  public ReqGetAlbumList (ReqGetAlbumList arg) {
    selfuid = arg.selfuid;
    uid = arg.uid;
    start = arg.start;
    num = arg.num;
    minaccess = arg.minaccess;
    maxaccess = arg.maxaccess;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(selfuid);
    enc.encodeInteger(uid);
    enc.encodeInteger(start);
    enc.encodeInteger(num);
    enc.encodeInteger(minaccess);
    enc.encodeInteger(maxaccess);
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
    selfuid = dec.decodeInteger();
    uid = dec.decodeInteger();
    start = dec.decodeInteger();
    num = dec.decodeInteger();
    minaccess = dec.decodeInteger();
    maxaccess = dec.decodeInteger();
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
    os.print("selfuid = ");
    os.print(selfuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("uid = ");
    os.print(uid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("start = ");
    os.print(start.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("num = ");
    os.print(num.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("minaccess = ");
    os.print(minaccess.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("maxaccess = ");
    os.print(maxaccess.toString());
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
