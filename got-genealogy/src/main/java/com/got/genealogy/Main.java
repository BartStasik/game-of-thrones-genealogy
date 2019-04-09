package com.got.genealogy;

import com.got.genealogy.core.family.Relation;
import com.got.genealogy.core.graph.property.Weight;
import com.got.genealogy.core.family.FamilyTree;
import com.got.genealogy.core.family.Person;

public class Main {

    public static void main(String[] args) {
        FamilyTree graph1 = new FamilyTree(false);

        Person a = new Person("a");
        Person b = new Person("b");
        Person c = new Person("c");
        Person d = new Person("d");

        Relation defaultNull = new Relation("0");

        graph1.addVertex(a);
        graph1.addVertex(b);
        graph1.addVertex(c);
        graph1.addVertex(d);

        graph1.addEdge(a, b,
                new Weight<>(new Relation("1")));
        graph1.addEdge(a, c,
                new Weight<>(new Relation("2")));
        graph1.addEdge(b, d,
                new Weight<>(new Relation("3")));

        System.out.println("\n\nGraph 1");
        graph1.printGraph(defaultNull);

        graph1.removeVertex(c);

        System.out.println("\n\nRemoved vertex [c]");
        graph1.printGraph(defaultNull);

        graph1.addVertex(c);

        System.out.println("\n\nAdded vertex [c]");
        graph1.printGraph(defaultNull);

        graph1.removeEdge(a, b);

        System.out.println("\n\nRemoved edge [a -> b]");
        graph1.printGraph(defaultNull);

        graph1.addEdge(c, d, new Weight<>(new Relation("4")));

        System.out.println("\n\nAdded edge [c -> d]");
        graph1.printGraph(defaultNull);

        System.out.println("\n\nGet [a] annotation: " + graph1.getAnnotation(a));
        graph1.setAnnotation(a, "A");
        System.out.println("Set [a] annotation: " + graph1.getAnnotation(a));

        System.out.println("\n\n[a] visited: " + a.isVisited());
        graph1.visitVertex(a);
        System.out.println("\nVisited [a]");
        System.out.println("[a] visited: " + a.isVisited());
        System.out.println("\nLeft [a]");
        graph1.leaveVertex(a);
        System.out.println("[a] visited: " + a.isVisited());
    }
}
