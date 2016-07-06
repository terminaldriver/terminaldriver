package com.terminaldriver.tn5250j.mock;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;

public class MockFindByUtil {
	public static FindBy createFindBy(final Map<String,?> map){
		return new FindBy(){

			public Class<? extends Annotation> annotationType() {
				return FindBy.class;
			}

			public How how() {
				return (How) map.get("how");
			}

			public String using() {
				return (String) map.get("using");
			}

			public String labelText() {
				return (String) map.get("labelText");
			}

			public String text() {
				return (String) map.get("text");
			}

			public String name() {
				return (String) map.get("name");
			}

			public int row() {
				return (Integer) map.get("row");
			}

			public int column() {
				return (Integer) map.get("column");
			}

			public int length() {
				return (Integer) map.get("length");
			}

			public ScreenAttribute attribute() {
				if (map.get("attribute") instanceof Character){
					return ScreenAttribute.getAttrEnum((Character)map.get("attribute") );
				}
				return (ScreenAttribute) map.get("attribute");
			}
			
		};
	}
}
