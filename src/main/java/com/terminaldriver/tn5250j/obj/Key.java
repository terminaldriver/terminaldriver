package com.terminaldriver.tn5250j.obj;

public enum Key{
	ATTENTION("[attn]"),
	SYSTEM_REQUEST("[sysreq]"),
	RESET("[reset]"),
	CLEAR("[clear]"),
	HELP("[help]"),
	PG_UP("[pgup]"),
	PG_DOWN("[pgdown]"),
	ROLL_LEFT("[rollleft]"),
	ROLL_RIGHT("[rollright]"),
	FIELD_EXIT("[fldext]"),
	FIELD_PLUS("[field+]"),
	FIELD_MINUS("[field-]"),
	BOF("[bof]"),
	ENTER("[enter]"),
	HOME("[home]"),
	INSERT("[insert]"),
	BACKSPACE("[backspace]"),
	BACKTAB("[backtab]"),
	UP("[up]"),
	DOWN("[down]"),
	LEFT("[left]"),
	RIGHT("[right]"),
	DELETE("[delete]"),
	TAB("[tab]"),
	EOF("[eof]"),
	ERASE_EOF("[eraseeof]"),
	ERASE_FIELD("[erasefld]");
	
	private String code;
	Key(String code){this.code= code;}
	@Override
	public String toString(){return code;}
}