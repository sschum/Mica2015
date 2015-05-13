package de.tarent.mica.maze.bot.strategy.navigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.tarent.mica.maze.model.Coord;

public class DijkstraTest {

	final Coord a = new Coord(0, 0);
	final Coord b = new Coord(1, 1);
	final Coord c = new Coord(2, 2);
	final Coord d = new Coord(3, 3);
	final Coord e = new Coord(4, 4);
	final Coord f = new Coord(5, 5);

	private Map<Route, Edge> testGraph(){
		Map<Route, Edge> nodes = new HashMap<>();

		/*
			 4__B
             /   \1
            A__2__C__3__D
             \    |5    |2
             4\___E__1__F

           -----

           A| 0| A
           B| 3| C
           C| 2| A
           D| 5| C
           E| 4| A
           F| 5| E
		 */

		final Edge eAB = spy(new Edge(new Route(a, b), a.east())); doReturn(4).when(eAB).getWeight();
		final Edge eBA = spy(new Edge(new Route(b, a), a.east())); doReturn(4).when(eBA).getWeight();
		nodes.put(new Route(eAB.getStart(), eAB.getEnd()), eAB);
		nodes.put(new Route(eBA.getStart(), eBA.getEnd()), eBA);

		final Edge eBC = spy(new Edge(new Route(b, c), b.east())); doReturn(1).when(eBC).getWeight();
		final Edge eCB = spy(new Edge(new Route(c, b), b.east())); doReturn(1).when(eCB).getWeight();
		nodes.put(new Route(eBC.getStart(), eBC.getEnd()), eBC);
		nodes.put(new Route(eCB.getStart(), eCB.getEnd()), eCB);

		final Edge eAC = spy(new Edge(new Route(a, c), a.east())); doReturn(2).when(eAC).getWeight();
		final Edge eCA = spy(new Edge(new Route(c, a), a.east())); doReturn(2).when(eCA).getWeight();
		nodes.put(new Route(eAC.getStart(), eAC.getEnd()), eAC);
		nodes.put(new Route(eCA.getStart(), eCA.getEnd()), eCA);

		final Edge eAE = spy(new Edge(new Route(a, e), a.east())); doReturn(4).when(eAE).getWeight();
		final Edge eEA = spy(new Edge(new Route(e, a), a.east())); doReturn(4).when(eEA).getWeight();
		nodes.put(new Route(eAE.getStart(), eAE.getEnd()), eAE);
		nodes.put(new Route(eEA.getStart(), eEA.getEnd()), eEA);

		final Edge eCD = spy(new Edge(new Route(c, d), c.east())); doReturn(3).when(eCD).getWeight();
		final Edge eDC = spy(new Edge(new Route(d, c), c.east())); doReturn(3).when(eDC).getWeight();
		nodes.put(new Route(eCD.getStart(), eCD.getEnd()), eCD);
		nodes.put(new Route(eDC.getStart(), eDC.getEnd()), eDC);

		final Edge eCE = spy(new Edge(new Route(c, e), c.east())); doReturn(5).when(eCE).getWeight();
		final Edge eEC = spy(new Edge(new Route(e, c), c.east())); doReturn(5).when(eEC).getWeight();
		nodes.put(new Route(eCE.getStart(), eCE.getEnd()), eCE);
		nodes.put(new Route(eEC.getStart(), eEC.getEnd()), eEC);

		final Edge eDF = spy(new Edge(new Route(d, f), d.east())); doReturn(2).when(eDF).getWeight();
		final Edge eFD = spy(new Edge(new Route(f, d), d.east())); doReturn(2).when(eFD).getWeight();
		nodes.put(new Route(eDF.getStart(), eDF.getEnd()), eDF);
		nodes.put(new Route(eFD.getStart(), eFD.getEnd()), eFD);

		final Edge eEF = spy(new Edge(new Route(e, f), e.east())); doReturn(1).when(eEF).getWeight();
		final Edge eFE = spy(new Edge(new Route(f, e), e.east())); doReturn(1).when(eFD).getWeight();
		nodes.put(new Route(eEF.getStart(), eEF.getEnd()), eEF);
		nodes.put(new Route(eFE.getStart(), eFE.getEnd()), eFE);

		return nodes;
	}

	private class DijkstraSpy extends Dijkstra{
		public DijkstraSpy(Coord start, Map<Route, Edge> nodes){
			super(start, nodes);
		}

		@Override
		protected Integer getWeight(Coord preNode, Coord curNode, Edge neighbor) {
			return neighbor.getWeight();
		}
	}

	/**
	 * @see <a href="https://www.youtube.com/watch?v=S8y-Sk7u1So">YouTube-Tutorial</a>
	 */
	@Test
	public void buildTable(){
		Map<Route, Edge> nodes = testGraph();

		Dijkstra dij = new DijkstraSpy(a, nodes);

		checkTable(dij, a, a, 0);
		checkTable(dij, b, c, 3);
		checkTable(dij, c, a, 2);
		checkTable(dij, d, c, 5);
		checkTable(dij, e, a, 4);
		checkTable(dij, f, e, 5);
	}

	private void checkTable(Dijkstra dij, Coord coord, Coord prenode, int distance) {
		assertTrue(dij.table.get(coord).visited);
		assertTrue(dij.table.get(coord).distance == distance);
		assertEquals(prenode, dij.table.get(coord).prenode);
	}

	@Test
	public void getNodes(){
		Map<Route, Edge> nodes = testGraph();

		Dijkstra dij = new Dijkstra(a, nodes);
		List<Coord> way = dij.getNodes(f);

		assertTrue(3 == way.size());
		assertEquals(a, way.get(0));
		assertEquals(e, way.get(1));
		assertEquals(f, way.get(2));
	}

	@Test
	public void getShortestWay(){
		Map<Route, Edge> nodes = testGraph();

		Dijkstra dij = new Dijkstra(a, nodes);
		List<Coord> way = dij.getShortestWay(f);

		assertTrue(5 == way.size());
		assertEquals(a, way.get(0));
		assertEquals(a.east(), way.get(1));
		assertEquals(e, way.get(2));
		assertEquals(e.east(), way.get(3));
		assertEquals(f, way.get(4));
	}
}
