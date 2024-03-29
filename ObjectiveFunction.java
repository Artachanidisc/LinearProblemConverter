import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ObjectiveFunction {

	private int n = 20;
	private int m = 20;
	private int minMax[][] = new int[1][1];
	private double c[][] = new double[1][n]; // the limit in this example is 20 factors(you can adjust the number of factors as you please)
	private double A[][] = new double[m][n];
	private double b[][] = new double[m][1];
	private int Eqin[][] = new int[m][1];
	

	public void testFunction(ArrayList<String> functionArray) {
		int i = 0;
		int j = 0;
		String[] strs = null;
		int counterNonEmpty = 0;
		int nonEmpty[] = new int[n];
		int size = 0;
		n = 0;
		m = 0;
		int m1 = 0;
		int n1 = 0;
		int m2 = 0;
		int theCounter = 2;
		int counter = 0;
		int counter1 = 0;
		boolean symbolFound = false;


		for (i = 0; i < functionArray.size(); i++) {
			if (functionArray.get(i).trim().length() > 0) {
				nonEmpty[counterNonEmpty] = i;
				counterNonEmpty++;
			}
		}
		for (counter = 0; counter < functionArray.size() - 1; counter++) {
			if (counter == nonEmpty[0]) {
				strs = functionArray.get(nonEmpty[0]).split("\\s+");
				if (strs[0].equals("max")) {
					minMax[0][0] = 1;
				} else if (strs[0].equals("min")) {
					minMax[0][0] = -1;
				} else {
					System.out.println("Wrong from of objective function ");
				}
				size=strs.length;
				for (j = 1; j < size; j += 3) {
					try {
						Double aDouble = Double.parseDouble(strs[j]);
						if(j!=1&&! strs[j - 1].equals("-")&&! strs[j - 1].equals("+")) { 
							System.out.println("Incorrect input in line :" + counter++);
							break;
						}
						if (strs[j - 1].equals("-")) { 
							this.c[0][n] = -aDouble;
						} else {
							this.c[0][n] = aDouble;
						}
						n++;
					} catch (NumberFormatException ex) {
						System.out.println("Incorrect input in line :" + counter++);
						break;
					}				
				}
			} else if (counter == nonEmpty[1]) { 
				strs = functionArray.get(nonEmpty[1]).split("\\s+");
				if (!strs[0].equals("st") && !strs[0].equals("st") && !strs[0].equals("subject to")) {  
					System.out.println("Wrong input at st");
					break;
				}
				size = strs.length;
				for (counter1 = 1; counter1 < size; counter1++) { 
					if (strs[counter1].equals(">=")) {
						this.Eqin[m][0] = 1;
						symbolFound = true;
					} else if (strs[counter1].equals("<=")) {
						this.Eqin[m][0] = -1;
						symbolFound = true;
					} else if (strs[counter1].equals("=")) {
						this.Eqin[m][0] = 0;
						symbolFound = true;
					}
				}
				if (!symbolFound) {
					System.out.println("Symbol not found in line :" + ++counter);
					break;
				}
			} else if (counter > nonEmpty[1] ) {
				strs = functionArray.get(nonEmpty[theCounter-1]).split("\\s+");
				if( functionArray.get(nonEmpty[theCounter-1]).equals(functionArray.get(functionArray.size()-1))) {
					break;
				}
				size = strs.length;
				for (counter1 = 1; counter1 < size; counter1++) { 
					if (strs[counter1].equals(">=")) {
						this.Eqin[m][0] = 1;
						m++;
						symbolFound = true;
					} else if (strs[counter1].equals("<=")) {
						this.Eqin[m][0] = -1;
						m++;
						symbolFound = true;
					} else if (strs[counter1].equals("=")) {
						this.Eqin[m][0] = 0;
						m++;
						symbolFound = true;
					}
				}
				if (!symbolFound) {
					System.out.println("Symbol not found in line :" + counter++);
					break;
				}
				if (symbolFound) { 
					for (j = 1; j < size - 2; j += 3) {
						try {
							Double aDouble = Double.parseDouble(strs[j]);
							if(j!=1&&! strs[j - 1].equals("-")&&! strs[j - 1].equals("+")) {    
								System.out.println("Incorrect input in line :" + counter++);
								break;
							}
							if (j != 1 && strs[j - 1].equals("-")) {  
								this.A[m1][n1] = -aDouble;
							} else {
								this.A[m1][n1] = aDouble;
							}
							m1++;
							n1++;
						} catch (NumberFormatException ex) {
							System.out.println("Incorrect input in line :" + counter++);
							break;
						}
					}
					try { 
						Double aDouble = Double.parseDouble(strs[size - 1]);
						if(strs[size-2].equals("-")) {
							this.b[m2][0]=-aDouble;
						}
						else {
							this.b[m2][0] = aDouble;
						}			
						m2++;
					}
					catch (NumberFormatException ex){
						System.out.println("Incorrect input in line :" + counter++);
						break;
					}
				} else {
					System.out.println("Symbol not found in technical limitation 1");
				}
				theCounter++;
				symbolFound = false;
			}
		}	
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.print("DUAL PROBLEM:\n");
		writer.print("-------------------------------------------\n");
		if(minMax[0][0]==1) {
		writer.print("min");
	}
	else {
		writer.print("max ");
	}
		j=1;
		for (i = 0; i < m2; i++) {
			if(this.b[i][0]>0 && i>0) {
				writer.print("+");
			}
			writer.print((int) this.b[i][0] + "w"+j+" ");
			j++;
		}
		counter1=0;
		while(counter1<m2) {
		writer.print("\nst ");
		counter=1;
		for (i = 0; i < m1; i+=3) {
			for (j = 0; j < n1; j++) {
				if (this.A[i][j] != 0) {
					if(this.A[i][j]>0 && i>2) {
						writer.print("+");
					}
					writer.print((int) this.A[i][j] + "w"+counter+" ");
					counter++;
				}
			}
		}
		writer.print(" "+ ">=");
		writer.print("  "+(int) this.c[0][counter1]+"\n    ");
		counter1++;
		counter=1;
		for (i = 1; i < m1; i+=3) {
			for (j = 1; j < n1; j+=3) {
				if (this.A[i][j] != 0) {
					if(this.A[i][j]>0 && i>2) {
						writer.print("+");
					}
					writer.print((int) this.A[i][j] + "w"+counter+" ");
					counter++;
				}
			}
		}
		writer.print(" "+ ">=");
		writer.print("  "+(int) this.c[0][counter1]+"\n    ");
		counter1++;
		counter=1;
		for (i = 2; i < m1; i+=3) {
			for (j = 2; j < n1; j+=3) { 
				if (this.A[i][j] != 0) {
					if(this.A[i][j]>0 && i>2) {
						writer.print("+");
					}
					writer.print((int) this.A[i][j] + "w"+counter+" ");
					counter++;
				}
			}
		}
		writer.print(" "+ ">=");
		writer.print("  "+(int) this.c[0][counter1]+"\n    ");
		counter1++;
		}
		strs = functionArray.get(nonEmpty[counterNonEmpty-1]).split("\\s+");
		size = strs.length;
		j=0;
		try {
			for(i=0;i<size-1;i++) {
				if(strs[i].equals("<=")||strs[i].equals("=")||strs[i].equals(">=")) {
					break;
				}
				else {
					if(!strs[i].equals(",")) {
						j++;
					}
				}
			}
		}
		catch(Exception e) {
		}
		counter=1;
		if(minMax[0][0]==1) {
			for(i=0;i<m;i++) {
				if(Eqin[i][0]==0) {
					writer.print("w"+counter+" ελεύθερη  ");
					counter++;
				}
				else if(Eqin[i][0]==-1) {
					writer.print("w"+counter+" >=0  ");
					counter++;
				}
				else {
					writer.print("w"+counter+" <=0  ");
					counter++;
				}
			}
		}
		else {
			for(i=0;i<m;i++) {
				if(Eqin[i][0]==0) {
					writer.print("w"+counter+" ελεύθερη  ");
					counter++;
				}
				else if(Eqin[i][0]==-1) {
					writer.print("w"+counter+" <=0  ");
					counter++;
				}
				else {
					writer.print("w"+counter+" >=0  ");
					counter++;
				}
			}
		}
		writer.print("\n-------------------------------------------\n");
		writer.close();
		
		
		
	}
}
