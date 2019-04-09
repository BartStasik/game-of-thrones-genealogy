package com.got.genealogy.core.graph.collection;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrix<E> {

    private List<List<E>> matrix;

    public AdjacencyMatrix() {
        matrix = new ArrayList<>();
    }

    public AdjacencyMatrix(List<List<E>> matrix) {
        this.matrix = matrix;
    }

    public List<List<E>> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<List<E>> matrix) {
        this.matrix = matrix;
    }

    public int size() {
        return matrix.size();
    }

    public boolean isEmpty() {
        return matrix.isEmpty();
    }

    public List<E> getRow(int index) {
        return matrix.get(index);
    }

    public void setRow(int index, List<E> value) {
        matrix.set(index, value);
    }

    public void addRow() {
        addRow(new ArrayList<>());
    }

    public void addRow(List<E> value) {
        matrix.add(value);
    }

    public void fillRow(int index, E value) {
        // Matrix is symmetric, so row
        // index will also be the same
        // number of columns to add.
        for (int i = 0; i <= index; i++) {
            addCell(index, value);
        }
    }

    public void removeRow(int index) {
        matrix.remove(index);
    }

    public List<E> getColumn(int index) {
        return new ArrayList<>(matrix.get(index));
    }

    public void setColumn(int index, E value) {
        // Go through all rows and set
        for (List<E> row : matrix) {
            row.set(index, value);
        }
    }

    public void addColumn(E value) {
        // Go through all rows and add
        for (List<E> row : matrix) {
            row.add(value);
        }
    }

    public void removeColumn(int index) {
        // Go through all rows and remove
        for (List<E> row : matrix) {
            row.remove(index);
        }
    }

    public E getCell(int rowIndex, int columnIndex) {
        return matrix.get(rowIndex)
                .get(columnIndex);
    }

    public void setCell(int rowIndex, int columnIndex, E value) {
        matrix.get(rowIndex)
                .set(columnIndex, value);
    }

    public void addCell(int rowIndex, E value) {
        matrix.get(rowIndex)
                .add(value);
    }

    public E removeCell(int rowIndex, int columnIndex) {
        return matrix.get(rowIndex)
                .remove(columnIndex);
    }

}
