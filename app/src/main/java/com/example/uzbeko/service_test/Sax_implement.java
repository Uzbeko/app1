package com.example.uzbeko.service_test;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Ed on 8/11/2015.
 */
public class Sax_implement extends DefaultHandler {
//    private Object[] result = new Object[5];
    private ArrayList<Items> dataParser = new ArrayList<Items>();
    Items test;
    boolean start = false;
    boolean onElement = false;
    boolean title = false;
    boolean pubDate = false;
    boolean thumbnail = false;
    boolean link =false;
    int item_count;

    InputStream inputStream;
    StringBuffer buf = new StringBuffer();


//==========================================
    public void goFeed(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
    //--pasiruosiam ArrayLista------------
        if(dataParser.isEmpty()){
            dataParser.add(new Items());
        } else {
            dataParser.clear();
            dataParser.add(new Items());
        }
    //------------------------------
        item_count = 0;
        this.inputStream = inputStream;
        System.out.println("!!!!!!!!!!!!! spausdinu is Sax_test(goFeed())___gavay inpyt stream "+inputStream.available());
        try {
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxPars = saxPF.newSAXParser();
            XMLReader xmlR = saxPars.getXMLReader();

//            InputStream inputStream = getResources().openRawResource(resourceName);
////            String strfile1 = getApplicationContext().getFilesDir().getAbsolutePath() + "/serstatus.txt" ;
//            File fail = new File("C://Users/Ed/AndroidStudioProjects/MyApplication/parser_test/src/main/res/raw");
//            System.out.println("!!!!!!!!!!!!! ar yra tokia failo direktorija?: " + fail.isDirectory());

            System.out.println("!!!!!!!!!!!!! spausdinu is Sax_test(goFeed())");

            xmlR.setContentHandler(this);
            //INPUTSTREAM--

            //encoding----------
            InputStreamReader inputRead = new InputStreamReader(inputStream,"UTF-8");
            InputSource inputSource = new InputSource(inputRead);
            inputSource.setEncoding("UTF-8");
            //--end enceoding---
            xmlR.parse(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

//--Start parsing------------------------------------------------------------------------------------

    @Override
    public void startDocument() throws SAXException {
        System.out.println("TTTTTTTTTTTTTTTT: dokumentas parisnti pradetas");
    }
    //------------------------------------------------------------------------------------------------
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(localName.equals("item")){
            start=true;
            System.out.println("TTTTTTTTTTTTTTTT: uzejau ant item");
        }
        if(start) {
            if (localName.equals("title")) {
                onElement = true;
                title = true;
                System.out.println("TTTTTTTTTTTTTTTT: uzejau ant title");
            }
            else if (localName.equals("pubDate")) {
                onElement = true;
                pubDate = true;
                System.out.println("TTTTTTTTTTTTTTTT: uzejau ant pubDate");
            }
            else if (localName.equals("link")) {
                onElement = true;
                link = true;
                System.out.println("TTTTTTTTTTTTTTTT: uzejau ant link");
            }
            else if (localName.equals("thumbnail")){
                onElement = true;
                thumbnail = true;
                dataParser.get(item_count).setthumbnail(attributes.getValue("url"));
                System.out.println("TTTTTTTTTTTTTTTT: uzejau ant thumbnail: " +item_count+" : itemcounr | "+dataParser.get(item_count).getthumbnail());
//                System.out.println( attributes.getValue("url"));                //return string
            }
        }

    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(localName.equals("title")) {
            onElement = false;
            title = false;

            dataParser.get(item_count).setTitle(buf.toString());
            System.out.println(item_count+" | bufbufbufuf: " + dataParser.get(item_count).getTitle());
//            result[0]= buf.toString();
//            System.out.println("bufbufbufuf: " + result[0]);
            buf.delete(0, buf.length());
            System.out.println("TTTTTTTTTTTTTTTT: Element is added: title");
        }
        else if(localName.equals("pubDate")){
            onElement = false;
            pubDate = false;

            dataParser.get(item_count).setPubDate(buf.toString());
            System.out.println(item_count+" | bufbufbufuf: " + dataParser.get(item_count).getPubDate());
//            result[1]= buf.toString();
//            System.out.println("bufbufbufuf: " + result[1]);
            buf.delete(0,buf.length());
            System.out.println("TTTTTTTTTTTTTTTT: Element is added: pubDate");
        }
        else if(localName.equals("link")){
            onElement = false;
            link = false;

            dataParser.get(item_count).setLink(buf.toString());
            System.out.println(item_count+" | bufbufbufuf: " + dataParser.get(item_count).getLink());
//            result[1]= buf.toString();
//            System.out.println("bufbufbufuf: " + result[1]);
            buf.delete(0,buf.length());
            System.out.println("TTTTTTTTTTTTTTTT: Element is added: link");
        }

        if(localName.equals("item")){
            dataParser.add(new Items());
            ++item_count;
            if(item_count == 10) {
                start = false;
                System.out.println("!!!!!!!!!!!!! PABAIGA");
                for(int e=0; e<dataParser.size();e++){
                    System.out.println("!!!!!!!!!dataParser: pubdate "+((Items)dataParser.get(e)).getPubDate());
                    System.out.println("!!!!!!!!!dataParser: Title"+((Items)dataParser.get(e)).getTitle());
                    System.out.println("!!!!!!!!!dataParser: Thumbnail"+((Items)dataParser.get(e)).getthumbnail());
                    System.out.println("!!!!!!!!!dataParser: link"+((Items)dataParser.get(e)).getLink());
                }
                try {
                    System.out.println("!!!!!!!!!!!!! spausdinu is Sax_test(goFeed())___imput stram pries uzdaryma: " + inputStream.available());
                    inputStream.close();                       //--kai baigs parsinti uzdarau Inputsource
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new DoneParsingException();
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if(onElement) {
            if (title) {
                buf.append(ch, start, length);
                System.out.println("TTTTTTTTTTTTTTTT: characters is caled: title");
            }
            else if (pubDate){
                buf.append(ch, start, length);
                System.out.println("TTTTTTTTTTTTTTTT: characters is caled: pubDate");
            }
            else if (link){
                buf.append(ch, start, length);
                System.out.println("TTTTTTTTTTTTTTTT: characters is caled: link");
            }
        }
    }
     //---------------------------------------------------------------------------------------------
    public ArrayList<Items> getParseData(){
        return this.dataParser;
    }
    //----------------------------------------------------------------------------------------------
//    public String getParseResult(){
//        if(result[0] == null){
//            System.out.println("nulis grazinamas is getParseResult()");
//            return null;
//
//        } else {
//            System.out.println("++++++++++++++Return from Parser");
//            return (String)result[0];
//        }
//    }

    //--------exception DoneParsingException class--------------------------------------------
    public class DoneParsingException extends SAXException {
    }
}

