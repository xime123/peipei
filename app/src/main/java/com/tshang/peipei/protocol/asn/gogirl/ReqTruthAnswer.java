//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqTruthAnswer.java
//
//   Java class for ASN.1 definition ReqTruthAnswer as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqTruthAnswer</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqTruthAnswer implements ASN1Type {

  /** member variable representing the sequence member touid of type java.math.BigInteger */
  public java.math.BigInteger touid;
  /** member variable representing the sequence member answerid of type java.math.BigInteger */
  public java.math.BigInteger answerid;
  /** member variable representing the sequence member fromuid of type java.math.BigInteger */
  public java.math.BigInteger fromuid;
  /** member variable representing the sequence member truthid of type byte[] */
  public byte[] truthid;

  /** default constructor */
  public ReqTruthAnswer() {}

  /** copy constructor */
  public ReqTruthAnswer (ReqTruthAnswer arg) {
    touid = arg.touid;
    answerid = arg.answerid;
    fromuid = arg.fromuid;
    truthid = new byte[arg.truthid.length];
    System.arraycopy(arg.truthid,0,truthid,0,arg.truthid.length);
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeInteger(touid);
    enc.encodeInteger(answerid);
    enc.encodeInteger(fromuid);
    enc.encodeOctetString(truthid);
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
    touid = dec.decodeInteger();
    answerid = dec.decodeInteger();
    fromuid = dec.decodeInteger();
    truthid = dec.decodeOctetString();
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
    os.print("touid = ");
    os.print(touid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("answerid = ");
    os.print(answerid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("fromuid = ");
    os.print(fromuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("truthid = ");
    os.print(Hex.toString(truthid));
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
