package yoon.qreply.model;

class QreplySQL {
	//������ ���� ��� ����Ʈ
	final static String LIST = "select A.*, B.NICK, B.PIC from R_PIC_REPLY A,R_MEMBER B where A.PNUM=? and A.ID=B.ID(+) order by A.NUM desc";
	
	//���� ��� ������Ʈ
	final static String QREPLY = "insert into R_PIC_REPLY values(R_PIC_REPLY_NUM.nextval, ?, ?, ? , ? ,? , SYSDATE)";
	
	//��� ����
	final static String QR_DEL = "delete from R_PIC_REPLY where NUM=?";
	
	//��� �н����� ���
	final static String QR_PWD = "select PWD from R_PIC_REPLY where NUM=?";
	
	//final static String PR_LIST = "select NUM, USERID, NAME, REPLY, RDATE from R_PIC_REPLY where PNUM=? order by NUM desc";
	//final static String PR_INSERT = "insert into R_PIC_REPLY values(R_PIC_REPLY_NUM.nextval, ?, ?, ?, ?, ?, SYSDATE)";
	//final static String PR_DELETE = "delete from R_PIC_REPLY where num=?";
	//final static String PR_PWD = "select PWD from R_PIC_REPLY where NUM=?";
}
