package njs;

import javax.script.*;

import lang.*;
import lang.exceptions.Exception;
import jdk.nashorn.api.scripting.*;

/*To compile this class in Eclipse, you must first enable restricted api access in the Eclipse compiler preferences.*/

public class Main {
	@SuppressWarnings("restriction")
	public static FObj convert(Object o){
		if(o instanceof Integer){
			return new FNum((int)o);
		}
		if(o instanceof String){
			return new FString((String)o);
		}
		if(o instanceof Long){
			return new FNum((long)o);
		}
		if(o instanceof Double){
			double d=(double)o;
			return new FNum((long)(1e16*d),(long)1e16);
		}
		if(o instanceof Boolean){
			return FBool.valueOf((boolean)o);
		}
		if(o instanceof JSObject){
			JSObject obj=(JSObject)o;
			if(obj.isArray()){
				FList f=new FList();
				long a=(long)obj.getMember("length");
				for(int i=0;i<a;i++){
					f.append((obj.hasSlot(i)?convert(obj.getSlot(i)):null));
				}
				return f;
			}
		}
		return null;
	}
	@SuppressWarnings("serial")
	public static class JavascriptException extends Exception{public JavascriptException(String name){super(name);}}
	public static void load(Module m){
		final ScriptEngine js=new ScriptEngineManager().getEngineByExtension("js");
		try {
			js.eval("function alert(a){var t=Java.type('javax.swing.JOptionPane');var u=new (Java.type('javax.swing.JFrame'))();u.setAlwaysOnTop(true);t.showMessageDialog(u,a,'',t.INFORMATION_MESSAGE);u.dispose();}");
			js.eval("function prompt(){var t=Java.type('javax.swing.JOptionPane');var u=new (Java.type('javax.swing.JFrame'))();u.setAlwaysOnTop(true);var v=t.showInputDialog(u,'');u.dispose();return v}");
		} catch (ScriptException e) {
			System.out.println(e);
			throw new RuntimeException(e);
		}
		m.set("._invoke",new Function(a->{
			try{
				return convert(js.eval(a));
			}catch(ScriptException e){
				throw new JavascriptException(e.toString());
			}
		}));
	}
}
