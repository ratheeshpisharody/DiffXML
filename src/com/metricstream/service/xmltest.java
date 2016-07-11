package com.metricstream.service;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.metricstream.DAO.DiffD;
import com.metricstream.Exceptions.distanceException;
import com.metricstream.Util.parseXMLHandler;
import com.metricstream.domain.DTree;

/**
 * 
 * @author debdipta.h
 *
 *Testing code with the main class
 */
public class xmltest {
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, distanceException {
		try{
			SAXParserFactory factory=SAXParserFactory.newInstance();
			SAXParser parser=factory.newSAXParser();
			String filename="E:\\fox.xml";
			InputStream xmlinput=new FileInputStream(filename);
			parseXMLHandler newhandlerx=new parseXMLHandler();
			parser.parse(xmlinput,newhandlerx);
			DTree treebase=newhandlerx.getTree();
			filename="E:\\fots.xml";
		    xmlinput=new FileInputStream(filename);
		    parseXMLHandler newhandlery = new parseXMLHandler();
			parser.parse(xmlinput,newhandlery);
			DTree treexprt=newhandlery.getTree();
			DiffD newdiff=new DiffD();
			newdiff.diff(treebase, treexprt);
        }
	 catch (Exception e) {
		e.printStackTrace();
	}
 }
}	
