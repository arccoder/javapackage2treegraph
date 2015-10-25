import json
import time

packageList = ["package1"];

for package in packageList:

    fileName = package;

    with open(fileName + ".txt") as f:
        content = f.readlines()

    content = [x.strip('\n') for x in content] 

    # Instantiate a dictionary to hold data
    data = {};

    # Process the data first
    mainDict = {}
    classList = []
    subList = []
    name = ''
    for line in content[1:]:
        line = line.split(',')
        
        if( len(line) < 2 ):
            continue;
        
        if( len(name) == 0 ):
            name = line[0]

        if( name == line[0] ):
            entry = ''
            for i in range(len(line)-1, 0, -1):
                entry += line[i] + ' '
            subList.append({'name':entry})
        else:
            mainDict['name'] = name
            mainDict['children'] = subList
            classList.append(mainDict)
            mainDict = {}

            name = line[0]
            subList = []
            entry = ''
            for i in range(len(line)-1, 0, -1):
                entry += line[i] + ' '
            subList.append({'name':entry})

    mainDict['name'] = name
    mainDict['children'] = subList     
    classList.append(mainDict)
    mainDict = {}

    # Process the path
    packagePath = content[0]
    packagePath = packagePath.split('.')

    for pack in reversed(packagePath):
        tmpData = {};
        tmpData["name"] = pack
        if( len(data) == 0 ):
            tmpData["children"] = classList
            data = tmpData
        else:
            tmpData["children"] = []
            tmpData["children"].append(data)
            data = tmpData

    # Dump the data in file
    with open( "www/" + package + ".json", "w") as text_file:
        text_file.write( json.dumps(data) )

