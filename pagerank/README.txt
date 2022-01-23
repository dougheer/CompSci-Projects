Tokenizer README

The dependencies for the code are the java.util.HashMap, java.util.Map, java.util.Set, java.util.ArrayList, java.util.HashSet, 
java.io.BufferedReader, java.io.File, java.io.FileInputStream, java.util.zip.GZIPInputStream, java.io.PrintWriter, java.io.FileWriter, and java.io.InputStreamReader,
java.io.FileNotFoundException, java.io.IOException, and java.io.UnsupportedEncodingException.

To build the code you need the mentioned classes above along some complier that uses the java language as the project was constructed in java.
The code itself is comprised of 2 classes a main method and the pagerank class.

To run it you put both the main method class and the pagerank class into a java compiler. Then in the main method you modify
the parameter string in the pagerank declaration with a lambda value, a tau value and the data you want to rank in the form of a graph
Then you compile and run the code and the output of the pagerank and the inlink rank will be sent to 2 text files.
