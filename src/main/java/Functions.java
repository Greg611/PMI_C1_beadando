import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Functions {

    public boolean read(String path, ArrayList<Patient> list){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(path);
            Element rootElement = document.getDocumentElement();
            NodeList childNodesList = rootElement.getChildNodes();
            Node node;

            for(int i=0;i<childNodesList.getLength();i++) {
                node = childNodesList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    ArrayList<String> diseases = new ArrayList<>();
                    NodeList childNodesOfPatientTag = node.getChildNodes();
                    String name="",birthYear ="",checkInDate = "",ID = "", bloodType = "";
                    for(int j = 0;j<childNodesOfPatientTag.getLength();j++){
                        if(childNodesOfPatientTag.item(j).getNodeType()==Node.ELEMENT_NODE){
                            switch (childNodesOfPatientTag.item(j).getNodeName()){
                                case "ID": ID=childNodesOfPatientTag.item(j).getTextContent();break;
                                case "name": name=childNodesOfPatientTag.item(j).getTextContent();break;
                                case "birthYear":birthYear=childNodesOfPatientTag.item(j).getTextContent();break;
                                case "bloodType":bloodType=childNodesOfPatientTag.item(j).getTextContent();break;
                                case "checkInDate":checkInDate=childNodesOfPatientTag.item(j).getTextContent();break;
                                case "disease": diseases.add(childNodesOfPatientTag.item(j).getTextContent());break;
                            }
                        }
                    }

                    list.add(new Patient(ID,name,Integer.parseInt(birthYear), bloodType, Date.valueOf(checkInDate),diseases));
                }
            }
            return true;
        }
        catch(FileNotFoundException e){
            System.err.println("A fájl nem létezik, ezért a program nem tud elindulni.");
            return false;
        }
        catch(Exception e){
            System.err.println("Olvasási hiba   "+e + "\n" + e.getStackTrace() + "\n" + e.getCause() + "\n" + e.getMessage());
            return false;
        }
    }

    private Integer readIntegerFromConsole(String outputString){
        Integer result = 0;
        Scanner scn = new Scanner(System.in);
        Boolean b = true;

        while(b){
            try{
                System.out.print(outputString);
                result=scn.nextInt();
                scn.nextLine();
                b=false;
            }
            catch(InputMismatchException ex){
                scn.nextLine();
                System.out.println("Csak szám adható meg.");
            }
        }
        return result;
    }

    public void listPatients(ArrayList<Patient> list){
        for(Patient p : list){
            System.out.println(p.toString());
        }
    }

    private static ArrayList<String> newPatientDiseases(){
        ArrayList<String> diseases = new ArrayList<>();
        Scanner scn = new Scanner(System.in);
        String disease;
        Boolean b = true;
        while(b) {
            System.out.print("Adja meg a panaszt(ha nincs több adjon meg üres sort): ");
            disease = scn.nextLine();
            if(disease.equals("")){
                b=false;
            }
            else if(disease.contains("1")||disease.contains("2")||disease.contains("3")||
                    disease.contains("4")||disease.contains("5")||disease.contains("6")||disease.contains("7")||
                    disease.contains("8")||disease.contains("9")||disease.contains("0"))
            {
                System.out.println("Nem adható meg szám.");
            }
            else{
                diseases.add(disease);
            }

        }
        return diseases;
    }

    private String newPatientID(ArrayList<Patient> list){
        String ID = "";
        Boolean B=true;
        while(B) {
            for (int i = 0; i < 5; i++) {
                Random rnd = new Random();
                Integer IDElement;
                if (i == 0) {
                    IDElement = rnd.nextInt(8) + 1;
                } else {
                    IDElement = rnd.nextInt(9);
                }
                ID = ID + IDElement;
            }
            int i;
            for(i=0;i<list.size();i++){
                if(ID.equals(list.get(i).getID())){
                    break;
                }
            }
            if(i==list.size()){
                B=false;
            }
        }
        return ID;
    }

    public void newPatient(ArrayList<Patient> list){
        Scanner scn = new Scanner(System.in);
        String name;
        System.out.print("Adja meg a páciens nevét:");name = scn.nextLine();
        Integer birthYear = readIntegerFromConsole("Adja meg a páciens születési évét: ");
        ArrayList<String> diseases = newPatientDiseases();
        String ID = newPatientID(list);
        System.out.println("Adja meg a páciens vércsoportját(0-,AB+ stb. alakban): ");
        String bloodType = scn.nextLine();
        Date now = Date.valueOf(LocalDate.now());
        list.add(new Patient(ID,name,birthYear,bloodType, now,diseases));
        System.out.println("Páciens sikeresen felvéve.");
    }

    public void delPatient(ArrayList<Patient> list){
        Integer IDint;
        String ID;
        IDint = readIntegerFromConsole("Adja meg a páciens azonosítóját: ");
        ID = IDint.toString();
        int i;
        for(i=0;i<list.size();i++){
            if(list.get(i).getID().equals(ID)){
                list.remove(i);
                System.out.println("Páciens sikeresen törölve az adatbázisból.");
            }
        }
        if(i==list.size()){
            System.out.println("Nincs ilyen azonosítojú páciens.");
        }
    }

    public  void modifyPatient(ArrayList<Patient> list){
        Scanner scn = new Scanner(System.in);
        Integer ID;
        String name;
        Integer birthYear;
        String disease,newDisease;
        ID = readIntegerFromConsole("Adja meg a páciens azonosítóját:");
        ArrayList<String> modifyList = new ArrayList<>();
        Boolean B = true;
        while(B){
            System.out.println("Adja meg a módosítani kívánt érték nevét(név, születési év, panaszok)");
            String modify = scn.nextLine();
            if(modify.equals("")){
                B=false;
            }
            else{
                modifyList.add(modify);
            }
        }

        for(int i=0;i<modifyList.size();i++){
            switch(modifyList.get(i)){
                case "név" : System.out.println("Adja meg az új nevet:");name = scn.nextLine();
                             for(int j=0;j<list.size();j++){
                                 if(list.get(j).getID().equals(ID.toString())){
                                     list.get(j).setName(name);
                                 }
                             }
                             break;
                case "születési év" :
                        birthYear = readIntegerFromConsole("Adja meg a módosított születési évet:");
                        for(int j=0;j<list.size();j++){
                            if(list.get(j).getID().equals(ID.toString())){
                                list.get(j).setBirthYear(birthYear);
                            }
                        }
                    break;
                case "panaszok" :
                    System.out.println("Adja meg a módosítani kívánt panaszt(ha újat kíván felvenni adjon meg üres sort):");
                    disease=scn.nextLine();
                    System.out.println("Adja meg az új panaszt(ha törölni kíván adjon meg üres sort):");
                    newDisease = scn.nextLine();
                    for(int j=0;j<list.size();j++){
                        if(list.get(j).getID().equals(ID.toString())){
                            list.get(j).setDiseaseByName(disease,newDisease);
                        }
                    }
                    break;
            }
        }

    }

    public void save(ArrayList<Patient> list,String path){
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("patients");
            document.appendChild(rootElement);

            for(Patient patient : list){
                Element patientElement = document.createElement("patient");
                rootElement.appendChild(patientElement);
                createElement(document,patientElement,"ID",patient.getID());
                createElement(document,patientElement,"name",patient.getName());
                createElement(document,patientElement,"birthYear",patient.getBirthYear().toString());
                createElement(document,patientElement,"bloodType",patient.getBloodType().getShortName());
                createElement(document,patientElement,"checkInDate",patient.getCheckInDate().toString());
                for(int i = 0; i<patient.getDiseases().size();i++) {
                    createElement(document, patientElement, "disease", patient.getDiseases().get(i));
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(path));
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(source,result);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void createElement(Document document,Element parent,String tagName,String value){
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }

}
