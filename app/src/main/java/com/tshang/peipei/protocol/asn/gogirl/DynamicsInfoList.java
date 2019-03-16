//-----------------------------------------------------------------------------
//   NOTE: this is a machine generated file - editing not recommended
//
//   File: ./src/com/tshang/peipei/protocol/asn/gogirl/DynamicsInfoList.java
//
//   Java class for ASN.1 definition DynamicsInfoList as defined in
//   module GOGIRL.
//   This file was generated by Snacc for Java at Tue Jan 26 18:38:19 2016
//-----------------------------------------------------------------------------

package com.tshang.peipei.protocol.asn.gogirl;

// Import PrintStream class for print methods
import java.io.PrintStream;

// Import ASN.1 basic type representations
import com.ibm.util.*;

// Import ASN.1 decoding/encoding classes
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE OF type <tt>DynamicsInfoList</tt>.
  * Sequence-of classes inherit from java.util.Vector.
  * As a subtype of the Vector class this class also 
  * preserves the order of the contained elements.
  * @author Snacc for Java
  * @version Tue Jan 26 18:38:16 2016

  */

public class DynamicsInfoList extends java.util.Vector implements ASN1Type {

  /** default constructor */
  public DynamicsInfoList() {}

  /** copy constructor */
  public DynamicsInfoList (DynamicsInfoList arg) {
    for (java.util.Enumeration e = arg.elements(); e.hasMoreElements();) {
      addElement(((DynamicsInfo)(e.nextElement())));
    }
  }

  /** encoding method.
    * @param enc
    *        encoder object derived from com.ibm.asn1.ASN1Encoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            encoding error
    */
  public void encode (ASN1Encoder enc) throws ASN1Exception {
    int seq_of_nr = enc.encodeSequenceOf();
    for (java.util.Enumeration e = elements(); e.hasMoreElements();) {
      ((DynamicsInfo)(e.nextElement())).encode(enc);
    }
    enc.endOf(seq_of_nr);
  }

  /** decoding method.
    * @param dec
    *        decoder object derived from com.ibm.asn1.ASN1Decoder
    * @exception com.ibm.asn1.ASN1Exception 
    *            decoding error
    */
  public void decode (ASN1Decoder dec) throws ASN1Exception {
    int seq_of_nr = dec.decodeSequenceOf();
    while (!dec.endOf(seq_of_nr)) {
      DynamicsInfo tmp = new DynamicsInfo();
      tmp.decode(dec);
      addElement(tmp);
    }
  }

  /** print method (variable indentation)
    * @param os
    *        PrintStream representing the print destination (file, etc)
    * @param indent
    *        number of blanks that preceed each output line.
    */
  public void print (PrintStream os, int indent) {
    boolean nonePrinted = true;
    os.println("{ -- SEQUENCE OF --");
    for (java.util.Enumeration e = elements(); e.hasMoreElements();) {
      if (nonePrinted == false)
        os.println(',');
      nonePrinted = false;
      for(int ii = 0; ii < indent+2; ii++) os.print(' ');
      ((DynamicsInfo)(e.nextElement())).print(os, indent+2);
      if (!e.hasMoreElements())
        os.println();
    }
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
