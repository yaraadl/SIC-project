//yara adel hassan mohamed 19100683 tuesday 8,30
//Step 2 in SIC
//  TASK 1 (done) :a program to --Read an input  File and divide it
//into 3 arrays or arraylists

//TASK 2 (done): A PRogram to create a new array and assign the location of the instruction into it

//Task 3{done): generating the opcode
//Tasl 4(done): generating the HTE record 

package assignment.pkg1.system;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;   //importing all java classes


public class Assignment1System {
    
    //creating two hashmaps will be holding the symol table and the opcodes
    static HashMap<String,String> opcode = new HashMap<String, String>();
    static HashMap<String,String> symbolTable = new HashMap<String, String>();
        
        
    public static void InitializeOpcode()
    {
        //assigning these opcodes to the hashmap to be used when generating the object code
        opcode.put("ADD","18");
        opcode.put("ADDF","58");
        opcode.put("AND","40");
        opcode.put("COMP","28");
        opcode.put("COMPF","88");
        opcode.put("DIV","24");
        opcode.put("DIVF","64");
        opcode.put("J","3C");
        opcode.put("JEQ","30");
        opcode.put("JGT","34");
        opcode.put("JLT","38");
        opcode.put("JSUB","48");
        opcode.put("LDA","00");
        opcode.put("LDB","68");
        opcode.put("LDCH","50");
        opcode.put("LDF","70");
        opcode.put("LDL","08");
        opcode.put("LDS","6C");
        opcode.put("LDT","74");
        opcode.put("LDX","04");
        opcode.put("LPS","D0");
        opcode.put("MUL","20");
        opcode.put("MULF","60");
        opcode.put("OR","44");
        opcode.put("RD","D8");
        opcode.put("RSUB","4C");
        opcode.put("TD","E0");
        opcode.put("TIX","2C");
        opcode.put("WD","DC");
        opcode.put("SSK","EC");
        opcode.put("STA","0C");
        opcode.put("STB","78");
        opcode.put("STCH","54");
        opcode.put("STF","80");
        opcode.put("STI","D4");
        opcode.put("STL","14");
        opcode.put("STSW","E8");
        opcode.put("STT","84");
        opcode.put("STX","10");
        opcode.put("SUB","1C");
        opcode.put("SUBF","5C");
    }
    
    //passing by reference
    public static void TextToArray(String location[] ,String label [], String inst [], String ref [], String program[])
    { //function to assign values in the sic file into 3 arrays 
        int nextLocation = 0;
        int size = 0; // size stands for line number
        
        try { //reading the file 
            File myObj = new File("C:\\Users\\fast\\Downloads\\inSIC.txt");
            Scanner myReader = new Scanner(myObj);
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                
                data = data.toUpperCase();//making sure all data are upper case
                
                data = data.trim();//removing any unnecessary spaces

                // to skip any comments in assembly code
                if (data.charAt(0) == '.') {
                    continue;
                }

                String[] arr = data.split("\t+");//reconizing our 3 arrays according to number of spaces

                if (arr.length == 3) {
                    if(arr[1].matches("START")) { //searching for the first location in the program
                        nextLocation = Integer.parseInt(arr[2],16);//converting the string to integer
                        location[size] = Integer.toHexString(nextLocation);//converting integer to hex string
                       
                        
                        program[0] = arr[0]; //putting the name of the program in the first array
                    }
                   
                    label[size] = arr[0]; //assigning  the label to the first array
                    inst[size] = arr[1];  // assigning the instruction to the second array
                    ref[size] = arr[2];   // assigning the reference to the third array
                } 
                else if (arr.length == 2) {  //if length is 2 so we  probably have an instruction and a refernce only 
                    label[size] = "*";        // assign the label into this symbol 
                    inst[size] = arr[0];     
                    ref[size] = arr[1];       // assign each index to its value
                }
                else if (arr.length == 1) {   //if length is 1 so we  probably have an instruction  only 
                    label[size] = "*";        // assign the label and the refernce  into this symbol 
                    inst[size] = arr[0];
                    ref[size] = "*";
                }
              
                
              location[1] = Integer.toHexString(nextLocation);// to assign the second location to the start of the program
                
              if (inst[size].matches("RESW") ) {
                   location[size+1] = Integer.toHexString(Integer.parseInt(ref[size])*3 + Integer.parseInt(location[size],16));
                }
               else if (inst[size].matches("RESB")  ) {
                    location[size+1] = Integer.toHexString(Integer.parseInt(ref[size])*1 + Integer.parseInt(location[size],16));
                } // will be placed in the next location
                else if (inst[size].matches( "BYTE"))
                {
                    if(ref[size].charAt(0) == 'C'){
                        location[size+1] = Integer.toHexString(((ref[size].length()-3) *1) + Integer.parseInt(location[size], 16));
                    }
                    else{ //hexadecimal
                        location[size+1] = Integer.toHexString((((ref[size].length())-3)/2) + Integer.parseInt(location[size], 16));
                    }
                } 
               else{ // word or any  instruction
                    location[size+1] = Integer.toHexString(3 + Integer.parseInt(location[size], 16));
               } 
                
                size++ ;
            }
            //java has no call by reference so we made a string array to carry name of prog and its size which will be converted later 
            program[1] = Integer.toString(size);
            //assigning the first instruction after the start of the program with the same location as the first one
            
            myReader.close(); //stop reading from the file 
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); // handeling if file wasn,t found exception 
        }
        
    }
    
    public static void GetSymbolTable(String [] location, String[] label, int size)
    {
        for(int i=0; i<size; i++)
        {
            if(!label[i].equals("*")) //if label isnt an astric so we deduce it is there 
            {
                symbolTable.put(label[i], location[i]); // assign its label and location into the symbol table hashmap
            }
        }
    }
    
    //getting the ascii code of some characters to assign it to their object code
    public static String ASCIICode(char c)
    {
        return Integer.toHexString(c);
    }
    
    public static void ObjectCode(String inst [], String ref [], String[] objectCode, int size)
    {
        for(int i=0; i<size; i++)
        {
            if(inst[i].equals("RESW") || inst[i].equals("RESB") || inst[i].equals("START") || inst[i].equals("END"))
            {
                objectCode[i] = "NoObjectCode";
            }
            else if(inst[i].equals("WORD"))
            {   //writting any word as it is but in 6 digits
                objectCode[i] = String.format("%06x", Integer.parseInt(ref[i]));
            }
            else if(inst[i].equals("BYTE"))
            {
                if(ref[i].charAt(0) == 'C')
                {
                    String byteObjectCode = "";
                    for(int j=2; i<ref[i].length()-2; j++)//ending at length - 2 & beginning by 2 to skip this c''  3 chars 
                    { //converting them with their ascii codes in a seprate string named byte object code 
                        byteObjectCode.concat(ASCIICode(ref[i].charAt(j)));
                    } 
                    objectCode[i] = byteObjectCode; 
                    // asigninng the object code to the sring of ascii we made 
                }
                else
                { //hexa decimal case
                    String byteObjectCode = "";
                    for(int j=2; i<ref[i].length()-2; j++)//ending at length - 2 & beginning by 2 to skip this x''  3 chars 
                    { //converting each character to string to be but in the object code
                        byteObjectCode.concat(Character.toString(ref[i].charAt(j)));
                    }// asigninng the object code to the sring of ascii we made
                    objectCode[i] = byteObjectCode;
                }
            }
            
            else
            {
                String instObjectCode = opcode.get(inst[i]); 
                
                if(ref[i].charAt(ref[i].length()-2) == ',')//if having a comma then it is indexed
                {
                    String[] refrance = ref[i].split(",");//split this cell content at the comma
                    int number = Integer.parseInt(symbolTable.get(refrance[0]), 16) + 32768; // on being indexed add 8000  but in decimal 
                    instObjectCode += String.format("%04x", number);
                }
                else
                {
                    instObjectCode += String.format("%04x", Integer.parseInt(symbolTable.get(ref[i]), 16));
                }
                // asigninng the object code to the sring of hexadecimals we made 
                objectCode[i] = instObjectCode;
            }
        }
    }
    
    public static void HRecord(ArrayList<String> HTERecord, String progName, String firstLocation, String lastLocation)
    {
        //H^PROGNAME6^START LOCATION6^,LENGHT6
        
        for(int i=progName.length(); i<6; i++) // i<6 for being a 6 places string
        {
            progName += "X"; //adding some x characters to complete the 6 places 
        }
        
        String H = "H^" +
                progName + "^" +
                String.format("%06x", Integer.parseInt(firstLocation, 16)) + "^" +
                String.format("%06x", Integer.parseInt(lastLocation, 16) - Integer.parseInt(firstLocation, 16));//last location - first location= size
        
        HTERecord.add(H);// adding the h string to the main hte record
    }
    
    public static void TRecord(ArrayList<String> HTERecord, String[] objectCode, String[] location, int size)
    {
        //T^STARTLOCATION6^LENGHT2^OBJECTCODE10
        
        String T = "";// the string we will be concatinating on
        
        for(int i=1; i<size-1; i++)
        {   //putting the t records in an arrray list to be easily accesed
            ArrayList<String> TRecord = new ArrayList();
            String startLocation = location[i];//starting location of this t record 
            int recordSize = 0;
            boolean Flag = false; //has no obj code
            
            while(!objectCode[i].equals("NoObjectCode") && recordSize < 60)
            {
                Flag = true;//ready to generate an obj code
                
                T = "T^"; // concatinating the  T  at the beggining of the record
                recordSize += objectCode[i].length();//updating the record size upon adding each objectcode
                TRecord.add(objectCode[i]);// adding the sequence of object codes
                
                i++;
            }
            
            if(Flag == true)
            {
                Flag = false;//in order not to reenter the loop except after checking it has a string that is ready to generate its t record
                
                int lastLocation = Integer.parseInt(location[i], 16);// storing the last location
            
                int length = lastLocation-Integer.parseInt(startLocation, 16);//calculating the length of the current hte record 

                T += String.format("%06x", Integer.parseInt(startLocation, 16)) + "^" +// concatinating the start oif this t record and its length
                     String.format("%02x", length) + "^";

                for(int j=0; j<TRecord.size(); j++)
                {
                    T += TRecord.get(j); //adding the t record

                    if(j+1<TRecord.size())
                    {
                        T += "^";//to prevent putting it at the end of the record
                    }
                }
                
                if(recordSize >= 60) // checking if the size is more than 60 decrase 1 as not to add an extra iteration
                {
                    i--;
                }

                HTERecord.add(T); // adding the whole t record
            }
        }
    }
    
    public static void ERecord(ArrayList<String> HTERecord, String firstLocation)
    {
        //E^START 6
                
        String E = "E^" + String.format("%06x", Integer.parseInt(firstLocation, 16));
        
        HTERecord.add(E);
    }
    
    public static void HTE(ArrayList<String> HTERecord, String progName, String firstLocation, String lastLocation, String[] objectCode, String[] location, int size)
    {
        //H^PROGNAME6^START LOCATION6^,LENGHT6
        HRecord(HTERecord, progName, firstLocation, lastLocation);
        //T^STARTLOCATION6^LENGHT2^OBJECTCODE10
        TRecord(HTERecord, objectCode, location, size);
        //E^START 6
        ERecord(HTERecord, firstLocation);
    } 
    

    public static void main(String[] args) {

        String[] label = new String[100]; // array holding the labels
        String[] inst = new String[100];  // array holding the instruction
        String[] ref = new String[100];  // array holding the refernce
        String[] location = new String[100];  // array holding the location
        String[] program = new String[2];     // array holding the program name
        String[] objectCode = new String[100];   // array holding the opcode
        ArrayList<String> HTERecord = new ArrayList();
        String progName = null;               //string throuch which the program name is stored
        int size = 0;  // assigning the initial value of the program to zero
        
        InitializeOpcode();
        
        TextToArray(location, label, inst, ref, program); //calling the fuction to print the 3 arrays
        progName = program[0]; // telling the program that its name is at index zero
        size = Integer.parseInt(program[1]); // beginig the size with the refernce of the start
        
        GetSymbolTable(location, label, size);
        
        ObjectCode(inst, ref, objectCode, size);
        
        HTE(HTERecord, progName, location[0], location[size-1], objectCode, location, size);
        
        for(int i=0; i<size; i++){ //printing
            System.out.println(location[i] + "\t" + label[i] + "\t" + inst[i] + "\t" + ref[i]+ "\t" +objectCode[i]);
        }
        
        System.out.println("===================================================================================================");
        
        System.out.println("Symbol Table:");
        System.out.println(Collections.singletonList(symbolTable));
        
        System.out.println("===================================================================================================");
        
        System.out.println("HTE Record:");
        for(int i=0; i<HTERecord.size(); i++)
        {
            System.out.println(HTERecord.get(i));
        }
        System.out.println("===================================================================================================");
    } 
}