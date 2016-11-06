package pokemon.ui;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
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
		Listener sortListener = new Listener() {
			public void handleEvent(Event e)
			{
				TableItem[] rows = table.getItems();
				TableColumn column = (TableColumn)e.widget;
				int columnIndex = table.indexOf(column);
				if (table.getSortColumn() == column)
					if (table.getSortDirection() == SWT.UP) {
						table.setSortDirection(SWT.DOWN);
						// reverse the rows.
					} else {
						// set sortDireciton to SWT.UP, reverse the rows.
						table.setSortDirection(SWT.UP);
					}
				else {
					table.setSortDirection(SWT.UP);
					table.setSortColumn(column);
					// sort the column ascendingly
				}
			}
		};
		for (int i = 0; i < columns.length; i++)
			columns[i].addListener(SWT.Selection, sortListener);
	}
	
// TODO: Figure out this sorting business
	// We can't sort the array in place, since creating new @TableItem@s instantly adds
	// them to the table and we want to wait. The solution is to create an array of string[]s
	// storing each row, and from which we make the new table afterward.
	public String[][] newRowOrder(int column, TableItem[] rows)
	{
		int columnCount = rows[0].getParent().getColumns().length;
		String[][] newRows = new String[rows.length][];
		for (int rowIndex = 0; rowIndex < rows.length; rowIndex++)
			newRows[rowIndex] = rowToStrings(columnCount, rows[rowIndex]);
		return newRows;
	}
	
	// Simply returns a string array containing, in order, each of the labels in the TableItem.
	String[] rowToStrings(int columnCount, TableItem row)
	{
		String[] rowString = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			rowString[i] = row.getText(i);
		}
		return rowString;
	}
	/**
	 * Create table headers String
	 * 
	 * @return
	 */
	private List<String> getTableHeaders() {
		List<String> ret = new ArrayList<String>();
		// TODO: Create the headers for the Table based on Pokemon attributes  
		return ret;
	}

}