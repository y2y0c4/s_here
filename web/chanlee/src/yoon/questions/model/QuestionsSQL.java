package yoon.questions.model;

class QuestionsSQL {
	//���� ����Ʈ  
	final static String LIST = "select * from R_PICTURE order by NUM desc";
	//����_��� ����Ʈ 
	final static String COMMENTLIST = "select * from R_PIC_REPLY order by NUM desc";
	//���� �ϳ� ����Ʈ
	final static String MAINQ = "select * from R_PICTURE where num=?";
	//���� ���� ����
	final static String TOTALQ = "select count(*) as totalcount from R_PICTURE";
	
	
	//final static String P_LIST = "select NUM, SUBJECT, RDATE from R_PICTURE order by NUM desc";
	//final static String P_SLIST = "select PIC, SUBJECT, AUTHOR, RDATE from R_PICTURE where NUM=?";

}
