/**
 * Compile using: javac Sorter.java
 * Run using: java Sorter input_file_name number_of_entries mode output_file_name
 * For example (numeric): java Sorter input1.txt 5 numeric output.txt
 * For example (text): java Sorter input1.txt 5 text output.txt
 * 
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Sorter {

    public static void main(String[] args)
    {
        
        if (args == null | args.length == 0 || args.length < 4) 
        {
            System.out.println("Usage: Sorter <input_file_name> <number_of entries> <mode> <output_file_name>\n");
            System.exit(1);
        } else
        if (args[0].equals("-h") || args[0].equals("--help")) 
        {
            System.out.println("Usage: Sorter <input_file_name> <number_of entries> <mode> <output_file_name>\n");
            System.exit(1);
        } else
        if (args[0].equals("") || // input_file_name
            args[1].equals("") || // number_of_entries
            args[2].equals("") || // mode
            args[3].equals(""))   // output_file_name
        {
            System.out.println("Usage: Sorter <input_file_name> <number_of entries> <mode> <output_file_name>\n");
            System.exit(1);
        }
        // Other error messages
        int numOfEntries = 0;
        try 
        {
            numOfEntries = Integer.parseInt(args[1]);  // For checking if integer
            if (numOfEntries < 1)
            {
                System.out.println("Number of entries should be positive.\n");
                System.exit(1);
            } 
        } catch (NumberFormatException numberFormatException)
        {
            System.out.println("Number of entries should be a positive number.\n");
            System.exit(1);
        }
        
        if (!(args[2].equals("numeric") || args[2].equals("text")))
        {
            System.out.println("Mode must be numeric.\n");
            System.exit(1);
        }

        Sorter sorter = new Sorter();
        int[] integerArray = new int[numOfEntries];     // contents if mode is numeric
        String[] strArray = new String[numOfEntries];   // contents if mode is texts
        boolean isModeNumeric = false;          // Flag for mode
        try
        {
            strArray = sorter.getListFromInputFile(args[0]);
            if (args[2].equals("text"))
            {
                isModeNumeric = false;
                sorter.mainMergeSort(isModeNumeric, null, strArray, 0, strArray.length - 1);   
            } else
            if (args[2].equals("numeric"))
            {
                isModeNumeric = true;
                integerArray = sorter.convertStringArrayToInteger(strArray);
                sorter.mainMergeSort(isModeNumeric, integerArray, null, 0, integerArray.length - 1);
            }
            String formattedContent = ""; 
            if (isModeNumeric)
            {
                formattedContent = sorter.formatContent(isModeNumeric, integerArray, null);
            } else
            {
                formattedContent = sorter.formatContent(isModeNumeric, null, strArray);
            }
            // This where arrays are written into the file
            sorter.writeToOutputFile(formattedContent, args[3]);

        } catch (ArrayIndexOutOfBoundsException arrayException)
        {
            throw new ArrayIndexOutOfBoundsException("Error in index on command line.\n");
        } catch (IOException ioException) 
        {
            System.err.println("Error on opening or writing to file.\n");
        }

    }
    /* Formats mergeSorted content for writing to output files 
     * 
     * @param isNumeric - mode; can be either numeric or text
     * @param unformattedArrIntegers - unformatted contents in integer array form; null if mode is text
     * @param unformattedArrStrings - unformatted contents in String array form; null if mode is numeric
     * @return - formatted content ready to be written to output file
    */
    private String formatContent(boolean isNumeric, int[] unformattedArrIntegers, 
        String[] unformattedArrStrings)
    {
        StringBuilder sbContent = new StringBuilder();
        if (isNumeric)
        {
            for (int index = 0; index < unformattedArrIntegers.length; index++)
            {
                sbContent.append(unformattedArrIntegers[index]);
                sbContent.append("\n");
            }   
        } else
        {
            for (int index = 0; index < unformattedArrStrings.length; index++)
            {
                sbContent.append(unformattedArrStrings[index]);
                sbContent.append("\n");
            }
        }
        
        return sbContent.toString();
    }

    /* Generates a converted array of strings to integer for merging 
     * 
     * @param arrStrings - contents in String array form that needs to be converted
     * @return - converted contents in integer array form
    */
    private int[] convertStringArrayToInteger(String[] arrStrings)
    {
        int[] arrIntegers = new int[arrStrings.length];

        for (int index = 0; index < arrStrings.length; index++)
        {
            arrIntegers[index] = Integer.parseInt(arrStrings[index]);
        }
        return arrIntegers;
    }

    /* Retrieves the contents from the input file 
     * 
     * @param inputFilePathString - input filename from the command line
     * @return - contents from the input file 
     * @throws - IOException - error handling for file I/O 
    */
    private String[] getListFromInputFile(String inputFilePathString) throws IOException
    {
        try
        {
            Path currentDirectory = Paths.get(""); 
            String inputFile = currentDirectory + inputFilePathString;
            
            Path inputPath = Paths.get(inputFile);
            
            //Put in the list all the contents
            List<String> contents = Files.readAllLines(inputPath);
            String[] arrStrings = contents.toArray(new String[0]);
            return arrStrings;
            
        } catch (IOException ioException)
        {
            throw new IOException("Error in opening the file.\n");
        }
        
    }

    /* This is where the main writing to file is done 
     * 
     * @param formattedMergeSortedItems - formatted array items that are mergesorted already
     * @param outputFileString - output filename from the command line
     * @throws - IOException - exception handling for file I/O errors
    */
    private void writeToOutputFile(String formattedMergeSortedItems, String outputFileString) throws IOException
    {
        try
        {
            Path currentPath = Paths.get("");
            String outputFile = currentPath + outputFileString;
            
            Path outputPath = Paths.get(outputFile);
            byte[] contentsToWrite = formattedMergeSortedItems.getBytes();
            
            // Write to output file; CREATE to create file if non-existent;
            Files.write(outputPath, contentsToWrite, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      
        } catch (IOException ioException)
        {
            throw new IOException("Error in writing to file\n");
        }
    }
  
    /* This is where the initial and main merging is done 
     *
     * @param isNumeric - mode; can be either numeric or text
     * @param argMergeList - integer array if mode is numeric; null if mode is text
     * @param arrMergeStringList - String array if mode is text; null if mode is numeric
     * @param top - the top item in the array (the minimum after mergesorted)
     * @param bottom - the bottom item in the array (the highest after mergesorted)
     * @throws - ArrayIndexOutOfBoundsException - exception handling for arrays
    */
    private void mainMergeSort (boolean isNumeric, int[] arrMergeList, String[] arrMergeStringList, 
            int top, int bottom) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            // To make sure merging is not mixed
            if (isNumeric)
            {
                arrMergeStringList = null;
            } else
            {
                arrMergeList = null;
            }
           
            // Main now
            if (top < bottom)
            {
                // Get the middle point
                int middle = top + (bottom - top) / 2;

                // Loop recursively the first half and the second half
                mainMergeSort(isNumeric, arrMergeList, arrMergeStringList, top, middle);
                mainMergeSort(isNumeric, arrMergeList, arrMergeStringList, middle + 1, bottom);
                
                // Now merge the sorted halves
                merge(isNumeric, arrMergeList, arrMergeStringList, top, middle, bottom);
            }
        } catch (ArrayIndexOutOfBoundsException arrayException)
        {
            throw new ArrayIndexOutOfBoundsException("Error in array length.\n");
        }
        
    }

    /* This is where merging and sorting of two halves is done 
     * 
     * @param isNumeric - mode; can be either numeric or text
     * @param arrMergeList - integer array if mode is numeric; null if mode is text
     * @param arrMergeStringsList- String array if mode is text; null if mode is numeric
     * @param top - the top item in the array (the minimum after mergesorted)
     * @param middle - the middle item in the array (the middle after mergesorted)
     * @param bottom - the bottom item in the array (the highest after mergesorted)
     * @throws - ArrayIndexOutOfBoundsException - exception handling for arrays
    */
    private void merge(boolean isNumeric, int[] arrMergeList, String[] arrMergeStringsList,
        int top, int middle, int bottom) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            //System.out.println("sub merge in:" + top + ":" + middle + ":" + bottom);
            int midTop = middle - top + 1;
            int bottomMid = bottom - middle;

            // Temp arrays to store the 2 halves (numeric)
            int[] arrTop = new int[midTop];
            int[] arrBottom = new int[bottomMid];

            // Transfer data to temp array (numeric)
            if (isNumeric)
            {
                for (int a = 0; a < midTop; a++)
                {
                    arrTop[a] = arrMergeList[top + a];
                    
                }
                for (int b = 0; b < bottomMid; b++)
                {
                    arrBottom[b] = arrMergeList[middle + 1 + b];
                }
            }
            // Temp arrays to store the 2 halves (text)
            String[] arrStringTop = new String[midTop];
            String[] arrStringBottom = new String[bottomMid];

            // Transfer data to temp array (text)
            if (!isNumeric)
            {
                System.arraycopy(arrMergeStringsList, top, arrStringTop, 0, midTop);
                System.arraycopy(arrMergeStringsList, middle + 1, arrStringBottom, 0, bottomMid);
            }
            // Now merge the temp arrays to the main array
            int a = 0;  // Index for subarray1
            int b = 0;  // Index for subarray2
            int c = top;  // Index for the merged subarrays

            while (a < midTop  && b < bottomMid)
            {
                if (isNumeric)
                {
                    if (arrTop[a] <= arrBottom[b])
                    {
                        arrMergeList[c] = arrTop[a];
                        a++;
                    } else
                    {
                        arrMergeList[c] = arrBottom[b];
                        b++;
                    }
                    c++;
                } else // For "text"
                {
                    if (arrStringTop[a].compareTo(arrStringBottom[b]) <= 0)
                    {
                        arrMergeStringsList[c] = arrStringTop[a];
                        a++;
                    } else
                    {
                        arrMergeStringsList[c] = arrStringBottom[b];
                        b++;
                    }
                    c++;
                }
                    
                
            } // end if while

            // Copy the remaining elements
            while (a < midTop) 
            {
                if (isNumeric)
                {
                    arrMergeList[c] = arrTop[a];
                } else
                {
                    arrMergeStringsList[c] = arrStringTop[a];
                }
                
                a++;
                c++; 
            }
            while (b < bottomMid)
            {
                if (isNumeric)
                {
                    arrMergeList[c] = arrBottom[b];
                } else
                {
                    arrMergeStringsList[c] = arrStringBottom[b];
                }
                
                b++;
                c++;
            }
        } catch (ArrayIndexOutOfBoundsException arrayException)
        {
            throw new ArrayIndexOutOfBoundsException("Error on array index.\n");
        }
    } // end of merge
    
    
}
