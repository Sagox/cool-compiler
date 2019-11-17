package cool;

import java.io.PrintWriter;
import java.util.*;

public class ArgumentInfo{
	public String name;
	public TypeUtils type;
	ArgumentInfo(String gname, TypeUtils t) {
		name = "%" + gname;
		type = t;
	}

}