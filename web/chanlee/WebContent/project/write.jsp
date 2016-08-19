<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
 import="java.util.*, yoon.board.model.BoardDTO"%>
<body style="margin: 0px;" bgcolor="transparent">
	<div id="wrapper"> 
	<!-- top line-->
		<div class='roof' style="width:100%; height:0px;  margin:0; padding:0;"></div>
		<!-- top line-->
		<!-- wrapper -->
		<div class='center-wrapper' style="width:1000px; margin:0 auto;">
		<!-- header-->
		<%@include file="../menu.jsp" %>
<%
	if(mbdto != null){
		String userid = (String)request.getAttribute("userid");

		if(mbdto.getId().equals(userid)){
%>

  	<script language="javascript">
	  function checkValue()
	  {
	    if(document.input.subject.value == "")
		{
		  alert("제목을 입력해주세요");
		  return false;
		}
		if(document.input.content.value == "")
		{
		  alert("내용을 입력해주세요");
		  return false;
		}
		if(document.input.id.value == "")
		{
		  alert("이름을 입력해주세요");
		  return false;
		}
		document.input.submit();
	  }
	</script>

				  <p id="board_title">일상 게시판</p>
	  <hr width="90%" color="White"  noshade>

	  <form name="input" action="board.do?method=insert" method="post">

	    <table class="board1" align="center"  cellspacing="1" 
		                                  cellpadding="3">
		  <tr class="board-normal">
		    <td align="right" width="20%"><b>제목</b></td>
			<td align="center" width="80%">
			  <input type="text" name="subject" size="100%" id="r_subject">
			</td>
		  </tr>
		  <tr class="board-normal">
		    <td align="right" width="20%"><b>작성자</b></td>
			<td align="center" width="80%">
			<input type="hidden" name="id"  value="<%=mbdto.getId()%>">
			  <b size="100%" id="r_writer"><%= mbdto.getNick() %></b>
			</td>
		  </tr>
		  <tr id="r_content">
		    <td align="right" width="20%"><b>내용</b></td>
			<td align="center" width="80%">
			  <textarea name="content" rows="25" cols="100%" id="r_textarea"></textarea>
			</td>
		  </tr>
		  <tr>
		    <td align="center" colspan="2"><br><br>
			  <a onclick="checkValue()" id="btn_write">작성하기</a>
			  	<a href="board.do?method=list" id="b_list" style="margin-right:10%;">목록으로</a>
			</td>

			  

		  </tr>
		</table>
	  </form>

</div>
</div>

<script type="text/javascript">
$(document).ready(function(){
	$('a').mouseenter(function(){
		$(this).css('color','#777777');
	})
	$('a').mousedown(function(){
		$(this).css('color','#505050');
	})
	$('a').mouseleave(function(){
		$(this).css('color','white');
	})
	$('a').mouseup(function(){
		$(this).css('color','#777777');
	})

});
</script>
<%
		}else{
%>
	<script type="text/javascript">
			$(document).ready(function(){
				alert("잘못된 접근입니다.");
				history.back();
			});
	</script>

<%			
		}
}else{
%>
<script type="text/javascript">
$(document).ready(function(){
	alert("잘못된 접근입니다.");
	history.back();
});
</script>
<%
}
%>