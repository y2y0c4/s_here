<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
 import="yoon.questions.model.QuestionsDTO, java.util.ArrayList" %>

<body style="margin: 0px;" bgcolor="transparent">
	<div id="wrapper"> 
	<!-- top line-->
		<div class='roof' style="width:100%; height:0px; margin:0; padding:0;"></div>
		<!-- top line-->
		<!-- wrapper -->
		<div class='center-wrapper' style="width:1000px; margin:0 auto;">
		<!-- header-->
		<%@include file="../menu.jsp" %>
		
	<p id="board_title">질문 리스트</p>
	<hr width="90%" color="White" noshade>

	<table class="board1" cellpadding='2' style="table-layout:fixed;word-break:break-all;">
	<tr class="board-normal">
		<th align='center' width='15%' height='30'>썸네일</th>
		<th align='center' width='85%' height='30'>내용</th>
	</tr>
<%
	ArrayList<QuestionsDTO> qlist = (ArrayList<QuestionsDTO>)request.getAttribute("qlist");
	if(qlist != null){
		for(QuestionsDTO dto : qlist){
%>
		<tr class="board-normal">
			<td align='center' style="padding:10px;"><img width="80px" height="80px" src="<%=dto.getPic()%>"></td>
			<td align='left'>
			<a href="main.do?method=qinfo&num=<%=dto.getNum()%>"><%=dto.getSubject()%></a></td>
		</tr>
<%
		}
	}
%>
	</table>
</div>
</div>
</body>

<script type="text/javascript">
$('a').mouseenter(function(){
	$(this).css('color','#707070');
})
$('a').mouseleave(function(){
	$(this).css('color','white');
})
$('a').mousedown(function(){
	$(this).css('color','#505050');
})
$('a').mouseup(function(){
	$(this).css('color','#777777');
})
</script>