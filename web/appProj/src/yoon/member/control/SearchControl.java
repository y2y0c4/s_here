package yoon.member.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.bouncycastle.jcajce.provider.digest.Keccak.DigestKeccak;

import yoon.member.model.MemberManager;

public class SearchControl extends HttpServlet{
	public static final int KEY_SIZE = 1024;
	public void service(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		String m = req.getParameter("method");
		if(m != null){
			m = m.trim();
			if(m.equals("spw_form")){
				//pwd�˻�
				searchPwdForm(req, res);
			}else if(m.equals("s_pwd")){
				pwdSearch(req, res);
			}else if(m.equals("pwdUp")){
				//��й�ȣ ����
				pwdUpdate(req, res);
			}else if(m.equals("sid_form")){
				//���̵�˻�
				searchIdForm(req, res);
			}else if(m.equals("s_id")){
				idSearch(req, res);
			}else if(m.equals("duple")){
				//���̵� �ߺ�üũ
				dupleCheck(req, res);
			}else if(m.equals("duple2")){
				//�г����ߺ�üũ
				dupleCheck2(req, res);
			}else if(m.equals("duple3")){
				//���������� �г��� �ߺ�üũ(�ڽŰ��ϰ��� �ߺ��ƴѰɷ�)
				dupleCheck3(req, res);
			}
		}else{
			RequestDispatcher rd = req.getRequestDispatcher("main.do");
			rd.forward(req, res);
			//res.sendRedirect("main.do");
		}
	}
	private void dupleCheck(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		MemberManager manager = MemberManager.getInstance();
		String id = req.getParameter("id");
		
		if(id != null) id = id.trim();
		String pwd = manager.pwdCheckB(id);
		res.setContentType("text/html;charset=UTF-8");
		//JSONObject obj = new JSONObject();
		PrintWriter pw = res.getWriter();
		if(!pwd.equals("")){//üũ���� ������ �ƴϸ� -> ���̵� ������
			//obj.put("value" , "cannot");
			//String json = obj.toString();
			pw.print("false");
		}
		else{
			//obj.put("value" , "can");
			//String json = obj.toString();
			pw.print("true");
		}
	}
	private void dupleCheck2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		MemberManager manager = MemberManager.getInstance();
		String nick = req.getParameter("nick");
		
		if(nick != null) nick = nick.trim();
		System.out.println(nick);
		String id = manager.idCheck2B(nick);

		res.setContentType("text/html;charset=UTF-8");
		//JSONObject obj = new JSONObject();
		PrintWriter pw = res.getWriter();
		if(!id.equals("")){//üũ���� ������ �ƴϸ� -> �г����� ������
			//obj.put("value" , "cannot");
			//String json = obj.toString();
			pw.print("false");
		}
		else{
			//obj.put("value" , "can");
			//String json = obj.toString();
			pw.print("true");
		}
	}
	private void searchPwdForm(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String view = "search/pwdSearch.jsp";
		RequestDispatcher rd = req.getRequestDispatcher(view);
		rd.forward(req, res);
	}
	private void pwdSearch(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		MemberManager manager = MemberManager.getInstance();
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		//String emailId = req.getParameter("emailId");
		//String emailS = req.getParameter("emailS");
		String email = req.getParameter("email");
		String userid = manager.idSearchB(name, email);

		if(id.equals(userid)){
			req.setAttribute("id", id);
		}
		try{
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE);
            
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            HttpSession session = req.getSession();
            // ���ǿ� ����Ű�� ���ڿ��� Ű���Ͽ� ����Ű�� �����Ѵ�.
            session.setAttribute("__rsaPrivateKey__", privateKey);

            // ����Ű�� ���ڿ��� ��ȯ�Ͽ� JavaScript RSA ���̺귯�� �Ѱ��ش�.
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
            req.setAttribute("publicKeyModulus", publicKeyModulus);
            req.setAttribute("publicKeyExponent", publicKeyExponent);

    		String view = "search/pwdSearchOk.jsp";
    		RequestDispatcher rd = req.getRequestDispatcher(view);
    		rd.forward(req, res);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	private void pwdUpdate(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		MemberManager manager = MemberManager.getInstance();
		String id = req.getParameter("securedId");
		String pwd = req.getParameter("securedPwd");
		
		HttpSession session = req.getSession();
		
		PrivateKey privateKey = (PrivateKey)session.getAttribute("__rsaPrivateKey__");

		session.removeAttribute("__rsaPrivateKey__");
		if(privateKey == null){
			System.out.println("private error!!");
		}
		try{
			String password = CryptoSHA3(decryptRsa(privateKey,pwd), 256);

			boolean state = manager.pwdUpdateB(password, id);
			req.setAttribute("state", state);

			String view = "search/pwdUp.jsp";
			RequestDispatcher rd = req.getRequestDispatcher(view);
			rd.forward(req, res);
		}catch(Exception e){
			System.out.println("e on login : " + e);
		}
	}
	private void searchIdForm(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String view = "search/idSearch.jsp";
		RequestDispatcher rd = req.getRequestDispatcher(view);
		rd.forward(req, res);
	}
	private void idSearch(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		MemberManager manager = MemberManager.getInstance();
		String name = req.getParameter("name");
		//String emailId = req.getParameter("emailId");
		//String emailS = req.getParameter("emailS");
		String email = req.getParameter("email");
		//System.out.println("name : " + name + " ,  email : " + email);
		String userid = manager.idSearchB(name, email);

		System.out.println("userid : " + userid);
		req.setAttribute("userid", userid);
		String view = "search/idSearchOk.jsp";
		RequestDispatcher rd = req.getRequestDispatcher(view);
		rd.forward(req, res);
	}
	private void dupleCheck3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		MemberManager manager = MemberManager.getInstance();
		String nick = req.getParameter("nick");
		String userid = req.getParameter("id");
		if(nick != null) nick = nick.trim();
		System.out.println(nick);
		if(userid != null)userid=userid.trim();
		String id = manager.idCheck2B(nick);

		res.setContentType("text/html;charset=UTF-8");
		//JSONObject obj = new JSONObject();
		PrintWriter pw = res.getWriter();
		
		System.out.println(nick + " : " + userid + " : " + id);
		if(!id.equals("")){//üũ���� ������ �ƴϸ� -> �г����� ������

			if(id.equals(userid)){
				pw.print("true");//������ ���̵��� �� ȸ���� ���̵�� �������ߺ��ƴѰɷ�
			}else{
				pw.print("false");
			}
		}
		else{
			//obj.put("value" , "can");
			//String json = obj.toString();
			pw.print("true");
		}
	}
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        //System.out.println("will decrypt : " + securedValue);
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // ���� ���ڵ� ����.
        return decryptedValue;
    }
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
	public String CryptoSHA3(String key, int hash) {
		// 1.x ����
        //DigestSHA3 md = new DigestSHA3(hash);
        // 2.x �̻� ���� 
	    DigestKeccak  md = new DigestKeccak(hash);
	    try {
	        md.update(key.getBytes("UTF-8"));
	    }
	    catch (UnsupportedEncodingException e){
	       e.printStackTrace();
	    }
	    byte[] digest = md.digest();
	 
	    return org.bouncycastle.util.encoders.Hex.toHexString(digest);
	}
}
	
/*
 	HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
*/