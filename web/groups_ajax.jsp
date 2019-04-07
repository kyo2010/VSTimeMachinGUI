<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "wc" scope= "session" class= "vs.time.kkv.connector.web.WebControl" />  
<%@page import="vs.time.kkv.models.VS_STAGE_GROUP"%>
<%@page import="vs.time.kkv.models.VS_STAGE_GROUPS"%>
<%@page import="java.util.List"%>

<%    
   List<VS_STAGE_GROUP> groups = wc.getGroups();
   if (groups!=null){ 
      int index = 0;
      for (VS_STAGE_GROUP group : groups){ 
        index++;
        %>
          <%=((index%3==1)?"<div>":" ") %>
          <div>
              <div>Group <%=group.GROUP_NUM%></div>
        <%
        for (VS_STAGE_GROUPS pilot:group.users){
        //VS_STAGE_GROUPS pilot = wc.getPilot(group,index);
      %>       
            <div>
              <% if (pilot!=null) { %>
                    <div><%=pilot.getFIO()%> : <%=pilot.CHANNEL%></div>
              <% } %>    
            </div>
                                         
   <% }
 %>
    </div>
    <%=(index%3==0?"</div>":"") %>   
<%

     }
   }  
%>