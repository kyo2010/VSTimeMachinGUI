<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "wc" scope= "session" class= "vs.time.kkv.connector.web.WebControl" />  
<%@page import="vs.time.kkv.models.VS_STAGE_GROUP"%>
<%@page import="vs.time.kkv.models.VS_STAGE_GROUPS"%>
<%@page import="vs.time.kkv.models.VS_STAGE"%>
<%@page import="vs.time.kkv.connector.web.VirtualDisplay"%>
<%@page import="vs.time.kkv.connector.MainlPannels.stage.StageTableData"%>


<%@page import="java.util.List"%>

<%       
   //List<VS_STAGE_GROUP> groups = wc.getLastStageGroups();
   //VirtualDisplay display = wc.getLastStageVirtualDisplay(wc.countRefresh, 90-4.8, 5, 7, 1,7);
   
   //  displayHeight, pilotHeight, groupHeight, groupSpacerHeight , secundPerPage   
   VirtualDisplay display = wc.getLastStageVirtualDisplay(wc.countRefresh, 90-4.8, 4, 8, 4, 7);
   
   VS_STAGE stage = wc.getLastStage();
   if (display!=null){ 
      %>
      <section>          
        <div class="title">
          <div class="raceName"><%=wc.getRaceName()%> - <%=stage.CAPTION%></div>
          <div class="page"><b><%=display.CURRENT_DISPLAY%></b>/<%=display.DISPLAYS_COUNT%></div>
        </div>
        <table width="100%" border="0" style="padding:1vh 0vh 1vh 0vh" >
      <%
      int index = 0;
      for (StageTableData data : display.rows){         
        %>         
           <% if (data.isGrpup || index==0) { %>
             <%=(index!=0?"<tr><td class=\"group_spacer\"></td></tr>":"")%>
             <tr class="group_row2">
                <% for (int col=0; col<wc.getColumnCount(display); col++) { 
                     String colName = wc.getColumnName(display, col);                      
                     double width=wc.getColumnWidth(display,col); %>  
                     <%=colName==null?"":("<td class=\"cell2\" "+(width==0?"":"style=\"width:"+width+"%\"")+" >"+colName+"</td>")%>
                <% } %>
             </tr>                            
           <% }; 
              VS_STAGE_GROUPS pilot = data.pilot;
              if(pilot!=null) { %>       
                <% if (data.pilot!=null) { %>
                  <tr class="pilot_row2">
                    <% for (int col=0; col<wc.getColumnCount(display); col++) { 
                         String value = wc.getValueAt(display, index+display.start_row_index, col); 
                         double width=wc.getColumnWidth(display,col); 
                         String className = "cell2";
                         String style = "";
                         if (wc.isPilotCol(display,col)) {
                           className = "cellPilotName2";
                           value = pilot.getFIO();
                         }
                         if (wc.isChannelCol(display,col) && value!=null){
                           value = "<span style='text-align:center;font-weight: bold; padding: 1vh 1vh 0vh 0.5vh;margin: 0vh 0.2vh 0vh 0.5vh;' "+
                                   "class=\"w3-round  w3-center "+wc.getW3Color(display,pilot)+"\">"
                                   + value + "</span>";                                                      
                         }
                         %>  
                         <%=value==null?"":("<td class=\""+className+"\" "+style+" "+(width==0?"":"style=\"width:"+width+"%\"")+" >"+value+"</td>") %>
                    <% } %>
                  </tr>  
                <% } %>                                                       
           <% } 
          index++;
      } %>
      </table>
      </section>
<%
     wc.countRefresh++;
   }else{
     wc.countRefresh = 0;
   }    
%>