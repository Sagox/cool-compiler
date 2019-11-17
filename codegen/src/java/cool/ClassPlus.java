package cool;
import java.util.*;

public class ClassPlus {
	public String name;
	public String parent = null;
	public HashMap <String, AST.attr> alist;
	public ArrayList<AST.attr> justAttrs = new ArrayList<AST.attr>();
	public HashMap <String, AST.method> mlist;
	
	ClassPlus(String nm, String pr, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml) {
		name = new String(nm);
		if(pr != null) parent = new String(pr);
		alist = new HashMap <String, AST.attr>();
		alist.putAll(al);
		Collection<AST.attr> values = alist.values();
		// System.out.println(alist.size());
		// System.out.println(values.size());
		// if(values.size() > 0)
		justAttrs = new ArrayList<AST.attr>();
		mlist = new HashMap <String, AST.method>();
		mlist.putAll(ml);
	}
}