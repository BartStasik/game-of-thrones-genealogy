package com.got.genealogy.core.graph.collection;

import com.got.genealogy.core.graph.property.Weight;

import java.util.ArrayList;
import java.util.List;

/**
 * AdjacencyMatrix object, using a 2D List to
 * store the edges and weights between vertices,
 * corresponding to row/column index values.
 * <p>
 * This object is used directly by Graph.
 *
 * @param <E> holds edges with the wrapper class,
 *           Weight.
 */
public class AdjacencyMatrix<E extends Weight> {

    private List<List<E>> matrix;

    /**
     * Constructor for the class, initialising
     * the matrix.
     */
    public AdjacencyMatrix() {
        matrix = new ArrayList<>();
    }

    /**
     * Constructor for the class, initialising
     * the matrix, with the provided parameter.
     *
     * @param matrix 2D List of a generic type.
     */
    public AdjacencyMatrix(List<List<E>> matrix) {
        this.matrix = matrix;
    }

    /**
     * Gets the 2D List that stores the matrix.
     *
     * @return 2D list of a generic type.
     */
    public List<List<E>> getMatrix() {
        return matrix;
    }

    /**
     * Replaces the 2D List that stores the matrix.
     *
     * @param matrix 2D List of a generic type.
     */
    public void setMatrix(List<List<E>> matrix) {
        this.matrix = matrix;
    }

    /**
     * Returns the number of rows in the matrix.
     *
     * @return matrix size int.
     */
    public int size() {
        return matrix.size();
    }

    /**
     * Returns <tt>true</tt> if the matrix is
     * empty.
     *
     * @return Returns <tt>true</tt> if the
     * matrix is empty.
     */
    public boolean isEmpty() {
        return matrix.isEmpty();
    }

    /**
     * Gets a matrix row, using the positional
     * index.
     *
     * @param index position of the required row
     *              in the matrix.
     * @return List of weight cells for the row.
     */
    public List<E> getRow(int index) {
        return matrix.get(index);
    }

    /**
     * Replaces a matrix row.
     *
     * @param index position of the required row
     *              in the matrix.
     * @param value new List of weight cells.
     */
    public void setRow(int index, List<E> value) {
        matrix.set(index, value);
    }

    /**
     * Adds a new, empty row to the bottom of the
     * matrix.
     */
    public void addRow() {
        addRow(new ArrayList<>());
    }

    /**
     * Adds a new row to the bottom of the matrix.
     *
     * @param value new List of weight cells.
     */
    public void addRow(List<E> value) {
        matrix.add(value);
    }

    /**
     * Fills a row with the specified values. The
     * number values is determined by the index
     * value, which should point to the next
     * available row position.
     *
     * @param index row position, to be filled.
     * @param value used to fill the row with.
     */
    public void fillRow(int index, E value) {
        // Matrix is symmetric, so row
        // index will also be the same
        // number of columns to add.
        for (int i = 0; i <= index; i++) {
            addCell(index, value);
        }
    }

    /**
     * Removes a specific row.
     *
     * @param index of the row to be removed.
     */
    public void removeRow(int index) {
        matrix.remove(index);
    }

    /**
     * Gets a matrix column, using the positional
     * index.
     *
     * @param index position of the required
     *              column in the matrix.
     * @return List of weight cells for the
     * column.
     */
    public List<E> getColumn(int index) {
        List<E> column = new ArrayList<>();
        for (List<E> cell : matrix) {
            column.add(cell.get(index));
        }
        return column;
    }

    /**
     * Replaces a matrix column.
     *
     * @param index position of the required
     *              column in the matrix.
     * @param value the new value of weight cells.
     */
    public void setColumn(int index, E value) {
        // Go through all rows and set
        for (List<E> row : matrix) {
            row.set(index, value);
        }
    }

    /**
     * Adds a new column to the end of every
     * row.
     *
     * @param value the new value of weight cells.
     */
    public void addColumn(E value) {
        // Go through all rows and add
        for (List<E> row : matrix) {
            row.add(value);
        }
    }

    /**
     * Removes column from every row.
     *
     * @param index of the column to be removed.
     */
    public void removeColumn(int index) {
        // Go through all rows and remove
        for (List<E> row : matrix) {
            row.remove(index);
        }
    }

    /**
     * Gets a specific cell in the matrix.
     *
     * @param rowIndex index of the row.
     * @param columnIndex index of column.
     * @return Weight cell of a generic type.
     */
    public E getCell(int rowIndex, int columnIndex) {
        List<E> row = matrix.get(rowIndex);
        if (row == null) {
            return null;
        }
        return row.get(columnIndex);
    }

    /**
     * Replaces a specific cell in the matrix.
     *
     * @param rowIndex index of the row.
     * @param columnIndex index of column.
     * @param value of the new cell.
     */
    public void setCell(int rowIndex, int columnIndex, E value) {
        List<E> row = matrix.get(rowIndex);
        if (row == null) {
            return;
        }
        row.set(columnIndex, value);
    }

    /**
     * Adds a new value to the end of an
     * exiting row.
     *
     * @param rowIndex index of the row.
     * @param value of the new cell.
     */
    public void addCell(int rowIndex, E value) {
        List<E> row = matrix.get(rowIndex);
        if (row == null) {
            return;
        }
        row.add(value);
    }

    /**
     * Removes an exiting cell.
     *
     * @param rowIndex index of the row.
     * @param columnIndex index of column.
     * @return the cell that has been removed.
     */
    public E removeCell(int rowIndex, int columnIndex) {
        List<E> row = matrix.get(rowIndex);
        if (row == null) {
            return null;
        }
        return row.remove(columnIndex);
    }

}
