# javapackage2treegraph
Displays a java package contents as a collapsible tree 

## Files

### jp2tg/src/MainClass.java
This class should be added at a layer above the package you want to graph.
This class outputs a json file used by the index.html file.

### jp2tg/src/package1
Package and the classes within it to be graphed.

### www/index.html
Display or shows how to graph a the package from json file.

### www/package1.json
This json file is created by 'java2jsonG.py'.

## Steps 
1. Insert the MainClass.java in your project.
2. Update the variables in MainClass.java with the right paths.
3. Run the java code.
4. Open the index.html. You should see the graph.

## Output
![](https://github.com/arccoder/javapackage2treegraph/blob/master/readme/graph.jpg)

## ToDo
Any suggestions?