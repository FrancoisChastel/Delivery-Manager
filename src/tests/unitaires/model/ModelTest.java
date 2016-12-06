package tests.unitaires.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Model;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;

public class ModelTest {

	private Model model;
	private GraphDeliveryManager graphDelMan;
	private int[] sizes;
	
	@Before
	public void setUp() {
		model = new Model(null);
		graphDelMan = model.getGraphDeliveryManager();
		graphDelMan.getNodeList().add(new MapNode(0,100,100));
		graphDelMan.getSectionList().add(new Section(0,1,"S1",100,20));
		sizes = new int[2];
		sizes[0] = graphDelMan.getNodeList().size();	
		sizes[1] = graphDelMan.getSectionList().size();
	}
	
	@After
	public void tearDown() {
		model.resetModel();
	}
	
	
	/**
	 * 
	 */
	@Test
	public void ResetTest1() {
		model.resetModel();
		GraphDeliveryManager graphDelMan = model.getGraphDeliveryManager();
		int newSizes[] = new int[2];
		newSizes[0] = graphDelMan.getNodeList().size();
		newSizes[1] = graphDelMan.getSectionList().size();
		assertFalse(compareArrays(newSizes,sizes));
	}
	
	private Boolean compareArrays(int []first, int[] second)
	{
		for(int i=0;i<first.length;i++)
		{
			if(first[i]!=second[i])
			{
				return false;
			}
		}
		return true;
	}

	
	

}
