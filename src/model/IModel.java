package model;

import java.io.File;
import java.util.List;

import view.View;

public interface IModel  {
	public List<MapNode> getMapNodes();
	public List<Section> getSections();
	public void loadDeliveryFile(File currentFile);
}
