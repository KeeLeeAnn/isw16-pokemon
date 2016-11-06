package pokemon.ui;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pokemon.data.Pokemon;

/**
 * Pokemon UIDialog displays Pokemons in SWT Table Widget
 *
 */
public class PokemonUI extends Dialog {

	private List<Pokemon> pokemons = new ArrayList<Pokemon>();

	/**
	 * @param parent
	 * @param pokemons
	 */
	public PokemonUI(Shell parent, List<Pokemon> pokemons) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, pokemons);
	}

	/**
	 * @param parent
	 * @param style
	 * @param pokemons
	 */
	public PokemonUI(Shell parent, int style, List<Pokemon> pokemons) {
		// Let users override the default styles
		super(parent, style);
		setText("Pokemon Manager");
		setPokemons(pokemons);
	}

	/**
	 * Opens the dialog
	 */
	public void open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public List<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setPokemons(List<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 */
	private void createContents(final Shell shell) {

		shell.setLayout(new GridLayout());
		final Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);

		// Basic table set-up:
		table.setHeaderVisible(true);
		
		// Initializing the columns:
		// Instead of hard-coding each column, the below loop initializes them
		// based on the contents of the @cols@ array. Using a linear function
		// to determine the width seems to give the table header a nice sort of, smooth feel,
		// but names are often too long so they get an extra big column.
		String[] cols = { "Number", "Name", "Type", "Trainer",
						 "Swaps", "SwapAllow", "Competitions" };
		TableColumn[] columns = new TableColumn[cols.length];
		for (int i = 0; i < cols.length; i++) {
			columns[i] = new TableColumn(table, SWT.LEFT);
			columns[i].setMoveable(false);
			columns[i].setText(cols[i]);
			columns[i].setWidth(cols[i].length() * 8 + 25);
		}
		columns[1].setWidth(100);
		columns[3].setWidth(150);

		// Adding the table rows for each pokemon:
		TableItem[] initialRows = new TableItem[pokemons.size()];
		String[] rowValues = new String[7];
		// We pack each column's string value into the rowValues array so that we can
		// set the @TableItem@'s text in one statement (at the end of the loop body)
		for (int rowIndex = 0; rowIndex < initialRows.length; rowIndex++) {
			Pokemon pkmn = pokemons.get(rowIndex);
			rowValues[0] = pkmn.getNumber() + "";
			rowValues[1] = pkmn.getName();
			rowValues[2] = pkmn.getType() + "";
			
			if (pkmn.getTrainer() == null)
				rowValues[3] = "None";
			else
				rowValues[3] = pkmn.getTrainer().getFirstname() + " "
							+ pkmn.getTrainer().getLastname();
			
			rowValues[4] = pkmn.getSwaps().size() + "";
			rowValues[5] = pkmn.isSwapAllow() + "";
			rowValues[6] = pkmn.getCompetitions().size() + "";
			
			initialRows[rowIndex] = new TableItem(table, SWT.DEFAULT);
			initialRows[rowIndex].setText(rowValues);
		}
		
		// finally, add a listener to the columns that adjusts the sorting order. Same system
		// used for music files: User clicks a column header and the whole table becomes sorted
		// by that field. Clicking it again reverses the sorting order.
		// To sort, we simply create and then sort an array of rows (named @rows@), then create
		// a matrix of strings representing the 
		Listener sortListener = new Listener() {
			public void handleEvent(Event e)
			{
				TableItem[] rows = table.getItems();
				TableColumn column = (TableColumn)e.widget;
				int columnIndex = table.indexOf(column);
				if (table.getSortColumn() == column)
					if (table.getSortDirection() == SWT.UP) {
						table.setSortDirection(SWT.DOWN);
						String[][] textHolder = extractText(rows);
						
						reverse(textHolder);
						
						table.removeAll();
						for (int k = 0; k < textHolder.length; k++) {
							rows[k] = new TableItem(table, SWT.DEFAULT);
							rows[k].setText(textHolder[k]);
						}
					} else {
						table.setSortDirection(SWT.UP);
						String[][] textHolder = extractText(rows);
						
						reverse(textHolder);
						
						table.removeAll();
						for (int k = 0; k < textHolder.length; k++) {
							rows[k] = new TableItem(table, SWT.DEFAULT);
							rows[k].setText(textHolder[k]);
						}
					}
				else {
					table.setSortDirection(SWT.UP);
					table.setSortColumn(column);

					sortRows(columnIndex, rows);
					
					String[][] textHolder = extractText(rows);
					table.removeAll();
					for (int k = 0; k < textHolder.length; k++) {
						rows[k] = new TableItem(table, SWT.DEFAULT);
						rows[k].setText(textHolder[k]);
					}
				}
			}
		};
		for (int i = 0; i < columns.length; i++)
			columns[i].addListener(SWT.Selection, sortListener);
	}
	
	String[][] extractText(TableItem[] rows)
	{
		String[][] rowTexts = new String[rows.length][rows[0].getParent().getColumnCount()];
		for (int i = 0; i < rowTexts.length; i++) {
			for (int j = 0; j < rowTexts[i].length; j++)
				rowTexts[i][j] = rows[i].getText(j);
		}
		return rowTexts;
	}
	// We're just going to, uh... *hope* that nobody tries to use large tables, since this uses
	// InsertionSort. It's in-place, by the way, so be careful.
	public void sortRows(int column, TableItem[] rows)
	{
		TableItem placeHolder;
		int i, j;
		for (i = 0; i < rows.length; i++) {
			placeHolder = rows[i];
			j = i - 1;
			// sorry about this, the next line is the args to @compareIgnoreCase@.
			while (j >= 0 && rows[j].getText(column).compareToIgnoreCase
					(placeHolder.getText(column)) >= 1) {
				rows[j + 1] = rows[j];
				j--;
			}
			rows[j + 1] = placeHolder;
			System.out.println("sortRows: swapped element " + i + " with element " + j);
		}
	}
	// again, in-place.
	public <T> void reverse(T[] rows)
	{
		T holder;
		for (int i = 0; i < rows.length / 2; i++) {
			holder = rows[i];
			rows[i] = rows[rows.length - i - 1];
			rows[rows.length - i - 1] = holder;
		}
	}
	

	/**
	 * Instructions unclear, code stuck in loop. Welche header sollen wir denn hier getten?
	 * Die von oben auf den Spalten, oder von einer einzelnen Zeile?
	 * 
	 * @return
	 */
//	private List<String> getTableHeaders() {
//		List<String> ret = new ArrayList<String>();
//		// Create the headers for the Table based on Pokemon attributes  
//		return ret;
//	}

}