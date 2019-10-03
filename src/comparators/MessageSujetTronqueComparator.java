package comparators;

import java.util.Comparator;
import modeles.MessageModel;

public class MessageSujetTronqueComparator implements Comparator<MessageModel> {

	@Override
	public int compare(MessageModel m1, MessageModel m2) {
		String sujetTronque1 = m1.getSujetTronque();
		String sujetTronque2 = m2.getSujetTronque();
		System.out.println("MessageSujetTronqueComparator - compare : sujetTronque1 = "+sujetTronque1+" | sujetTronque2 = "+sujetTronque2);

		return sujetTronque1.compareTo(sujetTronque2);
	}

}
