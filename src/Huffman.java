/*
 * Write a method to create a Huffman prefix code for a given byte array (as constructor) and methods to code and decode
 * data. Code must be optimal and decoding must completely restore the original array. Do not forget to test byte array
 * of length 1. Have a look at: http://www.itcollege.ee/~jpoial/algoritmid/kodeerimisyl.html .
 *
 * Used materials: http://enos.itcollege.ee/~jpoial/algoritmid/strat.html
 * https://stackoverflow.com/questions/32177934/best-way-to-order-an-hashmap-by-key-in-java
 * https://stackoverflow.com/questions/44367203/how-to-count-duplicate-elements-in-arraylist
 * https://www.mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/
 * https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
 */
import com.sun.org.apache.xerces.internal.xs.datatypes.ByteList;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Prefix codes and Huffman tree.
 * Tree depends on source data.
 */
public class Huffman {

   // TODO!!! Your instance variables here!
   private CharNode charNodeTree;

   /** Constructor to build the Huffman code for a given bytearray.
    * @param original source data
    */
   Huffman (byte[] original) {
      // TODO!!! Your constructor here!
      Map<Byte, Integer> characterFrequency = frequency(original);
      charNodeTree = new CharNode((byte) 0, 0 , 0 ,null, null);
      charNodeTree.createNodeTree(characterFrequency);
      System.out.println("end");

   }

   /**
    *
    * @param o
    * @return
    */
   private Map<Byte, Integer> frequency(byte[] o){
      List<Integer> listByte = new ArrayList<>();
      Map<Integer, Long> frequencySort = new LinkedHashMap<>();
      Map<Byte, Integer> frequencyByte = new LinkedHashMap<>();

      for (byte character: o) {
         listByte.add((int) character);
      }

      Map<Integer, Long> frequencyMap = listByte.stream()
              .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

      frequencyMap.entrySet().stream().sorted(Map.Entry.<Integer, Long> comparingByValue())
              .forEachOrdered(e -> frequencySort.put(e.getKey(), e.getValue()));

      // TODO! Questionable!!
      for (Map.Entry<Integer, Long> entry: frequencySort.entrySet()) {
         frequencyByte.put( (byte) (int) entry.getKey(),  Integer.parseInt(String.valueOf(entry.getValue())));
      }

      return frequencyByte;
   }

   /** Length of encoded data in bits. 
    * @return number of bits
    */
   public int bitLength() {
      return 0; // TODO!!!
   }

   /** Encoding the byte array using this prefixcode.
    * @param origData original data
    * @return encoded data
    */
   public byte[] encode (byte [] origData) {
      for (int i = 0; i < origData.length; i++) {
         charNodeTree.traverseTree(origData[i]);
      }
      return null; // TODO!!!
   }

   /** Decoding the byte array using this prefixcode.
    * @param encodedData encoded data
    * @return decoded data (hopefully identical to original)
    */
   public byte[] decode (byte[] encodedData) {
      return null; // TODO!!!
   }

   /** Main method. */
   public static void main (String[] params) {
      String tekst = "CAAAAAAAAAAAAABBBBBBCCCDDEEFFFFFFF";
      byte[] orig = tekst.getBytes();
      Huffman huf = new Huffman (orig);
      byte[] kood = huf.encode (orig);
      byte[] orig2 = huf.decode (kood);
      // must be equal: orig, orig2
      System.out.println (Arrays.equals (orig, orig2));
      int lngth = huf.bitLength();
      System.out.println ("Length of encoded data in bits: " + lngth);
      // TODO!!! Your tests here!
   }
}

class CharNode {
   private byte id;
   private int frequency;
   private int binary;
   private CharNode sibling;
   private CharNode firstChild;

   CharNode(byte c, int f, int bi, CharNode sib, CharNode child){
      setId(c);
      setFrequency(f);
      setBinary(bi);
      setSibling(sib);
      setFirstChild(child);
   }

   private void setId(byte id) {
      this.id = id;
   }
   private void setFrequency(int frequency) {
      this.frequency = frequency;
   }

   private void setBinary(int binary) {
      this.binary = binary;
   }

   private void setSibling(CharNode sibling) {
      this.sibling = sibling;
   }

   private void setFirstChild(CharNode firstChild) {
      this.firstChild = firstChild;
   }

   public void createNodeTree(Map<Byte, Integer> frequencyList){
      Iterator byteIterator = frequencyList.entrySet().iterator();
      int counter = 0;
      CharNode charNodeRoot = new CharNode((byte) -1, 0, 0, null, null);
      while (byteIterator.hasNext()){
         Map.Entry byteEntry = (Map.Entry) byteIterator.next();
         byte charId = (Byte) byteEntry.getKey();
         int charFrequency = (int) byteEntry.getValue();
         CharNode childNode = new CharNode(charId, charFrequency, 0, null, null);
         if (counter == 0) {
            charNodeRoot = childNode;
            if (byteIterator.hasNext()){
               charNodeRoot.setBinary(1);
            }
         } else {
            childNode.setSibling(charNodeRoot);
            charNodeRoot = new CharNode((byte) -1, childNode.frequency + childNode.sibling.frequency, 1, null, childNode);
         }
         System.out.println("test");
         counter++;
      }
      this.id = charNodeRoot.id;
      this.frequency = charNodeRoot.frequency;
      this.binary = charNodeRoot.binary;
      this.firstChild = charNodeRoot.firstChild;
      this.sibling = charNodeRoot.sibling;
   }

   public byte traverseTree(byte b){

      return 0;
   }
}
