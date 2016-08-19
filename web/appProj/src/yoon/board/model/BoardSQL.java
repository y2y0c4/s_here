package yoon.board.model;

class BoardSQL {
	//�Խ��� ��� ����
	//final static String LIST = "select * FROM R_BOARD order by NUM desc";
	//select a.num, (select count(*) from r_boa_reply b where b.bnum=a.num) from r_board a
	//����¡�� ���� ������
	final static String LIST = "select * from ("
									+ "select ROWNUM as RNUM, A.* from ("
										+ "select B.*, (select COUNT(*) as CNT from R_BOA_REPLY C where C.BNUM=B.NUM), D.NICK "
									+ "from R_BOARD B, R_MEMBER D where B.ID=D.ID order by B.NUM desc"
									+ ") A where ROWNUM < ?"
								+ ") where RNUM >= ?";
	//�˻� ��������
	//����
	final static String L_SUBJ = "select * from ("
			+ "select ROWNUM as RNUM, A.* from ("
			+ "select B.*, (select COUNT(*) as CNT from R_BOA_REPLY C where C.BNUM=B.NUM), D.NICK"
			+ " from R_BOARD B, R_MEMBER D where B.ID=D.ID and B.SUBJECT like ? order by B.NUM desc"
			//+ "select * from R_BOARD where SUBJECT like ? order by NUM desc" //where subject like '%������%' 
		+ ") A where ROWNUM < ?"
	+ ") where RNUM >= ?";
	//����
	final static String L_CONT = "select * from ("
			+ "select ROWNUM as RNUM, A.* from ("
			+"select B.*, (select COUNT(*) as CNT from R_BOA_REPLY C where C.BNUM=B.NUM), D.NICK"
			+ " from R_BOARD B, R_MEMBER D where B.ID=D.ID and B.CONTENT like ? order by B.NUM desc"
			//+ "select * from R_BOARD where CONTENT like ? order by NUM desc"
		+ ") A where ROWNUM < ?"
	+ ") where RNUM >= ?";
	//����+����
	final static String L_CONT_SUBJ = "select * from ("
			+ "select ROWNUM as RNUM, A.* from ("
			+ "select B.*, (select COUNT(*) as CNT from R_BOA_REPLY C where C.BNUM=B.NUM), D.NICK" 
			+ " from R_BOARD B, R_MEMBER D where B.ID=D.ID and (CONTENT like ? or SUBJECT like ?) order by B.NUM desc"
			//+ "select * from R_BOARD where CONTENT like ? or SUBJECT like ? order by NUM desc"
		+ ") A where ROWNUM < ?"
	+ ") where RNUM >= ?";
	//�۾���
	final static String L_ID = "select * from ("
			+ "select ROWNUM as RNUM, A.* from ("
			+ "select B.*, (select COUNT(*) as CNT from R_BOA_REPLY C where C.BNUM=B.NUM), D.NICK" 
			+ " from R_BOARD B, R_MEMBER D where B.ID=D.ID and D.NICK like ? order by B.NUM desc"
			//+ "select * from R_BOARD where ID like ? order by NUM desc"
		+ ") A where ROWNUM < ?"
	+ ") where RNUM >= ?";
	//��ü �Խñ� ��
	final static String TOTALNUM = "select COUNT(*) as TOTALCOUNT FROM R_BOARD";
	//�˻� �Խñ� ��
	final static String T_CONT = "select COUNT(*) as TOTALCOUNT FROM R_BOARD where CONTENT like ?";
	final static String T_SUBJ = "select COUNT(*) as TOTALCOUNT FROM R_BOARD where SUBJECT like ?";
	final static String T_CONT_SUBJ = "select COUNT(*) as TOTALCOUNT FROM R_BOARD where CONTENT like ? or SUBJECT like ?";
	final static String T_ID = "select COUNT(*) as TOTALCOUNT FROM R_BOARD where ID like ?";
	//���� ����
	final static String CONTENT = "select B.* , A.NAME from R_BOARD B, R_MEMBER A where B.NUM=? and B.ID=A.ID";
	//��ȸ�� ����
	final static String READNUMBER = "update R_BOARD set READNUM=READNUM+1 where NUM=?";
	
	//�� ����(�����ư Ŭ�� ��)											NUM			   ,ID, SUBJECT, CONTENT,READNUM, DATE
	final static String WRITE = "insert into R_BOARD values(R_BOARD_NUM.nextval,? ,		  ?,      ?,       ?, SYSDATE)";
		
	//�� ����(������ư Ŭ���� ����Ʈ ��� ����)
	final static String EDIT = "select B.SUBJECT, B.CONTENT, B.ID, A.NAME from R_BOARD B, R_MEMBER A where B.NUM=? and B.ID=A.ID ";
		
	//������Ʈ (���� ��ư Ŭ�� ��)
	final static String UPDATE = "update R_BOARD set SUBJECT=?, CONTENT=? where NUM=?";
		
	//�� ����
	final static String DEL = "delete from R_BOARD where NUM=?";
		
	
}
