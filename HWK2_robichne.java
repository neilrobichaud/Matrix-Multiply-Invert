/* This program takes matrices as input, multiplies them, and then inverts the product. */


public class HWK2 {
	 private static double [][][] arraybank;					//creating a 3D matrix to store arrays
	 private static int numMatrices;							//creating a variable for the first arg so it can be accessed outside main()
	 private static double[][] current;							//current will be the result after all matrix multiplication


	public static void main(String[] args) {							//auto generated main class
		

		numMatrices= Integer.parseInt(args[0]);				//takes the number of matrices and stores it in numMatrices
		int start_of_numbers= (numMatrices*2+1);				//Eg. if there are 7 matrices the start will be the 15th position

		//creates a list of dimensions
		int arrayind= 1; 										//starts arrayindex at 1 because 0th position is the number of matrices
		int[] dimlist = new int[2*numMatrices];					//list of dimensions of all matrices			
		for (int i=0; i<(numMatrices*2); i++){ 					//loops through twice the number of matrices
			dimlist[i]=Integer.parseInt(args[arrayind]);		//stores the height and width in list indexed one below arrayind because it starts at 0 not one				
			arrayind=arrayind+1;								//increments arrayind
		}

		//code to check for matrix multiplication error											
		for (int x=1;x < (numMatrices*2-1);x=x+2){				//loops from the second to second to last value
			if (dimlist[x]!=dimlist[x+1]){						//checks if NxM!=MxB
				System.out.println("Multiplication error.");		//if  not equal prints multiplication error		
				System.exit(0);												//breaks program			
							
			}
		}


		//creates 2D matrices out of the args and stores them in a 3D array
		arraybank= new double[numMatrices][][];		//creates a 3D array to store all 2D arrays		
		for (int i=0; i<numMatrices; i++){							//loop through for each matrix
			int N= dimlist[i*2];									//sets variable N to the index of a row value in dimlist
			int M= dimlist[i*2+1];									//sets variable M to the index of the corresponding column value in dimlist				
			double [][] c= new double [N][M]; 						//new empty 2D arrray of NxM
			for (int row = 0; row < N; row++){						//loop through for each row
				for (int col = 0; col < M; col++){					//loop through for number in that row
					c[row][col]=Integer.parseInt(args[start_of_numbers]);	//add this value into its place in c, using start_of_numbers as a bookmark for the args
					start_of_numbers++;								//increment start_of_numbers in order to keep using it as a book mark for the args


				}
			}
			arraybank[i]=c;											//adds the completed matrix to the arraybank			
		}
		
		current = arraybank[0]; 											//sets current matrix by multiplying the first two matrices in the arraybank

				
		for (int p=1;p<numMatrices;p++){   									//loops through arraybank starting at the second matrix
			double [][] next=arraybank[p];									//sets next to the next matrix in the list						
			current=matrixMultiply(current, next); 							//multiplies the current matrix by the next matrix and stores the result into current				
		}
		
	
		double ourdeterminant=runDetCheck(current);		
		if (ourdeterminant==0){												//if the determinant is 0 or the matrix is not nxn 
			System.exit(0);															//the program prints matrix not invertable and stops
		}
		if (rows(current)==2){													//if the matrix is 2x2
			double [][] inverse=base2Inverse(current);							//calls base2inverse which uses shortcut formula
			System.out.println(Math.round( (inverse[0][0])*100.0)/100.0);		//prints one value of the inverse
			System.out.println(Math.round( (inverse[0][1])*100.0)/100.0);		//prints one value of the inverse
			System.out.println(Math.round( (inverse[1][0])*100.0)/100.0);		//prints one value of the inverse
			System.out.println(Math.round( (inverse[1][1])*100.0)/100.0);		//prints one value of the inverse
			System.exit(0);														//breaks the loop
		}
		double[][] adjoint =transpose(signChanger(matrixOfMinors(current)));	//the adjoint is the transpose of the cofactor matrix
		double[][] inverse= new double[cols(adjoint)][cols(adjoint)];			//inverse is an empty matrix of size adjoint
		for (int i=0;i<rows(adjoint);i++){										//loop for rows in adj
			for(int j=0;j<cols(adjoint);j++){									//loop for cols in adj
				inverse[i][j]=(adjoint[i][j]/ourdeterminant);	//rounds the value of the inverse
				System.out.println(Math.round(inverse[i][j]*100.0)/100.0);									//prints the value with a 
			}
		}
		
		
	}
		
		
	
		

	
	
		//after this loop has run, current contains the multiplication of all the input matrices 
	
	public static double runDetCheck(double[][] g){					//function to check whether a matrix has a determinant
				
		if (cols(current) != rows(current)){						//checks if matrix is NxN
			System.out.println("Matrix not invertable");			//if it is not NxN prints "matrix not invertable
			return 0;												//returns 0 since it must return a number
		}
		else{													//else..
			return findDeterminant(current);					//if the matrix is NxN and the determinant is not 0						
		}							
		
		
	}
	
	public static double[][] transpose(double[][]u){				//function to transpose any matrix
		double [][]k=new double [cols(u)][rows(u)];					//creates new nxn matrix
		for (int i=0;i<cols(u);i++){								//loops for each row
			for (int j=0;j<rows(u);j++){							//loops for each column
				k[j][i]=u[i][j];									//puts the number in the transposed place in matrix k
			}
		}
		return k;													//returns k
	}


	public static double[][] matrixOfMinors(double[][] p){			//function that finds all the minors of a matrix
		double [][] q=new double [rows(p)][cols(p)];				//creates a new empty matrix
		for (int i=0;i<rows(p);i++){								//loops for each row
			for(int j=0;j<cols(p);j++){								//loops for each column
				double [][] minor=findMinor(p,i,j);		//find the minor at i,j
				if (rows(minor)==2){					//if the minor is 2x2
					q[i][j]=q[i][j]+base2Case(minor);	//calculate the determinant and add it to q[i][j]
				}
				else{					
					minor=matrixOfMinors(minor);		//if not 2x2 find the matrix of minors of that matrix
				}	
			}}
		return q;}											//returns the matrix
						
	
	public static double[][] signChanger (double[][]u){				//function to change the signs of a matrix
		double [][]k=new double [cols(u)][rows(u)];					//creates new nxn matrix
		int sign=1;
		for (int i=0;i<cols(u);i++){								//loops for each row
			for (int j=0;j<rows(u);j++){							//loops for each column
				k[i][j]=u[i][j]*sign;								//multiplies the number by an alternating sign variable
				sign*=-1;											//alternating sign variable
			}
		}
		return k;													//returns k
	}
	
	
	
	
	
	
	
	public static int rows(double[][] a) {  									//function that returns the number of rows in a 2D array			
		return a.length;														//returns the number of rows
	}
	public static int cols(double[][] b) {										//function that returns the number of columns in a 2D array
		return b[0].length;														//returns the length of the first row
	}		


	public static double [][] matrixMultiply (double[][] a, double[][] b){		//function that multiplies two matrices
		double[][] c = new double[rows(a)][cols(b)];							//creates an empty matrix that has dimensions AxB where the original two are AxN * NxB
		for (int i = 0; i < rows(a); i++){										//loops for each row of a
			for (int j = 0; j < cols(b); j++){									//loops for each col of b
				for (int k = 0; k < cols(a); k++){								//loops through individual elements in a row of a
					c[i][j] += a[i][k] * b[k][j];								//places the sum of the multiplications into the empty matrix
				}
			}
		}	
		return c;																//returns the AxB matrix		
	}

	public static double base2Case (double[][] t){								//function that computes the determinant of a 2x2 matrix
		return t[0][0]*t[1][1]-t[1][0]*t[0][1];									//ad-bc returns value
	}
		
	public static double [][] base2Inverse (double[][] g){						//calculates the inverse of a 2x2 using the shortcut method
		double [][] p=new double [2][2];										//placeholder matrix
		double thisdeterminant= base2Case(g);									//get the determinant
		p[0][0]=g[0][0];														//copies a to p		
		g[0][0]=g[1][1]/thisdeterminant;										//sets a to d, divides by determinant
		g[1][1]=p[0][0]/thisdeterminant;										//sets d to a, getting a from p,divides by determinant
				
		g[0][1]=g[0][1]*(-1.0)/thisdeterminant;									//mult b by -1 ,divides by determinant
		g[1][0]=g[1][0]*(-1.0)/thisdeterminant;									//mult c by -1 ,divides by determinant
		return g;																//returns finished matrix
	}

	public static double findDeterminant (double[][] d){											//finds the determinant of a matrix
		double determinant=0;																		//determinant starts at 0
		int sign=1;																					//sign value starts +
		if (rows(d)==2){																			//if its a 2x2
			return base2Case(d);
		}
		else{																						//else
			for (int column = 0; column < cols(d); column++) {										//for each column
				double[][] submatrix = findMinor(d,0,column);										//creates a submatrix of d, by expanding along the first row calling findMinor
				if (rows(submatrix) == 2){															//if the submatrix is 2x2
					determinant=determinant + d[0][column]*sign*base2Case(submatrix);				//the determinant is the value*sign value* the(ad-bc)
					sign*=-1;																		//sign is incremented
				}
				else																				//else
				{
					determinant = determinant + sign*d[0][column]*findDeterminant(submatrix);		//if the matrix is not 2x2 the function recursively calls findDeterminant
					sign*=-1;																		//sign increments
				}
			}				
		return determinant;																		//returns determinant						
		}
	}
	

	
	
	public static double [][] findMinor (double [][] x, int i, int j){					//finds the minor of the input at [i][j]
		double [][] minor= new double [rows(x)-1][cols(x)-1];							//creates an empty matrix with dimensions n-1 x n-1
		
		
		int icounter=0;																	//creates/resets variable icounter	
		for (int p=0; p<rows(x); p++){													//loops through all rows of x		
		int jcounter=0;																	//creates/resets variable jcounter

			for (int q=0; q<cols(x) ;q++){												//loops through all cols of x		

				if ((p != i) && (q!=j)){												//if the index is not part of the excluded row and column
					minor[p-icounter][q-jcounter]=x[p][q];								//places the value into the matrix using icounter and jcounter to correct the index since minor is smaller than x
					
				}
				else if (p == i){														//if the loop comes across a value in the ignored row, the entire row will be ignored anyway
					icounter++;															//icounter is incremented to indicate that the ignored row has been found.
					q=cols(x)+1;														//An ignored row has been found and the accumulator is increased to stop the loop
				}
				else if (q==j){															//if an ignored column is found
					jcounter++;															//jcounter is used to correct the index when an ignored column is found
				}
			}
		}
		return minor;																	//returns the minor
	}}
	


