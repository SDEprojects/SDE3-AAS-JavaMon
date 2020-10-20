package com.capstone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ValidPlayerEntry {

    TextParser myParser = new TextParser();


    public Boolean validPlayerEntry() {
        try {
            String checker[] = myParser.getUserInput().split(" ");

            File inputFile = new File("data", "keyWords.txt");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // prints the root element of the file which is "keyWords" using getNodeName()
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

// Insert if statement for "action item by checking if the first word corresponds to items in action group - need to initialize reference"
            // creates and populates a list of nodes tag items by the tag name "action"
            NodeList nList = doc.getElementsByTagName("action");
            System.out.println("----------------------------");

            // iterates over node list of tag names "action"
            for (int temp = 0; temp < nList.getLength(); temp++) {
                // fetches node item from list by their index position
                Node nNode = nList.item(temp);
                System.out.println("Node list length is: " + nList.getLength());
                // prints current node name
                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // for(int a = 0; a < checker.length; a++)
                    if (checker[0] == eElement.getElementsByTagName("engage").item(0).getTextContent()) {
                        System.out.println("Player interacts");
                    } else if (checker[0] == eElement.getElementsByTagName("communicate").item(0).getTextContent()) {
                        System.out.println("Player talks");
                    } else if (checker[0] == eElement.getElementsByTagName("utilize").item(0).getTextContent()) {
                        System.out.println("Player uses");
                    } else if (checker[0] == eElement.getElementsByTagName("purchase").item(0).getTextContent()) {
                        System.out.println("Player buys");
                    } else myParser.getUserInput();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
