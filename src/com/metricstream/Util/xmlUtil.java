package com.metricstream.Util;


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.metricstream.constants.nodeType;
import com.metricstream.domain.DTreeNode;
/**
 * 
 * @author debdipta.h
 *
 */
public class xmlUtil {

	 public void writeToxml(DTreeNode root,String filename){
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder doc;
		Document newdoc;
		try{
			doc=factory.newDocumentBuilder();
			newdoc=doc.newDocument();
		    Element rootelement=newdoc.createElement(root.getNodeName());
			newdoc.appendChild(rootelement);
			for(DTreeNode child:root.getChildren()){
				if(child.getType().equals(nodeType.Attribute.toString()))
					rootelement.setAttribute(child.getNodeName(),child.getNodeValue());
				else
					rootelement.appendChild(setChild(newdoc,child));
			}
			writeTofile(newdoc,filename);
		   }
		catch(DOMException e){
			System.err.println("Cannot create the element");
			e.printStackTrace();
		}
		catch(ParserConfigurationException e){
			System.err.println("Problem with creation of the document");
			e.printStackTrace();
		}
	 }
	 
	 
	 private Node setChild(Document doc,DTreeNode node){
		 Element childnode=doc.createElement(node.getNodeName());
		 for(DTreeNode schild:node.getChildren()){
			String type=schild.getType();
			if(type.equals(nodeType.Attribute.toString()))
				childnode.setAttribute(schild.getNodeName(),schild.getNodeValue());
			else if (type.equals(nodeType.Element.toString()))
				childnode.appendChild(setChild(doc,schild));
			else if (type.equals(nodeType.Text.toString()))
				childnode.appendChild(doc.createCDATASection(schild.getNodeValue()));
			else{
				System.out.println("Not matching any nodetype hence escaping");
				System.exit(1);
			}	 
		 }
		 return childnode;
	 }
	 
	 
	 private void writeTofile(Document doc,String pathname){
		 try {
			Transformer transformer=TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			DOMSource source=new DOMSource(doc);
			StreamResult output=new StreamResult(new File(pathname));
			transformer.transform(source, output);
			System.out.println("File generated at "+pathname);
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
		   System.out.println("Problem creating the transformer object");
			e.printStackTrace();
		}
		 catch(TransformerException e){
			 System.err.println("cannot write to the desired file");
			 e.printStackTrace(); 
		 } 
		 
	 }
	 	
	}

