//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/ydmx/peipei/ans/gogirl/ReqDelateComment.java
//
//   Java class for ASN.1 definition ReqDelateComment as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Fri May 09 17:40:35 2014
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

import com.ibm.asn1.ASN1Decoder;
import com.ibm.asn1.ASN1Encoder;
import com.ibm.asn1.ASN1Exception;
// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.ASN1Type;
// Import ASN.1 basic type representations

/** This class represents the ASN.1 SEQUENCE type <tt>ReqDelateComment</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Fri May 09 17:40:34 2014

  */

public class ReqDelateComment implements ASN1Type {

  /** member variable representing the sequence member uid of type java.math.BigInteger */
  public java.math.BigInteger uid;
  /** member variable representing the sequence member topicuid of type java.math.BigInteger */
  public java.math.BigInteger topicuid;
  /** member variable representing the sequence member topicid of type java.math.BigInteger */
  public java.math.BigInteger topicid;
  /** member variable representing the sequence member commentid of type java.math.BigInteger */
  public java.math.BigInteger commentid;
  /** member variable representing the sequence member replyid of type java.math.BigInteger */
  public java.math.BigInteger replyid;
  /** member variable representing the sequence member illegaltype of type java.math.BigInteger */
  public java.math.BigInteger illegaltype;

  /** default constructor */
  public ReqDelateComment() {}

  /** copy constructor */
  public ReqDelateComment (ReqDelateComment arg) {
    uid = arg.uid;
    topicuid = arg.topicuid;
    topicid = arg.topicid;
    commentid = arg.commentid;
    replyid = arg.replyid;
    illegaltype = arg.illegaltype;
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
    enc.encodeInteger(topicuid);
    enc.encodeInteger(topicid);
    enc.encodeInteger(commentid);
    enc.encodeInteger(replyid);
    enc.encodeInteger(illegaltype);
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
    topicuid = dec.decodeInteger();
    topicid = dec.decodeInteger();
    commentid = dec.decodeInteger();
    replyid = dec.decodeInteger();
    illegaltype = dec.decodeInteger();
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
    os.print("topicuid = ");
    os.print(topicuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("topicid = ");
    os.print(topicid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("commentid = ");
    os.print(commentid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("replyid = ");
    os.print(replyid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("illegaltype = ");
    os.print(illegaltype.toString());
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
