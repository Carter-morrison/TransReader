package com.java.project.g3.transprog;

import java.util.Scanner;
import java.text.DecimalFormat;
import java.io.*;
import java.util.Random;

public class TransProcessor
{
	String path;

	public TransProcessor(String asjdn)
	{
		this.path = asjdn;
	}

	public enum Operators
	{
		CRP, FRE, RDI, MSR, MSQ, MRI, MDB;
	}

	// DLM = delimeter, FRE = for each loop, RDI = random integer, MSR = math square
	// root, MSQ = math square, MRI = math random int
	// -----------------------------------------------------------------
	// Reads urls from a file and prints their path components.
	// -----------------------------------------------------------------

	public String toString()
	{
		int negativeCount = 0;
		String delim = "%";
		int commandNumber = 0;
		int errorcount = 0;
		Random generator = new Random();
		String name = "";
		Scanner scan = new Scanner(System.in);
		DecimalFormat df = new DecimalFormat("0.##");
		String url;
		Scanner fileScan = null, urlScan;
		try
		{
			fileScan = new Scanner(new File(path));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter outfile = null;// Catches if the file is not found and continues the code
		try
		{
			outfile = new PrintWriter("D:\\TransData.txt");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Catches if the file is not found and continues the code
		PrintWriter errorfile = null;
		try
		{
			errorfile = new PrintWriter("D:\\ErrorData.txt");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Catches if the file is not found and continues the code
			// Read and process each line of the file
		while (fileScan.hasNext())
		{
			commandNumber++;
			url = fileScan.nextLine();
			urlScan = new Scanner(url);
			urlScan.useDelimiter(delim);
			while (urlScan.hasNext())
			{
				int checks = 0;
				int checked = 0;
				String urlCopy = urlScan.next();
				System.out.println("Command #" + commandNumber + ": " + url); // prints out the line# and original code
				for (Operators f : Operators.values())// Does a for loop to check all values against the enum
				{

					if (url.substring(0, 3).equals("DLM") && commandNumber == 1)// Checks to see if they pass a
																				// delimeter
					{
						delim = url.substring(3);
						break; // if they do pass a delimeter it sets it to the new one
					}
					if (url.substring(0, 3).equals("DLM") && commandNumber > 1) // if they pass a delimeter but its
					{ // not on the first line it gives an error
						System.out.println("Error: Misplaced Delimiter Statement");
						errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
								+ " \"): Misplaced Delimiter Statement.");
						errorcount++;
						break;
					}
					checks++;
					if (!(urlCopy.substring(0, 3).equals(f.toString())) && checks > 6 && checked >= 1)
						break;
					if (!(urlCopy.substring(0, 3).equals(f.toString())) && checks > 6) // if it loops through the enum
																						// and
																						// doesnt match
					{ // it presents an error
						System.out.println("Error: Command was not recognized");
						errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
								+ " \"): Command was not recognized.");
						errorcount++;
						break; // breaks out of loop
					}

					if (urlCopy.substring(0, 3).equals(f.toString()))
						switch (urlCopy)
						{
						case "FRE":
							int freNum = urlScan.nextInt();
							if (urlScan.hasNext())
							{
								String freLoop = urlScan.next(); // Checks to make sure that parms are passed in
								for (int i = 0; i < freNum; i++) // then executes the for loop printing the string
									System.out.println(freLoop); // the specified amount of times
								checked++;
								break;
							} else // if it doesnt have a parm it will break out and print an error
							{
								errorcount++;
								checked++;
								System.out.println("Error!: No bound detected");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \"): No bound detected.");
								break;
							}

						case "RDI":
							if (urlScan.hasNextInt() && urlScan.hasNextInt()) // double checks if it contains parms
							{
								int rdiNum2 = 0;
								int rdiNum = urlScan.nextInt();
								if (urlScan.hasNextInt())
								{
									checked++;
									rdiNum2 = urlScan.nextInt();
								} else// breaks out if no parms
								{
									System.out.println("No Number bound detected!");
									errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
											+ " \"): No number bound detected.");
									errorcount++;
									checked++;
									break;
								}
								negativeCount = 0;
								for (int i = 0; i < url.length(); i++) // does a for loop to count the negative signs
								{ // that way it can set the bounds correctly
									if (url.substring(i, i + 1).equals("-"))
									{
										negativeCount++;
									}
								}
								if (url.substring(4, 5).equals("-") && negativeCount == 1)// sees if the first number is
																							// negative
								{
									int rdiRand = generator.nextInt((rdiNum2) - 1 - rdiNum) + rdiNum;
									System.out.println(+rdiRand);
								}
								if (negativeCount == 2)// sees if both numbers are negative
								{
									int rdiRand = generator.nextInt((-1) * rdiNum - 1 - rdiNum2) - rdiNum2;
									System.out.println("-" + rdiRand);
								}
								if (negativeCount == 0)// default if neither are negative
								{
									int rdiRand = generator.nextInt(rdiNum2) + rdiNum;
									System.out.println(rdiRand);
								}
							} else// if not enough parms are passed in it will break out of the switch
							{
								System.out.println("Error!: No number bound detected");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \"): No number bound detected.");
								errorcount++;
								checked++;
								break;
							}
							break;
						case "MRI":
							int random = 0;
							int min = urlScan.nextInt();
							if (urlScan.hasNextInt())// checks for parms
							{
								checked++;
								int max = urlScan.nextInt();

								if (urlScan.hasNextInt())// checks for the last parm
								{
									checked++;
									int mriNum3 = urlScan.nextInt();
									for (int i = 0; i < mriNum3; i++)
									{
										int range = (max - min) + 1;
										random = (int) (Math.random() * range) + min;
										System.out.print(" " + random); // prints out random values a specific amount
																		// of times

									}
									System.out.println();
								} else// if only two numbers are passed
								{
									System.out.println("Error!: No number bound detected");
									errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
											+ " \"): No number bound detected.");
									checked++;
									errorcount++;
									break;
								}
							} else// if only one number is passed
							{
								System.out.println("Error!: No number bound detected");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \"): No number bound detected.");
								checked++;
								errorcount++;
								break;
							}
							break;
						case "MSR":
							int sqroot = 0;
							if (urlScan.hasNextInt())// if no numbers are passed in it will break from statement
							{
								checked++;
								sqroot = urlScan.nextInt();
								System.out.println((int) Math.sqrt(sqroot));
							} else
							{
								System.out.println("Error!: No number bound detected");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \"): No number bound detected.");
								errorcount++;
								checked++;
								break;
							}
							break;
						case "MSQ":
							int sqred = 0;
							if (urlScan.hasNextInt())
							{
								checked++;
								sqred = urlScan.nextInt();
								sqred = (int) Math.pow(sqred, 2);
								System.out.println(sqred);
							} else
							{
								System.out.println("Error!: No number bound detected");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \"): No number bound detected.");
								errorcount++;
								checked++;
								break;
							}
							break;
						case "CRP":
							String firstChar = urlScan.next(); // I use string variables to check for characters
							char char1 = firstChar.charAt(1);// that way if they pass in invalid parameters
							if (urlScan.hasNext())
							{ // the code wont break
								String replacer1 = urlScan.next();// Then I set a character = to the first letter in the
								char replacer = replacer1.charAt(1);// String
								if (urlScan.hasNext())
								{ // Makes sure a second parameter is passed in
									checked++;
									String mutator = urlScan.next();
									String mutation = mutator.replace(char1, replacer); // the actual
																						// replacement/mutation
									System.out.println(mutation);
								} else
								{
									System.out.println("Error no bounds detected.");
									errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
											+ " \") : No bounds detected.");
									errorcount++; // counts error and breaks out of code if no number bound is detected
									checked++;
									break;
								}

							} else
							{
								System.out.println("Error no bounds detected.");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \") : No bounds detected.");
								errorcount++;
								checked++;
								break;
							}
							break;
						case "MDB":
							double firstDouble = 0;
							if (urlScan.hasNextDouble())
							{
								firstDouble = urlScan.nextDouble();
							}
							if (urlScan.hasNextDouble())
							{ // Checks if another number bound is there
								double secondDouble = urlScan.nextDouble();
								System.out.println(df.format(firstDouble * secondDouble));
								// Formats the doubles that are multiplied together
							} else
							{
								System.out.println("Error: No number bound detected.");
								errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
										+ " \"): No number bound detected.");
								errorcount++;
								break;
								// prints out error and breaks out when no number is detected
							}
							break;
						default:
							System.out.println("Error: Command was not recongnized");
							errorfile.println("Error @ Command #" + commandNumber + " (\" " + url
									+ " \") : Command was not recognized.");
							errorcount++;
							checked++;
							break;
						}
				}
				outfile.println(name + " " + urlCopy);
				break;

			}

		}
		System.out.println("**************************\n      Summary     \n ErrorCount: " + errorcount
				+ "\n NumberOfCommands: " + commandNumber + "\n Percent Error: %"
				+ ((((double) errorcount / (double) commandNumber)) * 100)); // attatches the summary to the bottom of
																				// the
																				// run
		fileScan.close();
		scan.close();
		outfile.close();
		errorfile.close();
		return " ";
	}
}