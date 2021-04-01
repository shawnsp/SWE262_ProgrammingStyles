* Import the library into your workspace (from maven or other means). Study the API (Links to an external site.). Follow the instructions and tutorials given in this link (Links to an external site.) and/or this link (Links to an external site.). You can, and should, find more tutorials and examples out there.
* Find at least 10 XML files, some small, some medium, some large, and at least one very large. At least one should have a very nested structure. Here is a small one (Links to an external site.) and here are some very large ones (Links to an external site.).
* Write an application program that exercises the library by doing the following tasks, all of which are tasks that many people might do in their programs:
  
  1. Read an XML file (given as command line argument) into a JSON object and write the JSON object back on disk as a JSON file.
  2. Read an XML file into a JSON object, and extract some smaller sub-object inside, given a certain path (use JSONPointer). Write that smaller object to disk as a JSON file.
  3. Read an XML file into a JSON object, check if it has a certain key path (given in the command line too). If so, save the JSON object to disk; if not, discard it.
  4. Read an XML file into a JSON object, and add the prefix "swe262_" to all of its keys.
  5. Read an XML file into a JSON object, replace a sub-object on a certain key path with another JSON object that you construct, then write the result on disk as a JSON file. 
