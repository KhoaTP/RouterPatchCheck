import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RouterPatchCheck {

	static ArrayList<String> content = new ArrayList<String>();
	static ArrayList<String> removedContent = new ArrayList<String>();
	
	//Reads the CSV file and stores it in a class variable
	public static void readCSV(String input){
		 String line = "";
		 try (BufferedReader br = new BufferedReader(new FileReader(input))) {
	            while ((line = br.readLine()) != null) {
	            	//System.out.println(line);
	            	content.add(line);
	            }  
	        } catch (IOException e) {
	            System.out.println("Please enter a valid .csv file");
	        }
	}
	
	//Splits ArrayList lines in to String arrays to allow navigation through the CSV file
	public static ArrayList<String[]> splitInput(ArrayList<String> input){
		ArrayList<String[]> sortedInput = new ArrayList<String[]>();
		for(int i=0; i<input.size(); i++){
 			sortedInput.add(input.get(i).split(","));
		}
		return sortedInput;
	}
	
	//Checks if router is patched
	public static void checkPatched(ArrayList<String[]> input){
 		for(int i=1; i<content.size(); i++){
 			if(input.get(i)[2].equalsIgnoreCase("yes")){
 				removedContent.add(content.get(i));	
 			}
 		}
 		//System.out.println(removedContent);	
	}

	//Checks if router OS is below 12
	public static void checkVersion(ArrayList<String[]> input){
 		for(int i=1; i<input.size(); i++){
 			double num = Double.parseDouble(input.get(i)[3]);
 			if(num < 12){
 				removedContent.add(content.get(i));
 			}		
 		}
 		//System.out.println(removedContent);
	}
	
	//Checks if the routers share the same IP Address
	public static void checkIPAddress(ArrayList<String[]> input){
 		for(int i=1; i<content.size(); i++){
 			String address1 = input.get(i)[1];
 			//System.out.println(address1);
 			for(int j=1; j<content.size(); j++){
 				String address2 = input.get(j)[1];
 				if(!(i==j)){
					if((address1.equals(address2))){
						removedContent.add(content.get(i));
					}
				}
 			}
 		}
 		//System.out.println(removedContent);
 		
	}
	
	//Checks if routers share the same Host-name
	public static void checkName(ArrayList<String[]> input){

 		for(int i=1; i<content.size(); i++){
 			String name1 = input.get(i)[0];
 			for(int j=1; j<content.size(); j++){
 				String name2 = input.get(j)[0];
 				if(!(i==j)){
					if((name1.equalsIgnoreCase(name2))){
						removedContent.add(content.get(i));
					}
				}
 			}
 		}
 		//System.out.println(removedContent);
 		
	}
	
	//Compares the list of routers that don't need patching to the original list
	public static ArrayList<String> compareList(ArrayList<String> list1, ArrayList<String> list2){
		
		ArrayList<String> to_return = list1;
		
		for(int i=0; i<list2.size(); i++){
			String temp1 = list2.get(i);		
			for(int j=1; j<list1.size(); j++){	
				String temp2 = list1.get(j);	
				if(temp1.equals(temp2)){		
					to_return.remove(j);		
				}
			}
			
		}
		//System.out.println(to_return);
		return to_return;
	}
	
	//Prints the list in the specified format
	public static void formatList(ArrayList<String> list){
		
		ArrayList<String[]> sortedInput = splitInput(list);
		ArrayList<String> formatted = new ArrayList<String>();
		
		for(int i=1; i<list.size(); i++){
			String temp1 = sortedInput.get(i)[0];
			String temp2 = sortedInput.get(i)[1];
			String temp3 = sortedInput.get(i)[3];
			try{
				String temp4 = sortedInput.get(i)[4];
				formatted.add(temp1 + " ("+temp2+")"+", "+ temp3 + " ["+temp4+ "]");
			}catch(ArrayIndexOutOfBoundsException e){
				formatted.add(temp1 + " ("+temp2+")"+", "+ temp3 );
			}
		}
		for(String line : formatted){
			System.out.println(line);
		}
	}
	
	public static void main(String[] args) { 
//		if(args.length > 1){
//			System.out.println("Please only enter one argument on command line.");
//		}else{
		String userArgument = args[0];	//First argument from command line
		readCSV(userArgument);
		checkPatched(splitInput(content));
		checkVersion(splitInput(content));
		checkIPAddress(splitInput(content));
		checkName(splitInput(content));
		formatList(compareList(content, removedContent));
//		}	
	}
}
