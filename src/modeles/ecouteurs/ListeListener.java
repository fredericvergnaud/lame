package modeles.ecouteurs;

import java.util.EventListener;

import modeles.evenements.ListeChangedEvent;

public interface ListeListener extends EventListener {
	public void listeChanged(ListeChangedEvent event);
}
