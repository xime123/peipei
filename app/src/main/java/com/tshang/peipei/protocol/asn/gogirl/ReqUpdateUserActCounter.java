//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/ReqUpdateUserActCounter.java
//
//   Java class for ASN.1 definition ReqUpdateUserActCounter as defined in
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

/** This class represents the ASN.1 SEQUENCE type <tt>ReqUpdateUserActCounter</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class ReqUpdateUserActCounter implements ASN1Type {

  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member taskid of type java.math.BigInteger */
  public java.math.BigInteger taskid;
  /** member variable representing the sequence member actparamint of type java.math.BigInteger */
  public java.math.BigInteger actparamint;
  /** member variable representing the sequence member actparamstr of type byte[] */
  public byte[] actparamstr;

  /** default constructor */
  public ReqUpdateUserActCounter() {}

  /** copy constructor */
  public ReqUpdateUserActCounter (ReqUpdateUserActCounter arg) {
    uid = arg.uid;
    taskid = arg.taskid;
    actparamint = arg.actparamint;
    actparamstr = new byte[arg.actparamstr.length];
    System.arraycopy(arg.actparamstr,0,actparamstr,0,arg.actparamstr.length);
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
    enc.encodeInteger(taskid);
    enc.encodeInteger(actparamint);
    enc.encodeOctetString(actparamstr);
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
    taskid = dec.decodeInteger();
    actparamint = dec.decodeInteger();
    actparamstr = dec.decodeOctetString();
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
    os.print("taskid = ");
    os.print(taskid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("actparamint = ");
    os.print(actparamint.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("actparamstr = ");
    os.print(Hex.toString(actparamstr));
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