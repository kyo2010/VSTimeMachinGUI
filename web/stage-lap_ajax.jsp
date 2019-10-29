<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "wc" scope= "session" class= "vs.time.kkv.connector.web.WebControl" />  
<%@page import="vs.time.kkv.models.VS_STAGE_GROUP"%>
<%@page import="vs.time.kkv.models.VS_STAGE_GROUPS"%>
<%@page import="vs.time.kkv.models.VS_STAGE"%>
<%@page import="vs.time.kkv.connector.web.VirtualDisplay"%>
<%@page import="vs.time.kkv.connector.MainlPannels.stage.StageTableData"%>


<%@page import="java.util.List"%>

<%          
   VS_STAGE stage = wc.getLastStage();
   if (stage!=null){
     List<VS_STAGE_GROUPS> pilots = wc.getPilotsOrderByLaps(stage);   
      %>
        <div >
            <div class="raceName"><%=wc.getRaceName()%> - <%=stage.CAPTION%></div>
        </div>
        <br/>
        <br/>
        <table width="100%" class="w3-table w3-bordered">
            <tr>
                <th><%=wc.L("Pilot")%></th>
                <th><%=wc.L("Laps")%></th>
            </tr>
      <%
      for (VS_STAGE_GROUPS pilot : pilots){         
        %>                    
           <tr>
              <td><%=pilot.getFullUserName() %> </td>
              <td><%=pilot.LAPS %> </td>
           </tr>             
      <% } %>
      </table>      
<%}%>