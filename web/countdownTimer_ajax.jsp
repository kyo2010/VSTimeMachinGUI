<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "wc" scope= "session" class= "vs.time.kkv.connector.web.WebControl" />  
<%@page import="vs.time.kkv.models.VS_STAGE_GROUP"%>
<%@page import="vs.time.kkv.models.VS_STAGE_GROUPS"%>
<%@page import="vs.time.kkv.models.VS_STAGE"%>
<%@page import="vs.time.kkv.connector.web.VirtualDisplay"%>
<%@page import="vs.time.kkv.connector.MainlPannels.stage.StageTableData"%>


<%@page import="java.util.List"%>

<h1><b><%=wc.mainForm.countDownTimerText %></b>
</h1>