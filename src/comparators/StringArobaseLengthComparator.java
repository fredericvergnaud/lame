package comparators;

import java.util.Comparator;

public class StringArobaseLengthComparator implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		String[] tabNom1 = s1.split("\\$\\$\\$");
		String nom1 = tabNom1[0];
		String[] tabNom2 = s2.split("\\$\\$\\$");
		String nom2 = tabNom2[0];

		if (nom1.indexOf("@") == -1 && nom2.indexOf("@") != -1) {
			return -1;
		} else if (nom1.indexOf("@") != -1 && nom2.indexOf("@") == -1) {
			return 1;
		} else {
			if (s1.indexOf("indefini") == -1 && s2.indexOf("indefini") == -1)
				return nom2.length() - nom1.length();
			else if (s1.indexOf("indefini") == -1
					&& s2.indexOf("indefini") != -1)
				return -1;
			else if (s1.indexOf("indefini") != -1
					&& s2.indexOf("indefini") == -1)
				return 1;
			else
				return nom2.length() - nom1.length();
		}

	}
}
