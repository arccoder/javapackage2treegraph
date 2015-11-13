import java.io.IOException;

public class MainClass 
{
	public static void main(String[] args) throws IOException
	{	
    	String projectPath = "C:/Github/javapackage2treegraph/";
    	String folderPath = "jp2tg/src/";
    	String[] packageList = {"package1"};
    			
    	JavaPackage2TreeGraph jp2tg = new JavaPackage2TreeGraph();
    	
    	jp2tg.getPackageContents(projectPath, folderPath, packageList);
    }
}
