import java.io.IOException;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;

import java.util.List;
import java.util.Arrays;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JavaPackage2TreeGraph {
	
	// http://docs.oracle.com/javase/7/docs/api/constant-values.html
	private String modifierArray[] = {	"strict","abstract","interface","native",
							  			"transient","volatile","synchronized",
							  			"final","static","protected","private","public"};
		
	private String getStrModifier(int modifierNum)
	{
		if( modifierNum <= 0 )
			return "";
			
		String modifierBin = Integer.toBinaryString(modifierNum);
		
		int numBits = modifierBin.length();
		int offsetBin = 12 - modifierBin.length();
		
		String modiferStr = "";
		for(int i = 0; i < numBits; i++)
			if( modifierBin.charAt(i) == '1' )
				modiferStr += modifierArray[offsetBin + i] + " ";

		modiferStr.substring(0, modiferStr.length()-2);
		
		return modiferStr;
	}
	
	private Vector<String> getClassInPackage(String packagePath)
	{
    	File folder = new File( packagePath );
    	File[] listOfFiles = folder.listFiles(new FilenameFilter() {
    	    public boolean accept(File dir, String name) {
    	        return name.toLowerCase().endsWith(".java");
    	    }
    	});
    	
    	Vector<String> classNames = new Vector<String>();
    	
    	for (int i = 0; i < listOfFiles.length; i++) 
    		if (listOfFiles[i].isFile()) 
    			classNames.add( listOfFiles[i].getName().split("\\.")[0] );

    	return classNames;
	}
	
	public static int[] range(int start, int stop, int step)
	{
		int len = Math.abs(stop-start);
		int[] result = new int[len];
		
		for(int i = 0; i < len; i++)
			result[i] = start + i*step;
		
		return result;
	}
	
	public void getPackageContents( String projectPath, String folderPath, String[] packageList, String jsonPath)
	{
    	for(int p = 0; p < packageList.length; p++)
    	{	    	
	    	Vector<String> packClass = getClassInPackage( projectPath + folderPath + packageList[p] );
			
	    	List<String> packDetails = new ArrayList<String>();
	    	
	    	for (int i = 0; i < packClass.size(); i++) 
	    	{    		
		    	@SuppressWarnings("rawtypes")
				Class c = null;
				try {
					String classPath = packageList[p] + "." + packClass.get(i);
					c = Class.forName( classPath );
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				if( c == null )
					continue;
				
		    	Field[] allFields = c.getDeclaredFields();
		        for (Field field : allFields) 
		        {
		        	String className = field.getDeclaringClass().toString();
		        	String[] tokens = className.split("\\.");
		        	className = tokens[tokens.length-1];
				       
		        	packDetails.add(className + ',' +
		        			  		field.getName() + ',' +
		        			  		field.getType().getCanonicalName() + ',' +
		        			  		getStrModifier(field.getModifiers()));
		        }
	
		        Member[] allMembers = c.getDeclaredMethods();
		        for (Member member : allMembers) 
		        {
		        	String memberName = member.getDeclaringClass().toString();
		        	String[] tokens = memberName.split("\\.");
		        	memberName = tokens[tokens.length-1];
		        	
		        	packDetails.add(memberName + "," +
		        			  		member.getName() + "()," +
		        			  		getStrModifier(member.getModifiers()));
		        }
	    	}
	    	
	    	JSONObject jsonObj = packDetails2json( packDetails );
	    	
	    	writeJSON( jsonObj, jsonPath + packageList[p] + ".json");
	    	
    	}
	}

	public void writeJSON(JSONObject jsonObj, String jsonFilePath)
	{
		try (FileWriter file = new FileWriter( jsonFilePath )) 
		{
			file.write(jsonObj.toJSONString());
			//System.out.println("Successfully Copied JSON Object to File...");
			//System.out.println("\nJSON Object: " + jsonObj);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject packDetails2json(List<String> packDetails) 
	{
		// Instantiate a dictionary to hold data
		JSONObject data = new JSONObject();
		JSONObject mainDict = new JSONObject();
		
		JSONArray classList = new JSONArray();
		JSONArray subList = new JSONArray();
		String name = "";
		
		for(int p = 0; p < packDetails.size(); p++)
		{
			String line = packDetails.get(p);
			String[] arrline = line.split(",");
			
			if( arrline.length < 2 )
				continue;
			
			if( name.length() == 0 )
				name = arrline[0];
			
			String entry;
			if( name.equals( arrline[0] ) )
			{
				entry = "";
				int trange[] = range( arrline.length-1, 0 , -1);
				for(int i : trange)
					entry += arrline[i] + " ";
				
				JSONObject tmp = new JSONObject();
				tmp.put("name", entry);
				subList.add(tmp);
			}
			else
			{
				mainDict.put("children", subList.clone());
				mainDict.put("name", name);
				classList.add(mainDict.clone());
				
				mainDict.clear();
				subList.clear();
				entry = "";
				
				name = arrline[0];
				int trange[] = range( arrline.length-1, 0, -1);
				for(int i : trange)
					entry += arrline[i] + " ";
				
				JSONObject tmp = new JSONObject();
				tmp.put("name", entry);
				subList.add(tmp);
			}
		}
				
		mainDict.put("name",name);
		mainDict.put("children", subList.clone());
		classList.add(mainDict.clone());
				
		mainDict.clear();
		subList.clear();
				
		// Process the path
		String[] packagePath = "package".split("\\.");
		
		List<String> list = Arrays.asList(packagePath);
		Collections.reverse(list);
		packagePath = (String[]) list.toArray();
		
		for( String pack : packagePath )
		{
			JSONObject tmpData = new JSONObject();
			tmpData.put("name", pack);
			if( data.size() == 0 )
			{
				tmpData.put("children", classList.clone());
				data = tmpData;
			}
			else
			{
				tmpData.put("children", data.clone());
				data = tmpData;
			}
		}
				
		return data;
	}
}
