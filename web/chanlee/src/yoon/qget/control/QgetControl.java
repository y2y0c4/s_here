package yoon.qget.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import yoon.qget.model.QgetManager;
import net.sf.json.JSONObject;
import yoon.qget.model.QgetDTO;

public class QgetControl extends HttpServlet{
	public void service(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		String m = req.getParameter("method");
		System.out.println(m);
		if(m != null){
			if(m.equals("list")){
				//�������� ����Ʈ + best
				list(req, res);
			}else if(m.equals("insert")){
				//���� ����(�μ�Ʈ)
				qinsert(req, res);
			}else if(m.equals("delete")){
				//���� ����
				qdelete(req, res);
			}else if(m.equals("goodbad")){
				//���ƿ� �Ⱦ��
				gbupdate(req, res);
			}else{
				list(req, res);
			}
		}else{
			list(req,res);
		}
	}
	private void list(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		QgetManager manager = QgetManager.getInstance();
		int total = manager.getTotalB();//��ü ������ ����
		req.setAttribute("total", total);
		System.out.println("total : " + total);
		if(total != 0 && total != -1){
			ArrayList<QgetDTO> list = null;
			String strpg = req.getParameter("pg");
			System.out.println("pg: " +strpg);
			int pg= 1;
			if(strpg != null){
				strpg=strpg.trim();
				pg = Integer.parseInt(strpg);
				pg = pg * 10;
			}
			list = manager.listB(pg);
			req.setAttribute("list", list);
			req.setAttribute("pg", pg/10);
			req.setAttribute("total", total);
		}
		ArrayList<QgetDTO> blist = null;
		blist = manager.bestListB();
		req.setAttribute("best", blist);
		String view ="qget/qget.jsp";
		RequestDispatcher rd = req.getRequestDispatcher(view);
		rd.forward(req, res);
	}
	private void qinsert(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		QgetManager manager = QgetManager.getInstance();
		String id = req.getParameter("userid");
		String content = req.getParameter("qcontent");
		QgetDTO dto = new QgetDTO(-1, id, content, 0 , 0 ,null, null);
		manager.insertB(dto);
		
		String view = "qget.do?method=list&pg=1#tab1_qgList";
		res.sendRedirect(view);
	}
	private void qdelete(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		QgetManager manager = QgetManager.getInstance();
		String strNo = req.getParameter("num");
		int num = 0;
		if(strNo != null){
			strNo = strNo.trim();
			num = Integer.parseInt(strNo);
		}
		int chk = manager.deleteB(num);
		JSONObject obj = new JSONObject();
		PrintWriter pw = res.getWriter();
		if(chk==1){
			obj.put("value","suc");
		}else{
			obj.put("value","fail");
		}
		String json = obj.toString();
		pw.print(json);
	}
	
	//good bad update
	private void gbupdate(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		QgetManager manager = QgetManager.getInstance();
		String strNo = req.getParameter("num");
		int num = 0;
		if(strNo != null){
			strNo = strNo.trim();
			num = Integer.parseInt(strNo);
		}
		String userid = req.getParameter("userid");
		if(userid != null){
			userid = userid.trim();
		}
		String chk = manager.qgcheckB(num, userid);//���ƿ� �Ⱦ�� ���� üũ
		System.out.println("chk : " + chk);
		JSONObject obj = new JSONObject();
		PrintWriter pw = res.getWriter();
		if(chk.equals("1")){
			obj.put("value", "f1");
		}else if(chk.equals("2")){//�̹� ���ƿ� Ȥ�� �Ⱦ�� ����
			obj.put("value", "f2");//f1->�̹� ��ư ���� ��������
		}else if(chk.equals("3")){//���������� -> ��� �μ�Ʈ
			String what = req.getParameter("what");
			if(what!=null)what=what.trim();
			if(what.equals("1") || what.equals("2")){//1=>like , 2=>hate
				manager.updateGDB(num, what, userid);//db insert
				obj.put("value", "suc");//��� �μ�Ʈ ����
			}
		}else{//-1
			obj.put("value", "f3");//��񿡷�
		}
		String json = obj.toString();
		pw.print(json);
	}
}
