package edu.cmu.lti.oaqa.graph.core.utils;

import java.util.ArrayList;

import com.tinkerpop.blueprints.Vertex;

public class Primitives {
	
	public static long stringToLong(String string){
		boolean isNeg = isNegString(string);
		if (isNeg == true) string = string.substring(1);
		long number=0;
		for(int x=0;x<string.length();x++){
			number=number*10;
			char c = string.charAt(x);
			if(c=='0') number+=0;
			else if(c=='1') number+=1;
			else if(c=='2') number+=2;
			else if(c=='3') number+=3;
			else if(c=='4') number+=4;
			else if(c=='5') number+=5;
			else if(c=='6') number+=6;
			else if(c=='7') number+=7;
			else if(c=='8') number+=8;
			else if(c=='9') number+=9;
			else throw new Error();
		}
		if (isNeg == true) number=number*-1;
		return number;
	}
	
	public static double stringToDouble(String string){
		if(!string.contains(".")) return (double)stringToLong(string);
		boolean isNeg = isNegString(string);
		if (isNeg == true) string = string.substring(1);
		if(string.contains("-")) throw new Error("invalid negative sign");
		if(string.equals("")) throw new Error("empty string");
		if(string.indexOf('.')!=string.lastIndexOf('.')) throw new Error("too many decimals");
		int decimalLoc = string.indexOf('.');
		String sNoDecimal = string.replace('.', 'h').replaceAll("h", "");
		if(sNoDecimal.equals("")) throw new Error("empty string");
		long intNumber = stringToLong(sNoDecimal);
		double number = (double) (intNumber/(double) Math.pow(10, (string.length()-1-decimalLoc)));
		if (isNeg == true) number=number*-1;
		return number;
	}

	private static boolean isNegString(String string) {
		return string.charAt(0)=='-';
	}

	public static String intToString(int number){
		int d;
		String returnString="";
		while(number>0){
			d=number%10;
			if(d==0) returnString="0"+returnString;
			else if(d==1) returnString="1"+returnString;
			else if(d==2) returnString="2"+returnString;
			else if(d==3) returnString="3"+returnString;
			else if(d==4) returnString="4"+returnString;
			else if(d==5) returnString="5"+returnString;
			else if(d==6) returnString="6"+returnString;
			else if(d==7) returnString="7"+returnString;
			else if(d==8) returnString="8"+returnString;
			else if(d==9) returnString="9"+returnString;
			else throw new Error();
			number/=10;
		}
		return returnString;
	}
	
	public static String[] addToStringArray (String[] array, String s){
		String[] newArray = new String[array.length+1];
		int count=0;
		for(String string:array){
			newArray[count++]=string;
		}
		newArray[count]=s;
		return newArray;
	}
	
	public static String[] addToStringArray (String[] array, String s, Boolean allowDups){
		if(stringArrayContains(array,s) && allowDups==false) return array; 
		String[] newArray = new String[array.length+1];
		int count=0;
		for(String string:array){
			newArray[count++]=string;
		}
		newArray[count]=s;
		return newArray;
	}
	
	public static boolean stringArrayContains(String[] array, String s){
		for(String string:array){
			if (string.equals(s)) return true;
		}
		return false;
	}
		
	public static String[] stringToArray (String s){
		String[] split = s.split(" ");
		return addToStringArray(split,s);
	}
		
	public static String[] mergeStringArrays (String[] a, String[] b){
		int length = a.length + b.length;
		String[] merged = new String[length];
		int loc=0;
		for(int i=0;i<a.length;i++){
			merged[loc]=a[i];
			loc++;
		}
		for(int i=0;i<b.length;i++){
			merged[loc]=b[i];
			loc++;
		}
		return merged;
	}
	
	public static String[] mergeStringArrays (String[] a, String[] b, boolean allowDups){
		if (allowDups==true) return mergeStringArrays(a,b);
		ArrayList<String> strings = new ArrayList<String>();
		for (String s:a) strings.add(s);
		for (String s:b) strings.add(s);
		String[] returnString = new String[strings.size()];
		int count=0;
		for(String s:strings) returnString[count++] = s;
		return returnString;
	}
	
	public static void addToStringArrayProperty(Vertex vertex, String prop, String item){
		addToStringArrayProperty(vertex,prop,item,false);
	}
	
	public static void mergeStringArrayProperty(Vertex vertex, String prop, String[] addArray, boolean allowDups){
		String[] stringArray = (String[]) vertex.getProperty(prop);
		if(stringArray!=null) vertex.setProperty(prop, mergeStringArrays(stringArray,addArray,allowDups));
		else vertex.setProperty(prop, addArray);
	}
	
	public static void addToStringArrayProperty(Vertex vertex, String prop, String item, boolean allowDups){
		String[] stringArray = (String[]) vertex.getProperty(prop);
		if (stringArray==null){
			vertex.setProperty(prop, new String[] {item});
		}
		else{
			vertex.setProperty(prop, addToStringArray(stringArray,item,allowDups));
		}
	}

}
