package modeles.ecouteurs;

import java.util.EventListener;

import modeles.evenements.ProjetChangedEvent;
import modeles.evenements.ProjetListeAddedEvent;

public interface ProjetListener extends EventListener {
	public void projetChanged(ProjetChangedEvent event);
	public void projetListeAdded(ProjetListeAddedEvent event);
}
