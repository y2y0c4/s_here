package yoon.member.model;

class MemberSQL {
	//��� ����Ʈ
//	final static LIST = "select * from R_MEMBER";
//	final static String M_SLIST = "select NAME, ID, PWD from R_MEMBER";
	
	//��й�ȣ ��������
	final static String MB_PWD = "select PWD from R_MEMBER where ID=?";
	//ID ��������
	final static String MB_ID2 = "select ID from R_MEMBER where NICK=?";
	
	//��� �߰�
	final static String MB_JOIN = "insert into R_MEMBER values(R_MEMBER_NUM.nextval, ?, ?,?, ?,?, null, ?, SYSDATE)";

	//��� ���� ��������
	final static String MB_INFO = "select ID, PWD, NAME, NICK, EMAIL, PIC, BIRTH, RDATE from R_MEMBER where ID=?";
	
	//���̵� ��������(���̵� ã�� ����)
	final static String MB_ID = "select ID from R_MEMBER where NAME=? and EMAIL=?";
	
	//��й�ȣ ����
	final static String MB_PWD_UP = "update R_MEMBER set PWD=? where ID=?";
	
	//��� ���� ������Ʈ
	final static String MB_UPDATE = "update R_MEMBER set PWD=?, NICK=?, EMAIL=? where ID=?";
	//�����ʻ��� ������Ʈ
	final static String PIC_UPDATE = "update R_MEMBER set PIC=? where ID=?";
	//������ ���� �ּ� ��������
//	final static String MB_PIC = "select PIC from R_MEMBER where ID=?";
//	final static String ID_CHECK = "select pwd from R_MEMBER where ID=?";
//	final static String M_INSERT = "insert into R_MEMBER values(R_MEMBER_NUM.nextval, ?, ?, ?, ?, null, -1, SYSDATE)";
//	final static String M_PIC = "select PIC from R_MEMBER where ID =?";
//	final static String M_ID = "select ID from R_MEMBER where NAME=? and EMAIL=?";
//	final static String M_PWD_UP = "update R_MEMBER set PWD=? where ID=?";

}
