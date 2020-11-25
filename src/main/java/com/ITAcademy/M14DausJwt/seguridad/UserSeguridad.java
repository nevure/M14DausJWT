package com.ITAcademy.M14DausJwt.seguridad;

/**
 * clase de los usuarios logueados.
 * @author ru
 *
 */
public class UserSeguridad {

	public UserSeguridad() {
	}
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserSeguridad(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	
	
}
