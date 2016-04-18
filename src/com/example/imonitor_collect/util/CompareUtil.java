package com.example.imonitor_collect.util;

public class CompareUtil {
	public static boolean bytesEqual(byte[] b1,byte[] b2){
		if(b1.length!=b2.length)
			return false;
		for(int i=0;i<b1.length;i++){
			if(b1[i]!=b2[i])
				return false;
		}
		return true;
	}
}
