package com.org.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.org.common.CommonConstant;

public class StringUtil {
    public static String mapStringToString(Map<String,String> map){
    	StringBuffer sb = null;
    	if(map != null && map.size() > 0){
    		sb = new StringBuffer();
    		Iterator<Entry<String,String>>  it = map.entrySet().iterator();
    		while(it.hasNext()){
    			Entry<String,String> entity = it.next();
    			sb.append(entity.getKey()+ "=" +entity.getValue().toString()+" ");
    		}
    	}
    	return sb.toString();
    }
	public  static String getUUIDValue(){
		return  UUID.randomUUID().toString()+System.currentTimeMillis();
	}
	

	
    public static byte[] union(byte[]bs1,byte[]bs2){
        byte[]bs=new byte[bs1.length+bs2.length];
        for(int i=0;i<bs1.length;i++)
            bs[i]=bs1[i];
        for(int i=0;i<bs2.length;i++)
            bs[bs1.length+i]=bs2[i];
        return bs;
    }
    
    /**
     * 对待转换为bcd的ascii码制字符串的验证模板
     */
    public static Pattern patternBcd = Pattern.compile("^[\\d|:|;|<|=|>|\\?]*$");
    
    /**
     * 对待转换为hex的ascii码制字符串的验证模板
     */
    public static Pattern patternHex = Pattern.compile("^[\\d|a-f|A-F]*$");

    /**
     * 换行字符
     */
    public static String LINE = System.getProperty("line.separator");
    
	private static final String[]NUMBER_ARRAY = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
	
	private static final String[]CURR_ARRAY = {"分","角","元","拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟","万","兆","拾","佰","仟","万"};
	
	private static final String UNITS="元万亿兆";

    /**
     * 左、右、两边填充字符
     * 
     * @param str
     *            待填充的字符串，可以为null
     * @param fillChar
     *            填充的字符
     * @param fillSide ['left','right','both']
     *            填充的方向，
     * @param size
     *            输出字符串的固定byte长度。
     * @return String
     */
    public static String charFill(String str, char fillChar, String  fillSide, int size) {
        str = (str == null) ? "" : str;
        StringBuffer sb = new StringBuffer(str);
        int len = str.length();
        if (len >= size) 
            return (("left".equals(fillSide)) ? str.substring(len - size) : str.substring(0, size));
        int n = size - len;
        if ("left".equals(fillSide))
        	for (int i = 0; i < n; ++i)
        		sb.insert(0, fillChar);
        else if ("right".equals(fillSide))
        	for (int i = 0; i < n; ++i)
        		sb.append(fillChar);
        else if ("both".equals(fillSide)) {
        	for (int i = 0; i < n; ++i) {
        		if (i % 2 == 0)
        			sb.insert(0, fillChar);
        		else
        			sb.append(fillChar);
            }
        }
        return sb.toString();
    }

    /**
     * 去除(左、右、两边)填充的字符
     * 
     * @param str      待去除字符的字符串
     * @param trimChar 待去除的字符
     * @param trimSide 方向 ['left','right','both']
     * @return String
     */
    public static String trimChar(String str, char trimChar, String fillSide) {
        if (str == null) return null;
        if (("left".equals(fillSide)) || ("both".equals(fillSide)) )
        	str = str.replaceAll("^" + trimChar + "+", "");
        if (("rigth".equals(fillSide)) || ("both".equals(fillSide)) )
        	str = str.replaceAll(trimChar + "+$", "");
        return str;
    }


    /**
     * ASCII -> BCD  <p>
     * 该方法会对ascii码制的参数str进行检测，以保证能正确转换为BCD
     * @param str
     * @return String
     */
    public static String ascii2bcd(String str) {
        try {
            Matcher m = patternBcd.matcher(str);
            if (!m.matches())
                throw new RuntimeException("不能将ascii字符串\"" + str + "\"转换为bcd格式。");
           
            return new String(ascii2bcd(str.getBytes(CommonConstant.UTF8)), CommonConstant.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static byte[] ascii2bcd(byte[] bs) {
        byte[] res = new byte[bs.length / 2];
        for (int i = 0, n = bs.length; i < n; i += 2)
            res[i / 2] = (byte) ((bs[i] << 4) | (bs[i + 1] & 0x0f));
        return res;
    }

    /**
     * BCD转换为Ascii码
     * @param bcdData
     *            待转换的bcd字符串
     * @return 转为为ascii后的字符串
     */
    public static String bcd2ascii(String bcdData) {
        try {
            return new String(bcd2ascii(bcdData.getBytes(CommonConstant.UTF8)), CommonConstant.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static byte[] bcd2ascii(byte[] bs) {
        byte[] res = new byte[bs.length * 2];
        for (int i = 0, n = bs.length; i < n; i++) {
            res[i * 2] = (byte) (((bs[i] & 0xf0) >> 4) | 0x30);
            res[i * 2 + 1] = (byte) ((bs[i] & 0x0f) | 0x30);
        }
        return res;
    }
    
    /**
     * Hex转换为Ascii码
     * <p>
     * 
     * <strong>注意: </strong> 该方法在windows下，将byte转换为String时，有可能存在数据丢失，此时应该
     * 使用bcd2ascii(byte[]bs)方法。
     * 
     * @param str
     *            待转换的字符串
     * @return 转为为ascii后的字符串
     */
    public static String hex2ascii(String str) {
        try {
        	
            return new String(hex2ascii(str.getBytes(CommonConstant.UTF8)), CommonConstant.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static byte[] hex2ascii(byte[] bs) {
        byte[] res = new byte[bs.length * 2];
        for (int i = 0; i < bs.length; i++) {
            int ti = bs[i];
            ti = ti < 0 ? ti + 256 : ti;
            String t = Integer.toHexString(ti);
            if (t.length() < 2) t = "0" + t;
            res[i * 2] = (byte) t.charAt(0);
            res[i * 2 + 1] = (byte) t.charAt(1);
        }
        return res;
    }
    
    /**
     * Ascii转换为16进制<br>
     * 该方法会对参数srt进行检查，是否包含非法字符。
     * @param str
     * @return String
     */
    public static String ascii2hex(String str) {
        Matcher m = patternHex.matcher(str);
        if (!m.matches())
            throw new RuntimeException("不能将ascii字符串 \"" + str + "\" 转换为Hex格式。");
        try {
            return new String(ascii2hex(str.getBytes(CommonConstant.UTF8)), CommonConstant.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static byte[] ascii2hex(byte[] bs) {
        byte[] res = new byte[bs.length / 2];
        for (int i = 0, n = bs.length; i < n; i += 2) {
            res[i / 2] = (byte) (Integer.parseInt(new String(bs, i, 2), 16));
        }
        return res;
    }

    /**
     * 检查是否全部是hex合法字符<br>
     * 主要是jdk1.3不支持正则表达式
     */
    @SuppressWarnings("unused")
	private static void checkHexChar(String str){
        if(str!=null){
            byte[]bs=str.toUpperCase().getBytes();
            for(int i=0;i<bs.length;i++){
                if(!(bs[i]>0x29&&bs[i]<0x3a)&&!(bs[i]>0x40&&bs[i]<0x5b)){
                    throw new RuntimeException("\""+str+"\"中包含非法的Hex字符。必须是0-9,a-z,A-Z。");
                }
            }
        }
        else
            throw new RuntimeException("\""+str+"\"中包含非法的Hex字符。必须是0-9,a-z,A-Z。");
    }

    /**
     * 检查是否全部是bcd合法字符<br>
     * 主要是jdk1.3不支持正则表达式
     */
    @SuppressWarnings("unused")
	private static void checkBcdChar(String str){
        if(str!=null){
            byte[]bs=str.getBytes();
            for(int i=0;i<bs.length;i++){
                if(!(bs[i]>0x29&&bs[i]<0x40)){
                    throw new RuntimeException("\""+str+"\"中包含非法的Bcd字符。必须是0-9:;<=>?");
                }
            }
        }
        else
            throw new RuntimeException("\""+str+"\"中包含非法的Bcd字符。必须是0-9:;<=>?");
    }
    
    /**
     * 获取字符串的长度
     * @param str
     * @return int
     */
    public static int getByteLength(String str){
    	try{
    		return str.getBytes(CommonConstant.UTF8).length;
    	}catch(Exception e){
    		return -1;
    	}
    }
    
    /**
     * 只显示0x20 - 0x80 之间的字符
     */
    public static String getByteText(byte[] bs){
        byte[] bs2=new byte[bs.length];
        for(int i=0;i<bs.length;i++)
            bs2[i]=(bs[i]<0x20 || bs[i]>0x80)?0x20:bs[i];
        return new String(bs2);
    }
    
	/**
	 * 金额大写转换
	 * @param moneyStr
	 * @return
	 */
	public static String transMoney(String moneyStr) {
		BigDecimal bdNum = new BigDecimal(moneyStr);
		moneyStr = bdNum.multiply(new BigDecimal(100)).toString();
		int donet=moneyStr.indexOf(".");
		moneyStr=donet!=-1?moneyStr.substring(0,donet):moneyStr;
		int n = moneyStr.length();
		if(n>CURR_ARRAY.length){
			throw new RuntimeException("数据的整数部分长度不能超过"+(CURR_ARRAY.length-2)+"位。");
		}
		StringBuffer sb=new StringBuffer();
		boolean needUnit = false,preIsZero = false;
		for(int i=0;i<n;i++) { //只要有一个元素不为0,就需要单位； 如果前面有零，则不重复添加。
			int a123=Integer.parseInt(moneyStr.substring(i,i+1));
			String numStr = NUMBER_ARRAY[a123];
			String currStr =CURR_ARRAY[n-i-1];
			boolean isUnit=UNITS.indexOf(currStr)!=-1;
			needUnit=needUnit||a123!=0;
			sb.append(a123!=0?((preIsZero&&(i<n-2)?"零":"")+numStr+currStr):((needUnit&&isUnit)||(i==n-3)?currStr:""));
			needUnit=isUnit?false:needUnit;
			preIsZero=(a123!=0||(isUnit&&needUnit))?false:true;
		}
		return sb.append("整").toString();
	}
	
	
	

	/**
	 * 将double转换为分的整形字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String toFen(double value){
		return format(value).replaceAll(",","").replaceAll("\\.","");
	}
	
	
	/**
	 * 将分格式化为正确的元角分格式
	 * @param str
	 * @return
	 */
	public static String format(double value){
		return NumberFormat.getCurrencyInstance(Locale.CHINA).format(value).replaceAll("￥","");
	}
	
    /**
     * equals function that actually compares two buffers.
     *
     * @param onearray First buffer
     * @param twoarray Second buffer
     * @return true if one and two contain exactly the same content, else false.
     */
    public static boolean bufEquals(byte onearray[], byte twoarray[] ) {
    	if (onearray == twoarray) return true;
        boolean ret = (onearray.length == twoarray.length);
        if (!ret) {
            return ret;
        }
        for (int idx = 0; idx < onearray.length; idx++) {
            if (onearray[idx] != twoarray[idx]) {
                return false;
            }
        }
        return true;
    }
    
    private static final char[] hexchars = { '0', '1', '2', '3', '4', '5',
                                            '6', '7', '8', '9', 'A', 'B',
                                            'C', 'D', 'E', 'F' };
    /**
     * 
     * @param s 
     * @return 
     */
    static String toXMLString(String s) {
        if (s == null)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < s.length(); idx++) {
          char ch = s.charAt(idx);
          if (ch == '<') {
            sb.append("&lt;");
          } else if (ch == '&') {
            sb.append("&amp;");
          } else if (ch == '%') {
            sb.append("%25");
          } else if (ch < 0x20) {
            sb.append("%");
            sb.append(hexchars[ch/16]);
            sb.append(hexchars[ch%16]);
          } else {
            sb.append(ch);
          }
        }
        return sb.toString();
    }
    
    static private int h2c(char ch) {
      if (ch >= '0' && ch <= '9') {
        return ch - '0';
      } else if (ch >= 'A' && ch <= 'F') {
        return ch - 'A';
      } else if (ch >= 'a' && ch <= 'f') {
        return ch - 'a';
      }
      return 0;
    }
    
    /**
     * 
     * @param s 
     * @return 
     */
    static String fromXMLString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < s.length();) {
          char ch = s.charAt(idx++);
          if (ch == '%') {
            char ch1 = s.charAt(idx++);
            char ch2 = s.charAt(idx++);
            char res = (char)(h2c(ch1)*16 + h2c(ch2));
            sb.append(res);
          } else {
            sb.append(ch);
          }
        }
        
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @return 
     */
   public  static String toCSVString(String s) {
        if (s == null)
            return "";

        StringBuilder sb = new StringBuilder(s.length()+1);
        sb.append('\'');
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            switch(c) {
                case '\0':
                    sb.append("%00");
                    break;
                case '\n':
                    sb.append("%0A");
                    break;
                case '\r':
                    sb.append("%0D");
                    break;
                case ',':
                    sb.append("%2C");
                    break;
                case '}':
                    sb.append("%7D");
                    break;
                case '%':
                    sb.append("%25");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @throws java.io.IOException 
     * @return 
     */
    static String fromCSVString(String s) throws IOException {
        if (s.charAt(0) != '\'') {
            throw new IOException("Error deserializing string.");
        }
        int len = s.length();
        StringBuilder sb = new StringBuilder(len-1);
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '%') {
                char ch1 = s.charAt(i+1);
                char ch2 = s.charAt(i+2);
                i += 2;
                if (ch1 == '0' && ch2 == '0') { sb.append('\0'); }
                else if (ch1 == '0' && ch2 == 'A') { sb.append('\n'); }
                else if (ch1 == '0' && ch2 == 'D') { sb.append('\r'); }
                else if (ch1 == '2' && ch2 == 'C') { sb.append(','); }
                else if (ch1 == '7' && ch2 == 'D') { sb.append('}'); }
                else if (ch1 == '2' && ch2 == '5') { sb.append('%'); }
                else {throw new IOException("Error deserializing string.");}
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @return 
     */
    static String toXMLBuffer(byte barr[]) {
        if (barr == null || barr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(2*barr.length);
        for (int idx = 0; idx < barr.length; idx++) {
            sb.append(Integer.toHexString(barr[idx]));
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @throws java.io.IOException 
     * @return 
     */
    static byte[] fromXMLBuffer(String s)
    throws IOException {
        ByteArrayOutputStream stream =  new ByteArrayOutputStream();
        if (s.length() == 0) { return stream.toByteArray(); }
        int blen = s.length()/2;
        byte[] barr = new byte[blen];
        for (int idx = 0; idx < blen; idx++) {
            char c1 = s.charAt(2*idx);
            char c2 = s.charAt(2*idx+1);
            barr[idx] = Byte.parseByte(""+c1+c2, 16);
        }
        stream.write(barr);
        return stream.toByteArray();
    }
   
    
    /**
     * 
     * @param date 日期
     * @param patteran  格式化日期输出
     * @return
     */
    public static String getDateString(Date date,String pattern){
    	SimpleDateFormat sf = new SimpleDateFormat(pattern);
    	return sf.format(date);
    }
    
    
    /**
     * 字符串转对应日期格式
     * @param dateStr 字符串日期
     * @param pattern  yyyyMMddHHmmss
     * @return
     */
    public static Date getDate(String dateStr,String pattern){
    	SimpleDateFormat sf = new SimpleDateFormat(pattern);
    	try {
			return sf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @param length 长度值
     * @param size 转化为字符串后的长度位
     * @return 长度的字符表示形式 中间添加空格
     */
    public static String toLenStr(int length,int size){
    	String lenStr = ""+length;
    	for (int i = lenStr.length(); i < size; i++) {
    		if( (i%2) == 0){
    			lenStr = "0 " + lenStr;
    		}else{
    			lenStr = "0" + lenStr;
    		}
    		
		}
    	return lenStr;
    }
    
    /**
     * 
     * @param length 长度值
     * @param size 转化为字符串后的长度位
     * @return 长度的字符表示形式 左边补0
     */
    public static String toLen(int length,int size){
    	String lenStr = ""+length;
    	for (int i = lenStr.length(); i < size; i++) {
    	   lenStr = "0" + lenStr;
		}
    	return lenStr;
    }
    
    public static String toMoneyStr(String money,int size){
    	String moneyStr = "000000000000";
    	if(money != null){
    		int mlen = money.length();
    		for (; mlen < size; mlen++) {
    			money = "0" + money;
			}
    		moneyStr = money;
    	}
    	return moneyStr;
    }
    
    
    public static String toBitMapStr(boolean[] bitmap){
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < bitmap.length; i++) {
    		if(i%8 == 0 && i > 0){//8的倍数 加一个空格
    			sb.append(" ");
    		}
    		if(bitmap[i]){
				sb.append("1");
			}else{
				sb.append("0");
			}
		
		}
    	return sb.toString();
    }
    
    
    
	/**
	 * 获取指定日期前一天的字符串
	 * 
	 * @param patten
	 *            格式化字符串
	 * @return
	 */
	public static String getBeforeDate(Date date, String patten) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-1);
		SimpleDateFormat sf = new SimpleDateFormat(patten);
		return sf.format(c.getTime());
	}
	
	public static String getAfterDate(Date date, String patten,int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+day);
		SimpleDateFormat sf = new SimpleDateFormat(patten);
		return sf.format(c.getTime());
	}
	
	
	
    
    /**
     * 
     * @param buf 
     * @return 
     */
   public static String toCSVBuffer(byte barr[]) {
        if (barr == null || barr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(barr.length + 1);
        sb.append('#');
        for(int idx = 0; idx < barr.length; idx++) {
            sb.append(Integer.toHexString(barr[idx]));
        }
        return sb.toString();
    }
    
    /**
     * Converts a CSV-serialized representation of buffer to a new
     * ByteArrayOutputStream.
     * @param s CSV-serialized representation of buffer
     * @throws java.io.IOException 
     * @return Deserialized ByteArrayOutputStream
     */
    static byte[] fromCSVBuffer(String s)
    throws IOException {
        if (s.charAt(0) != '#') {
            throw new IOException("Error deserializing buffer.");
        }
        ByteArrayOutputStream stream =  new ByteArrayOutputStream();
        if (s.length() == 1) { return stream.toByteArray(); }
        int blen = (s.length()-1)/2;
        byte[] barr = new byte[blen];
        for (int idx = 0; idx < blen; idx++) {
            char c1 = s.charAt(2*idx+1);
            char c2 = s.charAt(2*idx+2);
            barr[idx] = Byte.parseByte(""+c1+c2, 16);
        }
        stream.write(barr);
        return stream.toByteArray();
    }
    public static int compareBytes(byte b1[], int off1, int len1, byte b2[], int off2, int len2) {
    	int i;
    	for(i=0; i < len1 && i < len2; i++) {
    		if (b1[off1+i] != b2[off2+i]) {
    			return b1[off1+i] < b2[off2+1] ? -1 : 1;
    		}
    	}
    	if (len1 != len2) {
    		return len1 < len2 ? -1 : 1;
    	}
    	return 0;
    } 
    
    public static String arraysToString(Object[] obj){
    	if(obj != null)
    		return Arrays.toString(obj);
    	else
    		return null;
    } 
    
    public static String mapToString(Map<String,String[]> map){
    	StringBuffer sb = new StringBuffer();
    	if(map != null && map.size() > 0){
    		sb = new StringBuffer();
    		Iterator<Entry<String, String[]>>  it = map.entrySet().iterator();
    		while(it.hasNext()){
    			Entry<String, String[]> entity = it.next();
    			sb.append(Arrays.toString(entity.getValue()));
    		}
    	}
    	return sb.toString();
    } 
    
    public static String mapObjectToString(Map<Integer,Object> map){
    	StringBuffer sb = null;
    	if(map != null && map.size() > 0){
    		sb = new StringBuffer();
    		Iterator<Entry<Integer, Object>>  it = map.entrySet().iterator();
    		while(it.hasNext()){
    			Entry<Integer, Object> entity = it.next();
    			sb.append(entity.getValue().toString()).append(",");
    		}
    	}
    	return sb.toString();
    }
    
    public static String bigdecimalToString(BigDecimal amount){
    	return amount.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
    }
    
    public static BigDecimal stringToBigDecimal(String  amount){
    	return new BigDecimal(amount).multiply(new BigDecimal("0.01")).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
    
    public static long dateStringToLong(String dateString,String pattern){
    	return StringUtil.getDate(dateString, pattern).getTime();
    }
    
    //校验位 = 10 – [(奇数位的和)*3 + (偶数位的和)]的个位
    public static String calParityBit(String org){
    	String parityBit = "";
    	if(org != null && org.trim().length() > 0){
    		int len = org.length();
    		int init = 10;
    		int odd  = 0;
    		int even = 0;
    		for (int i = 0; i < len; i++) {
				if((i+1)%2 == 0){//偶数
					even = even + Integer.parseInt(org.substring(i, i+1));
				}else{//奇数
					odd = odd + Integer.parseInt(org.substring(i, i+1));
				}
			}
    		parityBit = (init - (odd * 3 + even))+"";
    		parityBit = parityBit.substring(parityBit.length()-1, parityBit.length());
    	}
    	return parityBit;
    } 
    
    public static Map<String,String> translateMap(String str){
    	Map<String, String> map = null;
    	if(str != null && str.length() > 0){
    		map = new HashMap<String, String>();
    		String name = "";
    		String value = "";
    		int beg = 0;  
    		int end = 0;
    		int i = 0;
    		while((i = str.indexOf("[", beg)) >0){
    			end  = i;
    			i    = 0;
    			name  = str.substring(beg,end);
    			beg   = str.indexOf("]", end);
    			value = str.substring(end+1,beg);
    			
    			beg = beg +1;
    			map.put(name, value);
    		}
    	}
    	return map;	
    }
    
    public static Map<String,String> mediumTypeMap = new HashMap<String, String>();
    static {
    	//00:杉德宝通用账 01：杉德宝专用账户 02：杉德卡账户 03：杉德卡钱包  
    	mediumTypeMap.put("00", "0");
    	mediumTypeMap.put("01", "1");
    	mediumTypeMap.put("02", "2");
    	mediumTypeMap.put("03", "2");
    	mediumTypeMap.put("04", "2");
    	mediumTypeMap.put("05", "2");
    }
    public static String getMediumType(String mediumType){
    	if(mediumTypeMap.containsKey(mediumType)){
    		return mediumTypeMap.get(mediumType);
    	}
    	return "";
    }
    
    /**
     * @param num  如：000000001230
     * @return numStr  1230
     */
    public static String numberToNumStr(String num){
    	return "" + Long.parseLong(num);
    }
    
    /**
     * @param numStr  1230
     * @return num 000000001230
     */
    public static String strNumToNumber(String numStr){
    	return StringUtil.charFill(numStr, '0', "left", 12);
    }
    
    public static String getIp(){
    	String ip = "";
		try {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}   
    	return ip;
    }
    /**
     * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank("")        = true
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isBlank("bob")     = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str 要检查的字符串
     *
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    
    public static String simpleClassName(Class<?> clazz) {
        if (clazz == null) {
            return "null_class";
        }

        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return clazz.getName().substring(pkg.getName().length() + 1);
        } else {
            return clazz.getName();
        }
    }
     
	public static void main(String [] args){
		String s      = "sysCode[01]cardNo[7279906001000007]issueNo[050001]rechargeNo[0727990601000007]physicalNo[]expiredDate[20230528]";
		
		StringUtil.translateMap(s);	
	}

	public static String toEntityName(String fieldName, boolean toUpper) {
		String newFieldName = "";
		String[] fieldNameAry = fieldName.split("");
		for (int i = 0; i < fieldNameAry.length; i++) {
			String fieldNameTmp = fieldNameAry[i];
			if(fieldNameTmp.equals("_")){
				i++;
				fieldNameTmp = fieldNameAry[i];
				if(toUpper){
					fieldNameTmp = fieldNameTmp.toUpperCase();
				}
			}
			newFieldName += fieldNameTmp;
		}

		return newFieldName;
	}
    public static Long getTimestamp(){
    	return new Date().getTime()/1000;
    } 
}
