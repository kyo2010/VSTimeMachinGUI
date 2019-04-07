<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "wc" scope= "session" class= "vs.time.kkv.connector.web.WebControl" />  
<%@page import="vs.time.kkv.models.VS_STAGE_GROUP"%>
<%@page import="vs.time.kkv.models.VS_STAGE_GROUPS"%>

<% 
   VS_STAGE_GROUP group = wc.getActiveGroup();
   if (group!=null){ 
      for (int index=0; index<4; index++){ 
        VS_STAGE_GROUPS pilot = wc.getPilot(group,index);
      %>

      <%-- Teamplate for PILOT OSD --%>
      
        <%=((index%2==0)?"<div class='row'>":" ") %> 
        <div class="channel">
            <% if (pilot!=null) { %>
            <div class="channel-number"><%=(pilot!=null?pilot.CHANNEL:"")%></div>
            <div class="channel-info">               
                    <% if (pilot!=null && !"".equals(pilot.gePhotoUrl()) ) { %>
                       <div class="channel-photo">
                          <img src="<%=pilot.gePhotoUrl()%>" alt="">
                       </div>
                    <% } %>                
                <div class="channel-label" style="background-color: <%=(pilot!=null?pilot.getWebColor():"")%>;"></div>
                <div class="channel-name"><%=(pilot!=null?pilot.getFullUserName():"")%></div>
                <div class="channel-results">
                    <div class="table">
                        <div class="table-row">
                            <div class="table-cell table-header"><%=wc.L("Laps")%> </div>
                            <div class="table-cell table-header"><%=wc.L("Best Lap")%> </div>
                            <div class="table-cell table-header"><%=wc.L("Race Time")%> </div>
                        </div>
                        <div class="table-row">
                            <div class="table-cell"><%=pilot!=null?pilot.LAPS:0 %></div>
                            <div class="table-cell"><%=pilot!=null?wc.getTime(pilot.BEST_LAP):"" %></div>
                            <div class="table-cell"><%=pilot!=null?wc.getTime(pilot.RACE_TIME):"" %></div>
                        </div>
                    </div>
                </div>

            </div>
            <% } %>              
         </div>
       <%=(index%2==1?"</div>":"") %>
   <% }
   }  
%>