# javapackage2treegraph
Displays a java package contents as a collapsible tree 

## Files

### jp2tg/src/MainClass.java
This class should be added at a layer above the package you want to graph.
This class outputs a file called 'package.txt' in your project folder.

### jp2tg/src/package1
Package and the classes within it to be graphed.

### java2jsonG.py
Uses the 'package.txt' bundle the package info in json format to graph the data.

### www/index.html
Display or shows how to graph a the package from json file.

### www/package1.json
This json file is created by 'java2jsonG.py'.

## Steps 
1. Insert the MainClass.java in your project.
2. Update the variables in MainClass.java with the right paths.
3. Run the java code.
4. Run java2jsonG.py
5. Modify the index.html file with the appropriate json file path.
6. Open the index.html. You should see the graph.

## ToDo