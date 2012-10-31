package com.jasonhsu.libraryfinder;

public class AddressDetails {
	
	public String getAddress() {
		return Address;
	}
	public void setAddress(String Address) {
		this.Address = Address;
	}
	public String getLatStr() {
		return LatStr;
	}
	public void setLatStr(String LatStr) {
		this.LatStr = LatStr;
	}
	public String getLongStr() {
		return LongStr;
	}
	public void setLongStr (String LongStr) {
		this.LongStr = LongStr;
	}
	
	private String Address;
	private String LatStr;
	private String LongStr;

}
