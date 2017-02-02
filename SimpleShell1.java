import java.io.*;
import java.util.ArrayList;

public class SimpleShell1 {
public static void main(String[] args) throws java.io.IOException {
	String commandLine;
	BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
	File currentDirectory= new File(System.getProperty("user.dir"));
	ArrayList<String> historyArray = new ArrayList<String>();
	while (true) {
		// read what the user entered
		System.out.print("jsh>");
		commandLine = console.readLine();
		String[] commandList = commandLine.split(" ");
		
		// TODO: adding a history feature
		if (commandLine.equals("history")){
			// go through the historyArray that was saved previously, and print each element
			for(int j=0;j<historyArray.size();j++){
				System.out.println(j+" "+historyArray.get(j));
			}
			continue;
		}

		// if the user entered a return, just loop again
		if (commandLine.equals("")) {
			continue;
		}

		// TODO: creating the external process and executing the command in that process
		// TODO: modifying the shell to allow changing directories
		
		if (commandLine.contains("cd")){
			//change directory back to home if there's nothing after cd
			if(commandList[(commandList.length-1)].equals("cd")){

				String currentPath = System.getProperty("user.home");
				File f = new File(currentPath);
				currentDirectory = f;
			}else if(commandList[1].equals("..")){ // back to parent file if ".."
				String[] newParentArray = currentDirectory.getPath().split("/");
				String newParent = "";
				for (int i=0;i<(newParentArray.length-1);i++){
					newParent +=newParentArray[i]+"/";
				}
				File f = new File(newParent);
				currentDirectory = f;
			}else{ //change directory to the specified file name
				String newDirectory = currentDirectory.getPath()+"/"+commandList[1];
				if(new File(newDirectory).exists()){
					File f = new File(newDirectory);
					currentDirectory= f;
				}
				else{ //everything else not specified above
					System.out.println("Error: new directory is not valid! - "+newDirectory);
					continue;
				}
			}
		}else{
			try{
				ProcessBuilder pb = new ProcessBuilder();
				if (commandLine.equals("!!")){ //repeat the history before
					pb.command(historyArray.get(historyArray.size()-1).split(" "));
					//continue;
					commandLine = historyArray.get(historyArray.size()-1); //dont put back the !! into history

					//System.out.println("printhere size="+historyArray.size());
				}else if(commandLine.matches("\\d+")){ //if integer value
					pb.command(historyArray.get(Integer.parseInt(commandLine)));
					commandLine = historyArray.get(historyArray.size()-1);
					//dont put back the integer value into history
				}
				else{
					pb.command(commandList); 
				}
				pb.directory(currentDirectory);
				Process p = pb.start();
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String cOutput;
				while((cOutput = br.readLine())!= null){
					System.out.println(cOutput);
				}
				br.close();
			}
			catch(Exception e){ //error line
				//e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}//end else
		historyArray.add(commandLine);
	}//end while
}//end main
}//end class
	
