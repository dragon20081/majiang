package business.junitTest;  
      
    import java.security.MessageDigest;  
import java.util.logging.Level;
import java.util.logging.Logger;

import server.http.Html_360Notice;
      
    /** 
     * 采用MD5加密解密 
     * @author tfq 
     * @datetime 2011-10-13 
     */  
    public class MD5Util {  
      
    	
    	private static final Logger logger = Logger.getLogger(MD5Util.class.getName());	
        /*** 
         * MD5加码 生成32位md5码 
         */  
        public synchronized static String string2MD5(String inStr){  
            MessageDigest md5 = null;  
            try{  
                md5 = MessageDigest.getInstance("MD5");  
            }catch (Exception e){  
                logger.log(Level.SEVERE,e.toString());
                e.printStackTrace();  
                return "";  
            }  
            char[] charArray = inStr.toCharArray();  
            byte[] byteArray = new byte[charArray.length];  
      
            for (int i = 0; i < charArray.length; i++)  
                byteArray[i] = (byte) charArray[i];  
            byte[] md5Bytes = md5.digest(byteArray);  
            StringBuffer hexValue = new StringBuffer();  
            for (int i = 0; i < md5Bytes.length; i++){  
                int val = ((int) md5Bytes[i]) & 0xff;  
                if (val < 16)  
                    hexValue.append("0");  
                hexValue.append(Integer.toHexString(val));  
            }  
            return hexValue.toString();  
      
        }  
      
        /** 
         * 加密解密算法 执行一次加密，两次解密 
         */   
        public static String convertMD5(String inStr){  
      
            char[] a = inStr.toCharArray();  
            for (int i = 0; i < a.length; i++){  
                a[i] = (char) (a[i] ^ 't');  
            }  
            String s = new String(a);  
            return s;  
      
        }  
      
        // 测试主函数  
        public static void main(String args[]) {  
          //  String s = new String("101#XXX201211091985#1234567890abcdefghijklmnopqrstuv#order1234#123456789#success#1211090012345678901#p1#md5#987654321#1106640b6df74133173be7328bae64c2");  
        	
        	  String  s = "abcdefg";	
        	System.out.println("原始：" + s);  
            System.out.println("MD5后：" + string2MD5(s));  
            System.out.println("加密的：" + convertMD5(s));  
            System.out.println("解密的：" + convertMD5(convertMD5(s)));  
      
        }  
    }  