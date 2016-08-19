package yoon.board.model;

import java.sql.*;
import java.util.ArrayList;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import yoon.board.model.BoardDTO;

class BoardDAO {

	//����Ÿ���̽� Ǯ Ŀ�ؼ�
	private DataSource ds;
	
	BoardDAO(){
		try{
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
		}catch(NamingException ne){
			System.out.println("ne : " + ne);
		}
	}
	
	//�Խ��� ��� 
	ArrayList<BoardDTO> list(int pg, int sidx, String str){
		Connection con = null;
		//Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		try{
			int firstIdx = (pg-1)*15+1;
			int lastIdx = firstIdx + 15;
			con = ds.getConnection();
			//stmt = con.createStatement();
			switch(sidx){
				case 0:
					pstmt = con.prepareStatement(BoardSQL.LIST);
					pstmt.setInt(1, lastIdx);
					pstmt.setInt(2, firstIdx);
					break;
				case 1:
					pstmt = con.prepareStatement(BoardSQL.L_CONT);
					pstmt.setString(1, "%"+str +"%");
					pstmt.setInt(2, lastIdx);
					pstmt.setInt(3, firstIdx);
					break;
				case 2:
					pstmt = con.prepareStatement(BoardSQL.L_SUBJ);
					pstmt.setString(1, "%"+str +"%");
					pstmt.setInt(2, lastIdx);
					pstmt.setInt(3, firstIdx);
					break;
				case 3:
					pstmt = con.prepareStatement(BoardSQL.L_CONT_SUBJ);
					pstmt.setString(1, "%"+str +"%");
					pstmt.setString(2, "%"+str +"%");
					pstmt.setInt(3, lastIdx);
					pstmt.setInt(4, firstIdx);
					break;
				case 4:
					pstmt = con.prepareStatement(BoardSQL.L_ID);
					pstmt.setString(1, "%"+str +"%");
					pstmt.setInt(2, lastIdx);
					pstmt.setInt(3, firstIdx);
					break;
			}
			rs = pstmt.executeQuery();
			//������ ����"select * FROM R_BOARD order by NUM desc";

			while(rs.next()){
				
				int num = rs.getInt(2);
				String id = rs.getString(3);
				String subject = rs.getString(4);
				String content = rs.getString(5);
				int readnum = rs.getInt(6);
				java.sql.Date rdate = rs.getDate(7);
				int replycnt = rs.getInt(8);
				String nick = rs.getString(9);
				list.add(new BoardDTO(num, id, subject, content, readnum, rdate, replycnt, nick));
			
			}
			return list;
		}catch(SQLException se){
			System.out.println("se on list : " + se);
			return null;
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null)pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
				System.out.println("se on list: " +se);
			}
		}
	}

	int getTotal(int sidx, String str){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			con = ds.getConnection();

			switch(sidx){
			case 0:
				pstmt  = con.prepareStatement(BoardSQL.TOTALNUM);
				rs = pstmt.executeQuery();
				break;
			case 1:
				pstmt  = con.prepareStatement(BoardSQL.T_CONT);
				pstmt.setString(1, "%"+str + "%");
				rs = pstmt.executeQuery();
				break;
			case 2:
				pstmt = con.prepareStatement(BoardSQL.T_SUBJ);
				pstmt.setString(1, "%" + str + "%");
				rs = pstmt.executeQuery();
				break;
			case 3:	
				pstmt = con.prepareStatement(BoardSQL.T_CONT_SUBJ);
				pstmt.setString(1, "%" + str + "%");
				pstmt.setString(2, "%" + str + "%");
				rs = pstmt.executeQuery();
				break;
			case 4:
				pstmt = con.prepareStatement(BoardSQL.T_ID);
				pstmt.setString(1, "%" + str + "%");
				rs = pstmt.executeQuery();
				break;
			}

			if(rs.next()){
				int tempNum = rs.getInt(1);
				if(tempNum%15==0)return tempNum/15;
				else return tempNum/15+1;
			}
			else return -1;
		}catch(SQLException se){
			return -1;
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
			}
		}
	}
	//�� ���� �����ֱ�
	BoardDTO content(int tmpNo, boolean Cchk){
		Connection con = null;
		PreparedStatement pstmt = null;//�� ������ ���� ������Ʈ��Ʈ
		PreparedStatement pstmt2 = null;//��ȸ�� ������ ���� ������Ʈ��Ʈ
		ResultSet rs = null;
		try{
			con = ds.getConnection();
			//������ ����"select * from R_BOARD where NUM=?";
			pstmt = con.prepareStatement(BoardSQL.CONTENT);
			if(!Cchk){//��Ű �� false - >  ��Ű ���°ɷ� ����
				pstmt2 = con.prepareStatement(BoardSQL.READNUMBER);
				pstmt2.setInt(1, tmpNo);
				pstmt2.executeUpdate();
			}
			pstmt.setInt(1, tmpNo);

			rs = pstmt.executeQuery();
			rs.next();
			int num = rs.getInt(1);
			String id = rs.getString(2);
			String subject = rs.getString(3);
			String content = rs.getString(4);
			int readnum = rs.getInt(5);
			java.sql.Date rdate = rs.getDate(6);
			String nick = rs.getString(7);
			BoardDTO dto = new BoardDTO(num, id, subject, content, readnum, rdate, -1, nick);
			return dto;
		}catch(SQLException se){
			System.out.println("se on content : " +se);
			return null;
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
			}
		}
	}
	//�� �Է� �� -> ��� �μ�Ʈ
	void write(BoardDTO dto){
	
		Connection con = null;
		PreparedStatement pstmt = null;
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(BoardSQL.WRITE);
			//�� ����(�����ư Ŭ�� ��)				    NUM			   ,ID, SUBJECT, CONTENT,READNUM, DATE
			//"insert into R_BOARD values(R_BOARD_NUM.nextval,? ,		  ?,      ?,       ?, SYSDATE)";
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3,  dto.getContent());
			pstmt.setInt(4, 0);

			//������ ����
			int i = pstmt.executeUpdate();

		}catch(SQLException se){
			System.out.println("se on write: " +se);
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
			}
		}		
	}
	
	//�� ���� ��ư ->��� ������Ʈ
	void update(BoardDTO dto){
		Connection con = null;
		PreparedStatement pstmt = null;
		try{
			con = ds.getConnection();
			//"update R_BOARD set SUBJECT=?, CONTENT=? ";
			pstmt = con.prepareStatement(BoardSQL.UPDATE);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getNum());
			int i = pstmt.executeUpdate();
		}catch(SQLException se){
			System.out.println("se update: "+se);
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
			}
		}
	}
	
	//������ư ����  ��� �ο� ����
	void delete(BoardDTO dto){
		Connection con = null;
		PreparedStatement pstmt = null;
		try{
			con = ds.getConnection();
			//"delete from R_BOARD where NUM=?"
			pstmt = con.prepareStatement(BoardSQL.DEL);
			pstmt.setInt(1, dto.getNum());
			int i = pstmt.executeUpdate();
		}catch(SQLException se){
			System.out.println("se delete : "+se);
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
			}
		}
	}
	
	//����â �����ֱ�
	BoardDTO editMode(int tmpNo){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			con = ds.getConnection();
			//"select SUBJECT, CONTENT, ID from R_BOARD where NUM=? ";
			pstmt = con.prepareStatement(BoardSQL.EDIT);
			pstmt.setInt(1, tmpNo);
			rs = pstmt.executeQuery();
			rs.next();
			String subject = rs.getString(1);
			String content = rs.getString(2);
			String id = rs.getString(3);
			String nick = rs.getString(4);
			BoardDTO dto = new BoardDTO( tmpNo, id, subject, content, -1, null, -1 , nick);
			return dto;
		}catch(SQLException se){
			System.out.println("se edit : "+se);
			return null;
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException se){
			}
		}
	}
}
