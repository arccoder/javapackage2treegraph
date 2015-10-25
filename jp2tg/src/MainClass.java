import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Vector;

public class MainClass 
{

	static String getStrModifier(int modifierNum)
	{
		String[] modifierStr = {"","public","private","unknown","protected"};
		
		String modifier = "";
    	if( modifierNum > modifierStr.length-1 )
    		modifier = String.valueOf(modifierNum);
    	else
    		modifier = modifierStr[modifierNum];
    	
    	return modifier;
	}
	
	static Vector<String> getClassInPackage(String packagePath)
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
	
	public static void main(String[] args) throws IOException
	{

	    	String projectPath = "C:/Github/javapackage2treegraph/";
	    	String folderPath = "jp2tg/src/";
	    	String[] packageList = {"package1"};
	    			
	    	for(int p = 0; p < packageList.length; p++)
	    	{
	        	File file = new File( projectPath + packageList[p] + ".txt");
	        	
	    		// if file doesnt exists, then create it
	    		if (!file.exists()) {
	    			file.createNewFile();
	    		}

	    		FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    		BufferedWriter bw = new BufferedWriter(fw);
	    		
	    		bw.write( packageList[p] + "\n" );
		    	
		    	Vector<String> packClass = getClassInPackage( projectPath + folderPath + packageList[p] );
				
		    	for (int i = 0; i < packClass.size(); i++) 
		    	{    		
			    	Class c = null;
					try {
						String classPath = packageList[p] + "." + packClass.get(i);
						c = Class.forName( classPath );
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
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
		
			        	bw.write( className + ',' +
			        			  field.getName().toString() + ',' +
			        			  field.getType().toString() + ',' +
			        			  getStrModifier(field.getModifiers()) + "\n");
			        }
		
			        Member[] allMembers = c.getDeclaredMethods();
			        for (Member member : allMembers) 
			        {
			        	String memberName = member.getDeclaringClass().toString();
			        	String[] tokens = memberName.split("\\.");
			        	memberName = tokens[tokens.length-1];
			        	
			        	bw.write( memberName + "," +
			        			  member.getName().toString() + "()," +
			        			  getStrModifier(member.getModifiers()) + "\n");
			        }
		    	}
		    	bw.close();
	    	}
	    }
}
