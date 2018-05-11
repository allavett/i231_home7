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
 * https://stackoverflow.com/questions/10093860/creating-a-byte-type-from-a-string-in-java
 */

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.fastinfoset.util.CharArray;

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
   private byte[] encodedHuff;

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
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b: encodedHuff) {
         stringBuilder.append(Integer.toBinaryString(b));
      }
      String encodedString = stringBuilder.toString();

      return encodedString.length(); // TODO!!!
   }

   /** Encoding the byte array using this prefixcode.
    * @param origData original data
    * @return encoded data
    */
   public byte[] encode (byte [] origData) {
      byte[] bArray = new byte[origData.length];
      for (int i = 0; i < origData.length; i++) {
         bArray[i] = charNodeTree.traverseTreeEncode(origData[i]);
      }
      this.encodedHuff = bArray;
      return bArray;
   }

   /** Decoding the byte array using this prefixcode.
    * @param encodedData encoded data
    * @return decoded data (hopefully identical to original)
    */
   public byte[] decode (byte[] encodedData) {
      byte[] bArray = new byte[encodedData.length];
      for (int i = 0; i < encodedData.length; i++) {
         bArray[i] = charNodeTree.traverseTreeDecode(encodedData[i]);
      }
      return bArray; // TODO!!!
   }

   /** Main method. */
   public static void main (String[] params) {
      String tekst = "AAA";//AAAAAAABBBBBCCCDE";//CAAAAAAAAAAAAABBBBBBCCCDDEEFFFFFFF";//AAAAABBBBCCCDDDEEFF";//
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
      CharNode charNodeSibling = new CharNode((byte) -1, 0, 0, null, null);
      Boolean rootNodeHasSibling = false;

      while (byteIterator.hasNext()){
         Map.Entry byteEntry = (Map.Entry) byteIterator.next();
         byte charId = (Byte) byteEntry.getKey();
         int charFrequency = (int) byteEntry.getValue();

         CharNode childNode = new CharNode(charId, charFrequency, 0, null, null);

         if (!rootNodeHasSibling) {
            if (charFrequency < charNodeRoot.frequency) {
               rootNodeHasSibling = true;
               charNodeSibling = childNode;
               charNodeSibling.setBinary(1);
               if (!byteIterator.hasNext()){
                  charNodeSibling = new CharNode(childNode.id, childNode.frequency, 0, charNodeRoot, null);
                  charNodeRoot = new CharNode((byte) -1, charNodeSibling.frequency + charNodeSibling.sibling.frequency, 1, null, charNodeSibling);
               }
               continue;
            }
         } else {
            childNode.setSibling(charNodeSibling);
            charNodeSibling = new CharNode((byte) -1, childNode.frequency + childNode.sibling.frequency, 0, charNodeRoot, childNode);
            charNodeRoot = new CharNode((byte) -1, childNode.frequency + childNode.sibling.frequency, 1, null, charNodeSibling);
            rootNodeHasSibling = false;
            continue;
         }

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
      if (counter > 1) {
         this.binary = -1;
      } else {
         this.binary = charNodeRoot.binary;
      }
      this.firstChild = charNodeRoot.firstChild;
      this.sibling = charNodeRoot.sibling;
   }

   public byte traverseTreeEncode(byte b){
      Stack<CharNode> alternateRoute = new Stack<>();
      Stack<StringBuilder> alternateByteString = new Stack<>();
      CharNode currentNode = this;
      StringBuilder byteString = new StringBuilder();
      do {
         if (currentNode.id == b){
            if (byteString.toString().isEmpty()) {
               byteString.append(currentNode.binary);
            }
            break;
         }
         if (currentNode.firstChild == null && currentNode.sibling == null){
            //
            if (!alternateRoute.empty()){
               currentNode = alternateRoute.pop();
               byteString = alternateByteString.pop();
               byteString.deleteCharAt(byteString.length() - 1);
            }

            byteString.append(currentNode.binary);
         } else if (currentNode.firstChild == null){
            currentNode = currentNode.sibling;
            byteString.deleteCharAt(byteString.length() - 1);
            byteString.append(currentNode.binary);
         } else if (currentNode.sibling == null){
            currentNode = currentNode.firstChild;
            byteString.append(currentNode.binary);
         } else {
            alternateRoute.push(currentNode.sibling);
            alternateByteString.push(new StringBuilder(byteString));
            currentNode = currentNode.firstChild;
            byteString.append(currentNode.binary);
         }
      }
      while (currentNode != null);
      System.out.println(byteString);
      byte byteResult = (byte) Integer.parseInt(byteString.toString(), 2);

      return byteResult;
   }

   public byte traverseTreeDecode(byte b){
      CharNode currentNode = this;
      String byteString = Integer.toBinaryString(b);
      char[] cArray = byteString.toCharArray();

      //char[] cArray = byteString.toCharArray();
      //StringBuilder byteString = new StringBuilder();
      for (int i = 0; i < cArray.length; i++) {
         byte bItem = (byte) Integer.parseInt(String.valueOf(cArray[i]));
         if (bItem == currentNode.binary && currentNode.id != -1) break;
         if (bItem == 1){
            currentNode = currentNode.firstChild.sibling;
         } else if (bItem == 0) {
            currentNode = currentNode.firstChild;
         }
      }
      return currentNode.id;
   }
}
