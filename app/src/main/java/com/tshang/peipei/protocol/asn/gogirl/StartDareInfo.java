//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/StartDareInfo.java
//
//   Java class for ASN.1 definition StartDareInfo as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Wed Jun 17 18:16:32 2015
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>StartDareInfo</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Wed Jun 17 18:16:31 2015

  */

public class StartDareInfo implements ASN1Type {

  /** member variable representing the sequence member dareid of type byte[] */
  public byte[] dareid;
  /** member variable representing the sequence member groupid of type java.math.BigInteger */
  public java.math.BigInteger groupid;
  /** member variable representing the sequence member status of type java.math.BigInteger */
  public java.math.BigInteger status;
  /** member variable representing the sequence member starttime of type java.math.BigInteger */
  public java.math.BigInteger starttime;
  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;

  /** default constructor */
  public StartDareInfo() {}

  /** copy constructor */
  public StartDareInfo (StartDareInfo arg) {
    dareid = new byte[arg.dareid.length];
    System.arraycopy(arg.dareid,0,dareid,0,arg.dareid.length);
    groupid = arg.groupid;
    status = arg.status;
    starttime = arg.starttime;
    uid = arg.uid;
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_nr = enc.encodeSequence();
    enc.encodeOctetString(dareid);
    enc.encodeInteger(groupid);
    enc.encodeInteger(status);
    enc.encodeInteger(starttime);
    enc.encodeInteger(uid);
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
    dareid = dec.decodeOctetString();
    groupid = dec.decodeInteger();
    status = dec.decodeInteger();
    starttime = dec.decodeInteger();
    uid = dec.decodeInteger();
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
    os.print("dareid = ");
    os.print(Hex.toString(dareid));
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("groupid = ");
    os.print(groupid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("status = ");
    os.print(status.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("starttime = ");
    os.print(starttime.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("uid = ");
    os.print(uid.toString());
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
