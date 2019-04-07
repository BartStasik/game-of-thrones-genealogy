package com.got.genealogy;

import com.got.genealogy.core.graph.Graph;
import com.got.genealogy.core.graph.Weight;
import com.got.genealogy.core.person.Person;

public class Main {
    public static void main(String[] args) {
        Graph<Person, Integer> graph1 = new Graph<>(false);

        Person a1 = new Person("a");
        Person b1 = new Person("b");
        Person c1 = new Person("c");
        Person d1 = new Person("d");

        graph1.addVertex(a1);
        graph1.addVertex(b1);
        graph1.addVertex(c1);
        graph1.addVertex(d1);

        graph1.addEdge(a1, b1, new Weight<>(1));
        graph1.addEdge(a1, c1, new Weight<>(2));
        graph1.addEdge(b1, d1, new Weight<>(3));

        graph1.printGraph();

        graph1.removeVertex(c1);

        graph1.printGraph();

        Graph<Person, Integer> graph2 = new Graph<>(true);

        Person a2 = new Person("a");
        Person b2 = new Person("b");
        Person c2 = new Person("c");
        Person d2 = new Person("d");

        graph2.addVertex(a2);
        graph2.addVertex(b2);
        graph2.addVertex(c2);
        graph2.addVertex(d2);

        graph2.addEdge(a2, b2, new Weight<>(1));
        graph2.addEdge(a2, c2, new Weight<>(2));
        graph2.addEdge(b2, d2, new Weight<>(3));

        graph2.printGraph();

        graph2.removeEdge(a2, b2);

        graph2.printGraph();
    }
}
