//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/asn/gogirl/ReqGoGirlShowChat.java
//
//   Java class for ASN.1 definition ReqGoGirlShowChat as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Fri Jan 16 19:31:21 2015
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>ReqGoGirlShowChat</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Fri Jan 16 19:31:19 2015

  */

public class ReqGoGirlShowChat implements ASN1Type {

  /** member variable representing the sequence member roomid of type java.math.BigInteger */
  public java.math.BigInteger roomid;
  /** member variable representing the sequence member fromuid of type java.math.BigInteger */
  public java.math.BigInteger fromuid;
  /** member variable representing the sequence member chatdata of type GoGirlChatData */
  public GoGirlChatData chatdata = new GoGirlChatData();

  /** default constructor */
  public ReqGoGirlShowChat() {}

  /** copy constructor */
  public ReqGoGirlShowChat (ReqGoGirlShowChat arg) {
    roomid = arg.roomid;
    fromuid = arg.fromuid;
    chatdata = new GoGirlChatData(arg.chatdata);
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
    enc.encodeInteger(fromuid);
    chatdata.encode(enc);
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
    fromuid = dec.decodeInteger();
    chatdata.decode(dec);
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
    os.print("fromuid = ");
    os.print(fromuid.toString());
    os.println(',');
    for(int ii = 0; ii < indent+2; ii++) os.print(' ');
    os.print("chatdata = ");
    chatdata.print(os, indent+2);
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
