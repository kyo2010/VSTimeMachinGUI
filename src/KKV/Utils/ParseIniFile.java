package KKV.Utils;

import java.io.*;
import java.util.*;

public class ParseIniFile
{
   public ArrayList<ParseIniFile> linkIniFiles = new ArrayList<ParseIniFile>();
   final String comment = "#";

   StringTokenizer tokenizer;
   Hashtable<String,String> params;
   ParseIniFile parentIniFile = null;
   String iniFile;
   String fileCodePage = null;

   public ParseIniFile( String iniFile ){    
     this(iniFile,null,null);
   }
    
   public ParseIniFile( String iniFile, ParseIniFile parentIniFile, String fileCodePage  )
   {
      this.fileCodePage  = fileCodePage;
      this.iniFile = iniFile;
      this.parentIniFile = parentIniFile;
     
      params = new Hashtable();
      String sysParam = "";
      
      try
      {
        FileInputStream fileIn = new FileInputStream( iniFile );
        BufferedReader dataIn =
                new BufferedReader( new InputStreamReader( fileIn, "UTF-8" ) );

        String readLine = dataIn.readLine();
        String nameParam, valueParam;

        while( readLine != null )
        {
           readLine = readLine.trim();
           if ( !readLine.equals( "" ) && !readLine.substring( 0, comment.length() ).equals( comment ) )
           {
              if (fileCodePage!=null){
                //readLine = readLine.c
              }
              tokenizer = new StringTokenizer( readLine, "=" );
              nameParam  = tokenizer.nextToken();
              valueParam = "";
              try{
                valueParam = tokenizer.nextToken();
              }catch(Exception e){}
              nameParam = nameParam.replaceAll("\\\\n","\n");
              valueParam = valueParam.replaceAll("\\\\n","\n");
              valueParam = valueParam.replaceAll("\\\\r","\r");
              sysParam = "";
              try{
                sysParam = tokenizer.nextToken();
              }catch(Exception e1){}
              if (nameParam.indexOf("setProperty")==0){
                java.util.Properties props = System.getProperties();
                props.setProperty( valueParam, sysParam );
                System.setProperties(props);
              }              
              params.put( nameParam, valueParam );
              if (nameParam.equalsIgnoreCase("linkIniFile")){
                try{
                  linkIniFiles.add(new ParseIniFile(valueParam,this,fileCodePage));
                }catch(Exception e){
                  System.out.println("link on ini file "+valueParam+" not found");
                }
              }
           }
           readLine = dataIn.readLine();
        }

        fileIn.close();
        dataIn.close();
      }
      catch(Exception e) {  
      }

   }
     
   public boolean getBooleanParam( String nameParam, boolean defValue ) {
      String paramValue = (String)(params.get( nameParam ));
      if ( paramValue == null ){
        for (ParseIniFile pif: linkIniFiles){
          try{
            paramValue = pif.getParam(nameParam);
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("yes"))              
              return true;
            if (paramValue.equalsIgnoreCase("false") || paramValue.equalsIgnoreCase("no"))
              return false;
            return defValue;
          }catch(Exception e){}
        }
        return defValue;
      }else
        return defValue;
   }
 
   public String getParam( String nameParam ) throws UserException
   {
      String paramValue = (String)(params.get( nameParam ));
      if ( paramValue == null ){
        for (ParseIniFile pif: linkIniFiles){
          try{
            paramValue = pif.getParam(nameParam);
            return paramValue;
          }catch(Exception e){}
        }
        throw new UserException( "Error read file from ini file",nameParam );
      }else
        return paramValue;
   }
     
   public String getParam( String nameParam, String defValue )
   {
      String paramValue = (String)(params.get( nameParam ));
      if ( paramValue == null ){
        for (ParseIniFile pif: linkIniFiles){
          try{
            paramValue = pif.getParam(nameParam);
            return paramValue;
          }catch(Exception e){}
        }        
        return defValue;
      }
      return paramValue;
   }
   
   public void setParam( String nameParam, String paramName ) {
     params.put(nameParam, paramName);
     saveParams();
   }
   
   private void saveParams() {
     try{
       File f = new File(iniFile);
       (new File(f.getParent())).mkdirs();
       FileOutputStream fileOut = new FileOutputStream( iniFile );       
       for (String paramName : params.keySet()){
         String paramValue = params.get(paramName);
         String line = paramName+"="+paramValue+"\n";
         fileOut.write(line.getBytes());
       }
       fileOut.close();
     }catch(Exception e){
       
     }
   }
   
   public static void main(String[] args) {
     String test = "Total\\nPlace";
     System.out.println(test.replaceAll("\\\\n", "\n"));   
   }

}
